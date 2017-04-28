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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
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

        if (!TableUtils.getValidation(dbName, tableName)) {
            return false;
        }
        
        File file = new File(filePath);
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

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

            ArrayList<JSONObject> objects = new ArrayList<>();

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
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(RecordRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(RecordRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(RecordRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                    fileReader.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(RecordRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                    fileWriter.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(RecordRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return true;
    }

    @Override
    public boolean update(String dbName, String tableName, Map<String, String> fields) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Record> getAll(String dbName, String tableName) {
        String filePath = TableUtils.getFullName(dbName, tableName);

        if (!TableUtils.getValidation(dbName, tableName)) {
            return null;
        }

        File file = new File(filePath);
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        List<Record> records = new ArrayList<>();

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

            ArrayList<JSONObject> objects = new ArrayList<>();

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
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(RecordRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(RecordRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(RecordRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                    fileReader.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(RecordRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return records;
    }

    @Override
    public List<Record> getByFields(String dbName, String tableName, Map<String, String> fields) {
        String filePath = TableUtils.getFullName(dbName, tableName);

        if (!TableUtils.getValidation(dbName, tableName)) {
            return null;
        }

        File file = new File(filePath);
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        List<Record> records = new ArrayList<>();

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

            ArrayList<JSONObject> objects = new ArrayList<>();

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
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(RecordRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(RecordRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(RecordRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                    fileReader.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(RecordRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return records;//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

}
