package org.vb.resourceslibs;

import com.google.inject.Inject;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
public class APIUtilLib {

    /* ==========================================
     * =            Constructor                 =
     * ========================================== */
    @Inject
    APIUtilLib() {}

    /* ===============================
     * =    getCurrentDateTime       =
     * ===============================
     * method to get current date/time
     * ===============================
     * Receives:
     *  - nothing
     * Returns:
     *  - Date currentDateTime
     *    as Date type
     * =============================== */
    Date getCurrentDateTime() {
        Date currentDateTime;
        try {
            Calendar obj = Calendar.getInstance();
            currentDateTime = obj.getTime();
        } catch (Exception e) {
            log.error("Get Current date/time exception: {}", e.toString());
            System.out.println("Couldn't get current time.");
            currentDateTime = null;
        }
        return currentDateTime;
    } /* end of getCurrentDateTime method */


    /* ===============================
     * =   getCurrentDateTimeEpoch   =
     * ===============================
     * method to get current date/time
     * in epoch format
     * ===============================
     * Receives:
     *  - nothing
     * Returns:
     *  - Date currentDateTime
     *    as epoch (Long)
     * =============================== */
    long getCurrentDateTimeEpoch() {
        long longEpochTime = 0;
        try {
            Calendar obj = Calendar.getInstance();
            longEpochTime = obj.getTimeInMillis();
        } catch (Exception e) {
            log.error("Get Current date/time exception: {}", e.toString());
        }
        return longEpochTime;
    } /* end of getCurrentDateTimeEpoch method */

    /* ===============================
     * =        getDateTime          =
     * ===============================
     * method to convert
     * date/time to string var
     * and to UTC zone if required
     * ===============================
     * Receives:
     *  - Date ddate: Actual date to convert
     *  - String sdateFormat: date format:
     *    - "yyyy-mm-dd HH:mm:ss"
     *    - etc.
     *  - boolean isUTC:
     *    - true - convert to UTC
     *    - false - use it as is
     * Returns:
     *  - String formattedDateTime: formatted date/time stamp
     *    as a string
     * =============================== */
    @Builder(builderMethodName = "getDateTimeBuilder",
        buildMethodName = "buildgetDateTime")
    public String getDateTime(
        Date ddate,
        String sdateFormat,
        boolean isUTC) {
        String formattedDateTime = "";
        try {
            DateFormat dateFormat = new SimpleDateFormat(sdateFormat);
            if (isUTC) {
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            }
            String sddate = dateFormat.format(ddate);
            formattedDateTime = formattedDateTime.concat(sddate);
        } catch (Exception e) {
            log.error("TWUtilLib.java. DateTime. Exception: {}", e.toString());
        }
        return formattedDateTime;
    } /* end of getDateTime method */

    /* ===============================
     * =        localDir             =
     * ===============================
     * method to verify if local dir
     * exists.
     * Could be created if required.
     * ===============================
     * Receives:
     *  - String sdirPath: actual dir path
     *  - boolean bdirCreate:
     *    - true - create dir if it doesn't exist.
     *    - false - don't create dir.
     *  - boolean bdirClean:
     *    - true - clean dir (delete all files) if it exists.
     *    - false - don't clean dir.
     * Returns:
     *  - boolean breturn:
     *    - true - if dir exists or created.
     *    - false - if dir doesn't exist or couldn't be created.
     * Note: works for file verification also
     * =============================== */
    @Builder(builderMethodName = "localDirBuilder",
        buildMethodName = "buildlocalDir")
    public boolean localDir(
        String sdirPath,
        boolean bdirCreate,
        boolean bdirClean) {

        File flocalDir = new File(sdirPath);
        /* verify if dir exists */
        boolean breturn = flocalDir.exists();
        /* if dir doesn't exist */
        if (!breturn) {
            log.warn("Local dir {} doesn't exist.",
                sdirPath);
            /* create dir if creation requested */
            if (bdirCreate) {
                try {
                    /* using mkdirs() instead of mkdir() in case
                     * the dir path is nested */
                    breturn = flocalDir.mkdirs();
                    if (breturn) {
                        log.info("Local dir {} created.",
                            sdirPath);
                    } else {
                        log.error("Couldn't create local dir {} " +
                                ". mkdir() returns value: {}.",
                            sdirPath, breturn);
                    }
                } catch (Exception e) {
                    log.error("Exception creating local dir {}: {}.",
                        sdirPath, e.toString());
                    System.out.println("Exception creating local dir.");
                }
            }
        } else { /* dir exists */
            /* verify if it has files inside and they should be cleaned up */
            if (bdirClean) {
                try {
                    File[] listOfFiles = flocalDir.listFiles();
                    if (null != listOfFiles) {
                        for (File ffile : listOfFiles) {
                            if (ffile.isFile()) {
                                try {
                                    boolean bdelResult = ffile.delete();
                                    if (bdelResult) {
                                        log.info("Old file {} is deleted.",
                                            ffile.getName());
                                    } else {
                                        log.warn("Couldn't delete file {}.",
                                            ffile.getName());
                                    }
                                } catch (Exception e) {
                                    log.error("Couldn't delete file {}. Exception: {}.",
                                        ffile.getName(), e.toString());
                                    System.out.println("Couldn't delete local file.");
                                }
                            }
                        }
                    } else {
                        log.warn("Looks like dir {} is empty.",
                            sdirPath);
                    }
                } catch (Exception e) {
                    log.error("Exception cleaning local dir {}: {}.",
                        sdirPath, e.toString());
                    System.out.println("Exception cleaning local dir.");
                }
            }
        }
        return breturn;
    } /* end of localDir method */

    /* ===============================
     * =        writeToFile          =
     * ===============================
     * method to write provided String
     * to file
     * ===============================
     * Receives:
     *  - String sbody: string to be
     *    written to file
     *  - String slocalFileName: file path/name
     *    to be written to
     * Returns:
     *  - boolean breturn:
     *    - true:if file is recorded
     *    - false: if any errors occur
     * =============================== */
    @Builder(builderMethodName = "writeToFileBuilder",
        buildMethodName = "buildwriteToFile")
    public boolean writeToFile(
        String sbody,
        String slocalFileName) {
        boolean breturn = true;
        /* create output object and stream */
        FileOutputStream fostream = null;
        PrintStream pstream = null;
        try {
            /* local file object */
            log.info("Creating destination local file {}  object.",
                slocalFileName);
            File fdestFile = new File(slocalFileName);
            /* local file stream */
            log.info("Creating destination local file {} stream.",
                slocalFileName);
            fostream = new FileOutputStream(fdestFile);
            pstream = new PrintStream(fostream, true, "UTF-8");
            pstream.print(sbody);
        } catch (Exception e) {
            log.error("Error writing to {}. Exception: {}.",
                slocalFileName, e.toString());
            System.out.println("Writing provided string to file exception.");
        } finally {
            try {
                if (fostream != null) {
                    fostream.close();
                }
                if (pstream != null) {
                    pstream.close();
                }
                log.info("All streams are closed.");
            } catch (Exception e) {
                log.error("Error closing streams after writing to file {}. Exception: {}",
                    slocalFileName, e.toString());
                System.out.println("Error closing streams after writing string to file.");
            }
        }
        return breturn;
    } /* end of writeToFile method */

} /* end of class APIUtilLib */

