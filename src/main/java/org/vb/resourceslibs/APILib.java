package org.vb.resourceslibs;

import com.google.inject.Inject;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import org.vb.config.APIConf;
import org.vb.model.*;

@Slf4j
@Getter
public class APILib {

    private org.vb.resourceslibs.APIUtilLib apiUtilLib;
    private org.vb.resourceslibs.APIJsonLib apiJsonLib;
    private org.vb.resourceslibs.APIHttpLib apiHttpLib;
    private boolean bdirExists = false;
    //private String strLocalDirPath = "/tmp/api";
    //private String strLocalFileName = "apiapp.json";
    private String strLocalDirPath = "";
    private String strLocalFileName = "";
    private String strRemoteApiURL = "";
    private String strRemoteApiPath = "";

    /* ==========================================
     * =            Constructor                 =
     * ========================================== */
    @Inject
    public APILib(APIConf apiConf) {
        apiHttpLib = new APIHttpLib();
        apiJsonLib = new APIJsonLib();
        apiUtilLib = new APIUtilLib();

        /* verify if work dir exists and create it if it doesn't */
        bdirExists = apiUtilLib.localDirBuilder()
                .sdirPath(strLocalDirPath)
                .bdirCreate(true)
                .bdirClean(false)
                .buildlocalDir();
        strLocalDirPath = apiConf.getApiParameters().getLocalDir();
        strLocalFileName = apiConf.getApiParameters().getLocalFile();
        strRemoteApiURL = apiConf.getApiParameters().getRemoteApiUrl();
        strRemoteApiPath = apiConf.getApiParameters().getRemoteApiPath();

    }

    /* ===============================
     * =      getCountryNames        =
     * ===============================
     * method to get country names from
     * provided csv string with country codes
     * ===============================
     * Receives:
     *  - String strCountryCodes:
     *   CSV string with country codes
     *   Note: Could be w/wo spaces
     *  - boolean bretrieve:
     *   - true: retrieve fresh page from
     *      remote api and update local file
     *   - false - don't refresh page
     * Returns:
     *  - CountryNames countryNames:
     *   JSONArray of JSONObjects:
     *  {"countryCode":<country-code>,
     *   "countryName":<country-name>}
     *   Note: If country code doesn't exist,
     *    it returns "countryName":"Unknown Country"
     * =============================== */
    @Builder(builderMethodName = "getCountryNamesBuilder",
        buildMethodName = "buildgetCountryNames")
    public CountryNames getCountryNames(
            String strCountryCodes,
            boolean bretrieve) {

        /* Map CountryCode -> CountryName */
        Map<String, String> mapCountryCode = new HashMap<>();

        String strLocalFile = strLocalDirPath +
                "/" +
                strLocalFileName;

        if (bretrieve) {
            /* get data page as a string and save it in local file */
            long longTime = apiUtilLib.getCurrentDateTimeEpoch();
            String strTime = apiUtilLib.getDateTimeBuilder()
                    .sdateFormat("yyyy-mm-dd HH:mm:ss")
                    .isUTC(true)
                    .ddate(apiUtilLib.getCurrentDateTime())
                    .buildgetDateTime();
            log.debug("Begin retrieving page from {} at {}.",
                    "https://www.travel-advisory.info/api",
                    strTime);

            /* get page as a string */
            String strPage = apiHttpLib.getPageBuilder()
                    .intConnectTimeout(3000)
                    .intSocketTimeout(3000)
                    .strURL("https://www.travel-advisory.info/api")
                    .strQueryPath("")
                    .buildgetPage();
            log.debug("Page from {} retrieved at {}.",
                    "https://www.travel-advisory.info/api",
                    strTime);
            /* save it to the file */
            boolean bwriteFile = apiUtilLib.writeToFileBuilder()
                    .sbody(strPage)
                    .slocalFileName(strLocalFile)
                    .buildwriteToFile();
            if (bwriteFile) {
                log.info("File {} is written successfully.",
                        strLocalFile);
            } else {
                log.error("Error writing local file {}.",
                        strLocalFile);
            }
        }
        /* get Country Names from file */
        /* get mapCodeName from the retrieved page */
        ArrayList<String> alCountryCodes = new ArrayList<>(
                Arrays.asList(
                strCountryCodes.split("\\ *,\\ *")));
        Map<String, String> mapCodeName = apiJsonLib.getKeyValueMapBuilder()
                .sjsonFile(strLocalFile)
                .strJsonBlockKey("data")
                .bisFile(true)
                .alKeys(alCountryCodes)
                .bisReverse(false)
                .buildgetKeyValueMap();
        Map<String, String> mapCodeNameSorted = new TreeMap<>(mapCodeName);
        /* create CountryNames JSONArray */
        CountryNames countryNames = new CountryNames();
        if (!mapCodeNameSorted.isEmpty()) {
            for (Map.Entry<String, String> strCodeName : mapCodeNameSorted.entrySet()) {
                log.info("Adding entry {} -> {} to CountryNames JSONArray.",
                       strCodeName.getKey(),
                       strCodeName.getValue());
                CountryName countryName = new CountryName();
                countryName.setCountryCode(strCodeName.getKey());
                countryName.setCountryName(strCodeName.getValue());
                countryNames.add(countryName);
            }
        } else {
            log.error("Retrieved map CountryCode -> CountryName is empty.");
        }
        return countryNames;
    }

