/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.education.crudlib.table;

import java.util.List;

/**
 *
 * @author --
 */
public interface TableRepository {
    public void create(String dbName,String tableName, List<String> fieldNames);
    public void delete(String dbName, String tableName);
    public void update(String dbName, String tableName, String newTableName);
    public Table getByName(String dbName, String tableName);
    public List<String> getAllNames();
}
