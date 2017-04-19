/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.education.crudlib.database;

import static com.netcracker.education.crudlib.record.impl.RecordDAOImpl.LOGGER;
import com.netcracker.education.crudlib.table.Table;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.event.Level;

/**
 *
 * @author Ya
 */
public class Database {
    private String name;
    private String path;
    private Map<String, Table> tables = new HashMap<>(); // tableName, table
    
    public Database(String name) {
        //нет оповещения о некорректном пути
        if (!DatabaseUtils.nameValidation(name)) {
            StringBuilder msg = new StringBuilder();
            msg.append("Incorrect database name: ").append(name);
            LOGGER.error(msg.toString(), Level.ERROR);
        }else{
            this.name = name;
            this.path = DatabaseUtils.getPath() + name + '/';
        }
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
    
    public List<String> getAllTablesNames(){
        return (List<String>) tables.keySet();
    }
}