    /* ===============================
     * =      getCountryCodes        =
     * ===============================
     * method to get country codes from
     * provided csv string with country names
     * ===============================
     * Receives:
     *  - String strCountryNames:
     *   CSV string with country names
     *   Note: Could be w/wo spaces
     *  - boolean bretrieve:
     *   - true: retrieve fresh page from
     *      remote api and update local file
     *   - false - don't refresh page
     * Returns:
     *  - CountryCodes countryCodes:
     *   JSONArray of JSONObjects:
     *  {"countryName":<country-name>,
     *   "countryCode":<country-code>}
     *   Note: If country name doesn't exist,
     *    it returns "countryCode":"Unknown Country"
     * =============================== */
    @Builder(builderMethodName = "getCountryCodesBuilder",
            buildMethodName = "buildgetCountryCodes")
    public CountryCodes getCountryCodes(
            String strCountryNames,
            boolean bretrieve) {

        /* Map CountryName -> CountryCode */
        Map<String, String> mapCountryName = new HashMap<>();

        String strLocalFile = strLocalDirPath +
                "/" +
                strLocalFileName;

        if (bretrieve) {
            /* get data page as a string and save it in local file */
            long longTime = apiUtilLib.getCurrentDateTimeEpoch();
            String strTime = apiUtilLib.getDateTimeBuilder()
                    .sdateFormat("yyyy-mm-dd HH:mm:ss")
                    .isUTC(true)
                    .ddate(apiUtilLib.getCurrentDateTime())
                    .buildgetDateTime();
            log.debug("Begin retrieving page from {} at {}.",
                    "https://www.travel-advisory.info/api",
                    strTime);

            /* get page as a string */
            String strPage = apiHttpLib.getPageBuilder()
                    .intConnectTimeout(3000)
                    .intSocketTimeout(3000)
                    .strURL("https://www.travel-advisory.info/api")
                    .strQueryPath("")
                    .buildgetPage();
            log.debug("Page from {} retrieved at {}.",
                    "https://www.travel-advisory.info/api",
                    strTime);
            /* save it to the file */
            boolean bwriteFile = apiUtilLib.writeToFileBuilder()
                    .sbody(strPage)
                    .slocalFileName(strLocalFile)
                    .buildwriteToFile();
            if (bwriteFile) {
                log.info("File {} is written successfully.",
                        strLocalFile);
            } else {
                log.error("Error writing local file {}.",
                        strLocalFile);
            }
        }
        /* get Country Names from file */
        /* get mapCodeName from the retrieved page */
        ArrayList<String> alCountryNames = new ArrayList<>(
                Arrays.asList(
                        strCountryNames.split("\\ *,\\ *")));
        Map<String, String> mapNameCode = apiJsonLib.getKeyValueMapBuilder()
                .sjsonFile(strLocalFile)
                .strJsonBlockKey("data")
                .bisFile(true)
                .alKeys(alCountryNames)
                .bisReverse(true)
                .buildgetKeyValueMap();
        Map<String, String> mapNameCodeSorted = new TreeMap<>(mapNameCode);
        /* create CountryCodes JSONArray */
        CountryCodes countryCodes = new CountryCodes();
        if (!mapNameCodeSorted.isEmpty()) {
            for (Map.Entry<String, String> strCountryName : mapNameCodeSorted.entrySet()) {
                log.info("Adding entry {} -> {} to CountryCodes JSONArray.",
                        strCountryName.getKey(),
                        strCountryName.getValue());
                CountryCode countryCode = new CountryCode();
                countryCode.setCountryName(strCountryName.getKey());
                countryCode.setCountryCode(strCountryName.getValue());
                countryCodes.add(countryCode);
            }
        } else {
            log.error("Retrieved map CountryName -> CountryCode is empty.");
        }
        return countryCodes;
    }




