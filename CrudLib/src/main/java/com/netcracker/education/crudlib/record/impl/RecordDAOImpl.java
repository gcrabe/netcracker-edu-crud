package com.netcracker.education.crudlib.record.impl;

import com.netcracker.education.crudlib.record.Record;
import com.netcracker.education.crudlib.record.RecordDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 *
 * @author batyrev
 */
public class RecordDAOImpl implements RecordDAO {

    public static final Logger LOGGER = LoggerFactory.getLogger(RecordDAOImpl.class.getName());

    @Override
    public void create(String dbName, String tableName, Map<String, String> fields) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
