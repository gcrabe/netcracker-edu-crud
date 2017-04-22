package com.netcracker.education.crudlib.utils;

import com.netcracker.education.crudlib.table.impl.TableRepositoryImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gc
 */
public class TableUtils extends Utils {
    
    public static final Logger LOGGER = LoggerFactory.getLogger(TableRepositoryImpl.class.getName());
    
    public static boolean getValidation(String dbName, String tableName){
        StringBuilder checkValidationName = new StringBuilder();
        checkValidationName.append(dbName).append(tableName);
        return DatabaseUtils.nameValidation(checkValidationName.toString());
    }


    public static String getFullName(String dbName, String tableName){
        StringBuilder fullTableName = new StringBuilder();
        fullTableName.append(DatabaseUtils.getPath()).append(dbName).append("\\").append(tableName).append(".txt");
        return fullTableName.toString();
    }
}