    /* ===============================
     * =      getRemoteApiStatus        =
     * ===============================
     * method to get remote api status
     * ===============================
     * Receives:
     *  - Nothing
     * Returns:
     *  - RemoteApiStatus remoteApiStatus:
     *   JSONObject of JSONObjects:
     *    {
     *      "api_status": {
     *        "cache": "cached",
     *        "code": 200,
     *        "status": "ok",
     *        "note": "The api works, we could fetch countries.",
     *        "count": 238
     *      }
     *    }
     * =============================== */
    @Builder(builderMethodName = "getRemoteApiStatusBuilder",
            buildMethodName = "buildgetRemoteApiStatus")
    public RemoteApiStatus getRemoteApiStatus() {

        /* Map of "api_status" key->value */
        Map<String, String> mapRemoteApiStatus = new HashMap<>();

        long longTime = apiUtilLib.getCurrentDateTimeEpoch();
        String strTime = apiUtilLib.getDateTimeBuilder()
                .sdateFormat("yyyy-mm-dd HH:mm:ss")
                .isUTC(true)
                .ddate(apiUtilLib.getCurrentDateTime())
                .buildgetDateTime();
        log.debug("Begin retrieving page from {}/{} at {}.",
                strRemoteApiURL,
                strRemoteApiPath,
                strTime);

        /* get page as a string */
        String strPage = apiHttpLib.getPageBuilder()
                .intConnectTimeout(3000)
                .intSocketTimeout(3000)
                .strURL(strRemoteApiURL)
                .strQueryPath(strRemoteApiPath)
                .buildgetPage();
        log.debug("Page from {}/{} retrieved at {}.",
                strRemoteApiURL,
                strRemoteApiPath,
                strTime);

        /* get 'api_status' block from retrieved page */
        ArrayList<String> alApiStatusKeys = new ArrayList<>();
        alApiStatusKeys.add(0, str);
        Map<String, String> mapApiStatus = apiJsonLib.getKeyValueMapFromNestedKeysBuilder()
                .sjsonFile(strPage)
                .bisFile(true)
                .alKeys(alCountryNames)
                .buildgetKeyValueMapFromNestedKeys();
        Map<String, String> mapApiStatusSorted = new TreeMap<>(mapApiStatus);
        /* create RemoteApiStatus JSONArray */
        RemoteApiStatus countryCodes = new RemoteApiStatus();
        if (!mapNameCodeSorted.isEmpty()) {
            for (Map.Entry<String, String> strCountryName : mapNameCodeSorted.entrySet()) {
                log.info("Adding entry {} -> {} to RemoteApiStatus JSONArray.",
                        strCountryName.getKey(),
                        strCountryName.getValue());
                CountryCode countryCode = new CountryCode();
                countryCode.setCountryName(strCountryName.getKey());
                countryCode.setCountryCode(strCountryName.getValue());
                countryCodes.add(countryCode);
            }
        } else {
            log.error("Retrieved map CountryName -> CountryCode is empty.");
        }
        return countryCodes;
    }
    
    
    
    
}
