package com.netcracker.education.crudlib.record.impl;

import com.netcracker.education.crudlib.record.Record;
import com.netcracker.education.crudlib.table.TableRepository;
import com.netcracker.education.crudlib.table.impl.TableRepositoryImpl;
import com.netcracker.education.crudlib.record.RecordRepository;
import com.netcracker.education.crudlib.utils.RecordUtils;
import com.netcracker.education.crudlib.utils.TableUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

/**
 *
 * @author batyrev
 */
public class RecordRepositoryImpl implements RecordRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecordRepositoryImpl.class.getName());

    private static TableRepository tableRepositoryInstance = TableRepositoryImpl.getInstance();

    private static RecordRepositoryImpl instance;

    private RecordRepositoryImpl() {
    }

    public static RecordRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new RecordRepositoryImpl();
        }

        return instance;
    }

    @Override
    public boolean create(String dbName, String tableName, Map<String, String> fields) {
        String filePath = TableUtils.getFullName(dbName, tableName);

        if (!RecordUtils.getValidation(dbName, tableName)) {
            StringBuilder msg = new StringBuilder();
            msg.append("Invalid characters in path [").append(dbName)
                    .append("/").append(tableName).append("].");
            LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
            return false;
        }

        File file = new File(filePath);
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        String jsonString = "";

        try {
            fileWriter = new FileWriter(file, true);
            bufferedWriter = new BufferedWriter(fileWriter);

            JSONObject jsonObject = new JSONObject();

            for (Map.Entry<String, String> entry : fields.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                jsonObject.put(key, value);
            }

            jsonString = jsonObject.toJSONString();
            bufferedWriter.write(jsonString);

            bufferedWriter.newLine();

            StringBuilder msg = new StringBuilder();
            msg.append("Record ").append(jsonString).append(" was created in table [")
                    .append(tableName).append("], database [").append(dbName).append("].");
            LOGGER.info(msg.toString(), org.slf4j.event.Level.INFO);
        } catch (IOException ex) {
            StringBuilder msg = new StringBuilder();
            msg.append("Record ").append(jsonString).append(" cant be created in table [")
                    .append(tableName).append("], database [").append(dbName).append("].");
            LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
            return false;
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                    fileWriter.close();
                } catch (IOException ex) {
                    StringBuilder msg = new StringBuilder();
                    msg.append("File [").append(tableName)
                            .append(".txt] cant be closed in database [")
                            .append(dbName).append("].");
                    LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean delete(String dbName, String tableName, Map<String, String> fields) {
        String filePath = TableUtils.getFullName(dbName, tableName);

        if (!RecordUtils.getValidation(dbName, tableName)) {
            StringBuilder msg = new StringBuilder();
            msg.append("Invalid characters in path [").append(dbName)
                    .append("/").append(tableName).append("].");
            LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
            return false;
        }

        File file = new File(filePath);
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        ArrayList<JSONObject> objects = null;

        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            ArrayList<String> lines = new ArrayList<>();
            String tempLine = null;

            while ((tempLine = bufferedReader.readLine()) != null) {
                if (tempLine.trim().isEmpty()) {
                    continue;
                }

                boolean isCorrect = true;

                for (Map.Entry<String, String> entry : fields.entrySet()) {
                    String pattern = '\"' + entry.getKey() + "\":\"" + entry.getValue() + '\"';

                    if (tempLine.contains(pattern)) {
                        isCorrect = false;
                    }
                }

                if (isCorrect) {
                    lines.add(tempLine);
                }
            }

            objects = new ArrayList<>();

            for (String line : lines) {
                JSONObject object = (JSONObject) JSONValue.parseWithException(line);
                objects.add(object);
            }

            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);

            fileWriter.write("");

            for (JSONObject object : objects) {
                String jsonString = object.toJSONString();
                bufferedWriter.write(jsonString);
                bufferedWriter.newLine();
            }

            StringBuilder msg = new StringBuilder("Record(s) ");

            for (int i = 0; i < objects.size(); i++) {
                JSONObject object = objects.get(i);
                msg.append(object.toJSONString());

                if (i + 1 != objects.size()) {
                    msg.append(", ");
                }
            }

            msg.append(" was(were) deleted in table [")
                    .append(tableName).append("], database [").append(dbName).append("].");
            LOGGER.info(msg.toString(), org.slf4j.event.Level.INFO);
        } catch (FileNotFoundException ex) {
            StringBuilder msg = new StringBuilder();
            msg.append("File [").append(tableName).append("] was not found ")
                    .append("in database [").append(dbName).append("].");
            LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
            return false;
        } catch (IOException ex) {
            StringBuilder msg = new StringBuilder("Record(s) ");

            for (int i = 0; i < objects.size(); i++) {
                JSONObject object = objects.get(i);
                msg.append(object.toJSONString());

                if (i + 1 != objects.size()) {
                    msg.append(", ");
                }
            }

            msg.append(" cant be deleted in table [").append(tableName)
                    .append("], database [").append(dbName).append("].");
            LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
            return false;
        } catch (ParseException ex) {
            StringBuilder msg = new StringBuilder();
            msg.append("File [").append(tableName)
                    .append(".txt] cant be parsed in database [")
                    .append(dbName).append("].");
            LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
            return false;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                    fileReader.close();
                } catch (IOException ex) {
                    StringBuilder msg = new StringBuilder();
                    msg.append("File [").append(tableName)
                            .append(".txt] cant be closed in database [")
                            .append(dbName).append("].");
                    LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
                    return false;
                }
            }

            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                    fileWriter.close();
                } catch (IOException ex) {
                    StringBuilder msg = new StringBuilder();
                    msg.append("File [").append(tableName)
                            .append(".txt] cant be closed in database [")
                            .append(dbName).append("].");
                    LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean update(String dbName, String tableName, Map<String, String> fields,
            String newKey, String newValue) {
        String filePath = TableUtils.getFullName(dbName, tableName);

        if (!RecordUtils.getValidation(dbName, tableName)) {
            StringBuilder msg = new StringBuilder();
            msg.append("Invalid characters in path [").append(dbName)
                    .append("/").append(tableName).append("].");
            LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
            return false;
        }

        File file = new File(filePath);
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        ArrayList<JSONObject> objects = null;

        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            ArrayList<String> lines = new ArrayList<>();
            String tempLine = null;

            while ((tempLine = bufferedReader.readLine()) != null) {
                if (tempLine.trim().isEmpty()) {
                    continue;
                }

                for (Map.Entry<String, String> entry : fields.entrySet()) {
                    String pattern = '\"' + entry.getKey() + "\":\"" + entry.getValue() + '\"';

                    if (tempLine.contains(pattern)) {
                        JSONObject object = (JSONObject) JSONValue.parseWithException(tempLine);
                        Object previousKey = object.replace(newKey, newValue);

                        if (previousKey == null) {
                            StringBuilder msg = new StringBuilder();
                            msg.append("Record [").append(object.toJSONString())
                                    .append("] cant be updated in table [")
                                    .append(tableName).append("], database [")
                                    .append(dbName).append("]: ")
                                    .append("Incorrect types.");
                            return false;
                        }

                        tempLine = object.toJSONString();
                        lines.add(tempLine);
                        break;
                    }

                    lines.add(tempLine);
                }
            }

            objects = new ArrayList<>();

            for (String line : lines) {
                JSONObject object = (JSONObject) JSONValue.parseWithException(line);
                objects.add(object);
            }

            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);

            fileWriter.write("");

            for (JSONObject object : objects) {
                String jsonString = object.toJSONString();
                bufferedWriter.write(jsonString);
                bufferedWriter.newLine();
            }

            StringBuilder msg = new StringBuilder("Record(s) ");

            for (int i = 0; i < objects.size(); i++) {
                JSONObject object = objects.get(i);
                msg.append(object.toJSONString());

                if (i + 1 != objects.size()) {
                    msg.append(", ");
                }
            }

            msg.append(" was(were) updated in table [")
                    .append(tableName).append("], database [").append(dbName).append("].");
            LOGGER.info(msg.toString(), org.slf4j.event.Level.INFO);
        } catch (FileNotFoundException ex) {
            StringBuilder msg = new StringBuilder();
            msg.append("File [").append(tableName).append("] was not found ")
                    .append("in database [").append(dbName).append("].");
            LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
            return false;
        } catch (IOException ex) {
            StringBuilder msg = new StringBuilder("Record(s) ");

            for (int i = 0; i < objects.size(); i++) {
                JSONObject object = objects.get(i);
                msg.append(object.toJSONString());

                if (i + 1 != objects.size()) {
                    msg.append(", ");
                }
            }

            msg.append(" cant be updated in table [").append(tableName)
                    .append("], database [").append(dbName).append("].");
            LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
            return false;
        } catch (ParseException ex) {
            StringBuilder msg = new StringBuilder();
            msg.append("File [").append(tableName)
                    .append(".txt] cant be parsed in database [")
                    .append(dbName).append("].");
            LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
            return false;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                    fileReader.close();
                } catch (IOException ex) {
                    StringBuilder msg = new StringBuilder();
                    msg.append("File [").append(tableName)
                            .append(".txt] cant be closed in database [")
                            .append(dbName).append("].");
                    LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
                    return false;
                }
            }

            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                    fileWriter.close();
                } catch (IOException ex) {
                    StringBuilder msg = new StringBuilder();
                    msg.append("File [").append(tableName)
                            .append(".txt] cant be closed in database [")
                            .append(dbName).append("].");
                    LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public List<Record> getAll(String dbName, String tableName) {
        String filePath = TableUtils.getFullName(dbName, tableName);

        if (!RecordUtils.getValidation(dbName, tableName)) {
            StringBuilder msg = new StringBuilder();
            msg.append("Invalid characters in path [").append(dbName)
                    .append("/").append(tableName).append("].");
            LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
            return null;
        }

        File file = new File(filePath);
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        List<Record> records = new ArrayList<>();
        ArrayList<JSONObject> objects = null;

        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            ArrayList<String> lines = new ArrayList<>();
            String tempLine = null;

            while ((tempLine = bufferedReader.readLine()) != null) {
                if (tempLine.trim().isEmpty()) {
                    continue;
                }

                lines.add(tempLine);
            }

            objects = new ArrayList<>();

            for (String line : lines) {
                JSONObject object = (JSONObject) JSONValue.parseWithException(line);
                objects.add(object);
            }

            for (JSONObject object : objects) {
                Set<Map.Entry<String, String>> entrySet = object.entrySet();
                ArrayList<String> tempList = new ArrayList<>();
                Record record = null;

                for (Map.Entry<String, String> entry : entrySet) {
                    tempList.add(entry.getValue());
                }

                record = new Record(tempList);
                records.add(record);
            }

            StringBuilder msg = new StringBuilder("Record(s) ");

            msg.append(records.toString());
            msg.append(" was(were) taken from table [")
                    .append(tableName).append("], database [").append(dbName).append("].");
            LOGGER.info(msg.toString(), org.slf4j.event.Level.INFO);
        } catch (FileNotFoundException ex) {
            StringBuilder msg = new StringBuilder();
            msg.append("File [").append(tableName).append("] was not found ")
                    .append("in database [").append(dbName).append("].");
            LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
            return null;
        } catch (IOException ex) {
            StringBuilder msg = new StringBuilder("Record(s) ");

            for (int i = 0; i < objects.size(); i++) {
                JSONObject object = objects.get(i);
                msg.append(object.toJSONString());

                if (i + 1 != objects.size()) {
                    msg.append(", ");
                }
            }

            msg.append(" cant be taken from table [").append(tableName)
                    .append("], database [").append(dbName).append("].");
            LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
            return null;
        } catch (ParseException ex) {
            StringBuilder msg = new StringBuilder();
            msg.append("File [").append(tableName)
                    .append(".txt] cant be parsed in database [")
                    .append(dbName).append("].");
            LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                    fileReader.close();
                } catch (IOException ex) {
                    StringBuilder msg = new StringBuilder();
                    msg.append("File [").append(tableName)
                            .append(".txt] cant be closed in database [")
                            .append(dbName).append("].");
                    LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
                    return null;
                }
            }
        }

        return records;
    }

    @Override
    public List<Record> getByFields(String dbName, String tableName, Map<String, String> fields) {
        String filePath = TableUtils.getFullName(dbName, tableName);

        if (!RecordUtils.getValidation(dbName, tableName)) {
            StringBuilder msg = new StringBuilder();
            msg.append("Invalid characters in path [").append(dbName)
                    .append("/").append(tableName).append("].");
            LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
            return null;
        }

        File file = new File(filePath);
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        List<Record> records = new ArrayList<>();
        ArrayList<JSONObject> objects = null;

        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            ArrayList<String> lines = new ArrayList<>();
            String tempLine = null;

            while ((tempLine = bufferedReader.readLine()) != null) {
                if (tempLine.trim().isEmpty()) {
                    continue;
                }

                for (Map.Entry<String, String> entry : fields.entrySet()) {
                    String pattern = '\"' + entry.getKey() + "\":\"" + entry.getValue() + '\"';

                    if (tempLine.contains(pattern)) {
                        lines.add(tempLine);
                        break;
                    }
                }
            }

            objects = new ArrayList<>();

            for (String line : lines) {
                JSONObject object = (JSONObject) JSONValue.parseWithException(line);
                objects.add(object);
            }

            for (JSONObject object : objects) {
                Set<Map.Entry<String, String>> entrySet = object.entrySet();
                ArrayList<String> tempList = new ArrayList<>();
                Record record = null;

                for (Map.Entry<String, String> entry : entrySet) {
                    tempList.add(entry.getValue());
                }

                record = new Record(tempList);
                records.add(record);
            }

            StringBuilder msg = new StringBuilder("Record(s) ");

            msg.append(records.toString());
            msg.append(" was(were) taken from table [")
                    .append(tableName).append("], database [").append(dbName).append("].");
            LOGGER.info(msg.toString(), org.slf4j.event.Level.INFO);
        } catch (FileNotFoundException ex) {
            StringBuilder msg = new StringBuilder();
            msg.append("File [").append(tableName).append("] was not found ")
                    .append("in database [").append(dbName).append("].");
            LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
            return null;
        } catch (IOException ex) {
            StringBuilder msg = new StringBuilder("Record(s) ");

            for (int i = 0; i < objects.size(); i++) {
                JSONObject object = objects.get(i);
                msg.append(object.toJSONString());

                if (i + 1 != objects.size()) {
                    msg.append(", ");
                }
            }

            msg.append(" cant be taken from table [").append(tableName)
                    .append("], database [").append(dbName).append("].");
            LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
            return null;
        } catch (ParseException ex) {
            StringBuilder msg = new StringBuilder();
            msg.append("File [").append(tableName)
                    .append(".txt] cant be parsed in database [")
                    .append(dbName).append("].");
            LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                    fileReader.close();
                } catch (IOException ex) {
                    StringBuilder msg = new StringBuilder();
                    msg.append("File [").append(tableName)
                            .append(".txt] cant be closed in database [")
                            .append(dbName).append("].");
                    LOGGER.error(msg.toString(), org.slf4j.event.Level.ERROR);
                    return null;
                }
            }
        }

        return records;
    }

}
