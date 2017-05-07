package com.netcracker.education.crudlib.utils;

/**
 *
 * @author gc
 */
public class RecordUtils extends Utils {

    public static boolean getValidation(String dbName, String tableName) {
        return TableUtils.getValidation(dbName, tableName);
    }
}
