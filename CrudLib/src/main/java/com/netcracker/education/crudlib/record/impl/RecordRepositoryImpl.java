package com.netcracker.education.crudlib.record.impl;

import com.netcracker.education.crudlib.record.Record;
import com.netcracker.education.crudlib.table.TableRepository;
import com.netcracker.education.crudlib.table.impl.TableRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import com.netcracker.education.crudlib.record.RecordRepository;

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
    public void create(String dbName, String tableName, Map<String, String> fields) {
        //
    }

    @Override
    public void delete(String dbName, String tableName, Map<String, String> fields) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(String dbName, String tableName, Map<String, String> fields) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getAll(String dbName, String tableName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Record getByFields(String dbName, String tableName, Map<String, String> fields) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
