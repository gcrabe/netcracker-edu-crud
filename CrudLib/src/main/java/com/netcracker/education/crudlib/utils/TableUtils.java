package com.netcracker.education.crudlib.utils;

import com.netcracker.education.crudlib.database.Database;
import com.netcracker.education.crudlib.database.impl.DatabaseRepositoryImpl;
import com.netcracker.education.crudlib.record.Record;
import com.netcracker.education.crudlib.table.Table;
import com.netcracker.education.crudlib.table.impl.TableRepositoryImpl;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public static boolean writeToTableStorage(String dbName, Table table) {
        Database database = DatabaseRepositoryImpl.getInstance().getByName(dbName);
        String storagePath = database.getPath() + database.getName() + "TableStore.txt";

        File file = new File(storagePath);
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            fileWriter = new FileWriter(file, true);
            bufferedWriter = new BufferedWriter(fileWriter);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("name", table.getName());
            jsonObject.put("fieldNames", table.getFieldNames());

            String jsonString = jsonObject.toJSONString();

            bufferedWriter.write(jsonString);
            bufferedWriter.newLine();

            StringBuilder msg = new StringBuilder();
            msg.append("Table [").append(table.toString()).append("] was serialized in file [")
                    .append(storagePath).append("].");
            LOGGER.info(msg.toString(), org.slf4j.event.Level.INFO);
        } catch (IOException ex) {
            StringBuilder msg = new StringBuilder();
            msg.append("Table [").append(table.toString()).append("] cant be serialized in file [")
                    .append(storagePath).append("].");
            LOGGER.error(msg.toString(), Level.ERROR);
            return false;
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                    fileWriter.close();
                } catch (IOException ex) {
                    StringBuilder msg = new StringBuilder();
                    msg.append("Table [").append(table.toString()).append("] cant be serialized in file [")
                            .append(storagePath).append("].");
                    LOGGER.error(msg.toString(), Level.ERROR);
                    return false;
                }
            }
        }

        return true;
    }

    public static List<Table> readTablesFromStorage(Database database) {
        String storagePath = database.getPath() + database.getName() + "TableStore.txt";

        File file = new File(storagePath);
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        List<Table> tables = new ArrayList<>();

        try {
            fileReader = new FileReader(file);
            bufferedReader= new BufferedReader(fileReader);

            JSONObject jsonObject = new JSONObject();

            ArrayList<String> lines = new ArrayList<>();
            String tempLine = null;

            while ((tempLine = bufferedReader.readLine()) != null) {
                if (tempLine.trim().isEmpty()) {
                    continue;
                }

                lines.add(tempLine);
            }

            ArrayList<JSONObject> objects = new ArrayList<>();

            for (String line : lines) {
                JSONObject object = (JSONObject) JSONValue.parseWithException(line);
                objects.add(object);
            }

            for (JSONObject object : objects) {
                String name = (String) object.get("name");
                List<String> fieldNames = (ArrayList) object.get("fieldNames");
                Table table = new Table(name, fieldNames);
                tables.add(table);
            }

            StringBuilder msg = new StringBuilder();
            msg.append("Tables [").append(tables.toString())
                    .append("] was desserialized from [")
                    .append(storagePath).append("].");
            LOGGER.info(msg.toString(), org.slf4j.event.Level.INFO);
        } catch (IOException ex) {
            StringBuilder msg = new StringBuilder();
            msg.append("Tables [").append(tables.toString())
                    .append("] cant be desserialized from [")
                    .append(storagePath).append("].");
            LOGGER.error(msg.toString(), Level.ERROR);
            return null;
        } catch (ParseException e) {
            StringBuilder msg = new StringBuilder();
            msg.append("Tables [").append(tables.toString())
                    .append("] cant be parsed from [")
                    .append(storagePath).append("].");
            LOGGER.error(msg.toString(), Level.ERROR);
            return null;
        } finally {
            if (bufferedReader!= null) {
                try {
                    bufferedReader.close();
                    fileReader.close();
                } catch (IOException ex) {
                    StringBuilder msg = new StringBuilder();
                    msg.append("Tables [").append(tables.toString())
                            .append("] cant be desserialized from [")
                            .append(storagePath).append("].");
                    LOGGER.error(msg.toString(), Level.ERROR);
                    return null;
                }
            }
        }

        return tables;
    }
}
