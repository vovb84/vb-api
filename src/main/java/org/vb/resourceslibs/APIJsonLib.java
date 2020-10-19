package org.vb.resourceslibs;

import com.google.inject.Inject;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class APIJsonLib {

    /* ==========================================
     * =            Constructor                 =
     * ========================================== */
    @Inject
    APIJsonLib() {}

    /* ==========================================
     * =        loadJsonObjFromFile             =
     * ==========================================
     * Method to create JSON object from file
     * Receives:
     *  - sfN: JSON file path/name.
     * Returns:
     *  - jfJO: file JSON object
     * ========================================== */
    private JSONObject loadJsonObjFromFile(
            String sfN) {
        JSONObject jfJO;
        File fjsonFile = new File(sfN);
        if (fjsonFile.exists()) {
            try {
                InputStream ifileStream = new FileInputStream(sfN);
                String sjsonTxt = IOUtils.toString(ifileStream, "UTF-8");
                jfJO = new JSONObject(sjsonTxt);
            } catch (JSONException e) {
                log.error("File {} " +
                                " is not proper JSON format file. Exception: {}",
                        sfN, e.toString());
                jfJO = new JSONObject();
            } catch (Exception e) {
                log.error("Error opening file {}" +
                                " for streaming or creating JSON object with exception: {}",
                        sfN, e.toString());
                jfJO = new JSONObject();
            }
        } else {
            log.error("File {} doesn't exist.", sfN);
            jfJO = new JSONObject();
        }
        return jfJO;
    }

    /* ==========================================
     * =             getKeyValueMap             =
     * ==========================================
     * Method to get Map of keys: String pairs
     * from provided JSON file JSONObject-block.
     * Receives:
     *  - String sjsonFile: JSON file path/name or
     *     JSON string (depends on 3rd param)
     *  - ArrayList alKeys: list of country codes
     *  - String strJsonBlockKey: json block key name
     *     where all country codes are located.
     *  - boolean bisFile:
     *    - true: if sjsonFile is file path/name
     *    - false: if sjsonFile is actual JSONObject string
     *  - boolean bisReverse:
     *    - true: find Country code from Country Name
     *    - false: find Country Name from Country Code
     * Returns:
     *  - Map<String, String> mapKeyValue.
     *    - Returns empty Map if any error
     * ========================================== */
    @Builder (builderMethodName = "getKeyValueMapBuilder",
            buildMethodName = "buildgetKeyValueMap")
    public Map<String, String> getKeyValueMap(
            String sjsonFile,
            ArrayList<String> alKeys,
            String strJsonBlockKey,
            boolean bisFile,
            boolean bisReverse) {
        Map<String, String> mapKeyValue = new HashMap<>();
        try {
            JSONObject joFile;
            /* get JSON object */
            if (bisFile) {
                joFile = loadJsonObjFromFile(sjsonFile);
            } else {
                joFile = new JSONObject(sjsonFile);
            }
            /* get whole JSON block under provided strJsonBlockKey */
            JSONObject joBlockKey = joFile.getJSONObject(strJsonBlockKey);
            if (!alKeys.isEmpty()) {
                /* get JSON object for key:value pairs located in
                 * this file under strJsonBlockKey -> CountryCode */
                for (String skey : alKeys) {
                    if (!bisReverse) {
                        skey = skey.replaceAll("\\ *", "");
                        try {
                            JSONObject joKeyValues = joBlockKey.getJSONObject(skey);
                            mapKeyValue.put(skey, joKeyValues.get("name").toString());
                        } catch (JSONException jem) {
                            log.error("Exception reading JSON values {} from file {}.",
                                    jem.toString(),
                                    sjsonFile);
                            mapKeyValue.put(skey, "Unknown Country");
                        }
                    } else {
                        /* get Map of all CountryCodes -> corresponding block;
                         * create Map CountryCode -> CountryName and loop through it */
                        Map<String, String> mapTemp = new HashMap<>();
                        Iterator<String> keys = joBlockKey.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            if (joBlockKey.get(key) instanceof JSONObject) {
                                mapTemp.put(key, ((JSONObject) joBlockKey.get(key)).get("name").toString());
                            }
                        }
                        log.info("mapTemp created: {}",
                                mapTemp.toString());
                        /* Map with only required CountryNames -> CountryCodes */
                        boolean bisCountryNameFound = false;
                        for (Map.Entry<String, String> entry : mapTemp.entrySet()) {
                            String key = entry.getKey();
                            String value = entry.getValue();
                            if (value.equals(skey)) {
                                mapKeyValue.put(value, key);
                                bisCountryNameFound = true;
                            }
                        }
                        /* If Country Name doesn't exist */
                        if (!bisCountryNameFound) {
                            mapKeyValue.put("UnknownCountry_" + skey, skey);
                        }
                    }
                }
            }
        } catch(JSONException je) {
            log.error("Exception reading JSON values {} from file {}.",
                    je.toString(),
                    sjsonFile);
        } catch(Exception e) {
            log.error("Exception in getKeyValueMap method: {}",
                    e.toString());
        }
        return mapKeyValue;
    }

    /* ==========================================
     * =      stringArrayListToJsonArray        =
     * ==========================================
     * Method to convert ArrayList to JsonArray
     * Receives:
     *  - ArrayList arrayList: ArrayList
     * Returns:
     *  - String strJsonArray;
     *     JsonArray object as a string
     *  Note: Empty string if any error
     * ========================================== */
    @Builder (builderMethodName = "stringArrayListToJsonArrayBuilder",
            buildMethodName = "buildstringArrayListToJsonArray")
    String stringArrayListToJsonArray(
            ArrayList<String> arrayList) {
        String strReturn = "";
        try {
            JSONArray jaJsonArray = new JSONArray(arrayList.toString());
            strReturn = strReturn.concat(jaJsonArray.toString());
        } catch (JSONException je) {
            log.error("Exception converting ArrayList {} to JSONArray: {}",
                    arrayList.toString(),
                    je.toString());
        } catch (Exception e) {
            log.error("General exception converting ArrayList {} to JSONArray: {}",
                    arrayList.toString(),
                    e.toString());
        }
        return strReturn;
    }

} /* end of APIJsonLib class */
