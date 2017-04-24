package com.netcracker.education.crudlib.record.impl;

import com.netcracker.education.crudlib.record.Record;
import com.netcracker.education.crudlib.table.TableRepository;
import com.netcracker.education.crudlib.table.impl.TableRepositoryImpl;
import com.netcracker.education.crudlib.record.RecordRepository;
import com.netcracker.education.crudlib.utils.TableUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.json.simple.JSONObject;

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
        
        if (!TableUtils.getValidation(dbName, tableName)) {
            return false;
        }
        
        File file = new File(filePath);
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        
        try {
            fileWriter = new FileWriter(file, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            
            bufferedWriter.newLine();
            
            JSONObject jsonObject = new JSONObject();
            
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                
                jsonObject.put(key, value);
            }
            
            String jsonString = jsonObject.toJSONString();
            bufferedWriter.write(jsonString);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(RecordRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                    fileWriter.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(RecordRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
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
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        
        return true;
    }
    
    @Override
    public boolean update(String dbName, String tableName, Map<String, String> fields) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Record> getAll(String dbName, String tableName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Record getByFields(String dbName, String tableName, Map<String, String> fields) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
