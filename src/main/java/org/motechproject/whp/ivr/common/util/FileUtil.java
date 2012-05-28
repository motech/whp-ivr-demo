package org.motechproject.whp.ivr.common.util;


public class FileUtil {
    public static String sanitizeFilename(String filename) {
        return filename.toLowerCase().replaceAll("[ ]*_[ ]*", "_").replaceAll("[^a-z0-9-_.]+", "_");
    }
}