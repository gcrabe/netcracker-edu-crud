package com.netcracker.education.crudlib.utils;

import com.netcracker.education.crudlib.database.Database;
import com.netcracker.education.crudlib.database.impl.DatabaseRepositoryImpl;
import com.netcracker.education.crudlib.table.Table;
import com.netcracker.education.crudlib.table.impl.TableRepositoryImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 * @author gc
 */
public class TableUtils extends Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TableRepositoryImpl.class.getName());

    public static boolean getValidation(String dbName, String tableName) {
        StringBuilder checkValidationName = new StringBuilder();
        checkValidationName.append(dbName).append(tableName);
        return DatabaseUtils.nameValidation(checkValidationName.toString());
    }

    public static boolean getValidation(String dbName, String tableName, String newTableName) {
        StringBuilder checkValidationName = new StringBuilder();
        checkValidationName.append(dbName).append(tableName).append(newTableName);
        return DatabaseUtils.nameValidation(checkValidationName.toString());
    }

    public static String getFullName(String dbName, String tableName) {
        StringBuilder fullTableName = new StringBuilder();
        fullTableName.append(DatabaseUtils.getPath()).append(dbName).append("\\").append(tableName).append(".txt");
        return fullTableName.toString();
    }

    public static void writeToTableStore(String dbName, Table table) {
        Database database = DatabaseRepositoryImpl.getInstance().getByName(dbName);
        String dbPath = database.getPath();

        System.err.println(dbPath);
    }

    public static boolean writeTableToStorage(String dbName, Table table) {
        return true;
    }

    public static List<Table> readTablesFromStorage(Database database) {
        return null;
    }
}
