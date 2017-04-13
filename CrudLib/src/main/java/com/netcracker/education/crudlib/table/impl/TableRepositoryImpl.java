/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.education.crudlib.table.impl;

import com.netcracker.education.crudlib.record.impl.RecordDAOImpl;
import com.netcracker.education.crudlib.table.Table;
import com.netcracker.education.crudlib.table.TableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 * @author --
 */
public class TableRepositoryImpl implements TableRepository{

    public static final Logger LOGGER = LoggerFactory.getLogger(TableRepositoryImpl.class.getName());

    @Override
    public void create(String dbName, String tableName, List<String> fieldNames) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(String dbName, String tableName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(String dbName, String tableName, String newTableName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Table getByName(String dbName, String tableName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getAllNames(String dbName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
