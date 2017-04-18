/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.education.crudlib.database;

import com.netcracker.education.crudlib.table.Table;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ya
 */
public class Database {
    private String name;
    private String path;
    private Map<String, Table> tables = new HashMap<>(); // tableName, table
    
    public Database(String name) {
        this.name = name;
        this.path = DatabaseUtils.getPath() + name;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getPath(){
        return path;
    }
    
    public void putTable(String tableName, Table table){
        tables.put(tableName, table);
    }
    
    public Table getTable(String tableName){
        return tables.get(tableName);
    }
    
    public void removeTable(String tableName){
        tables.remove(tableName);
    }
}
