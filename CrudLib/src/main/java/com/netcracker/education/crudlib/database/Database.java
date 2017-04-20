/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.education.crudlib.database;

import com.netcracker.education.crudlib.table.Table;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 *
 * @author Ya
 */
public class Database {
    private String name;
    private String path;
    private Map<String, Table> tables = new HashMap<>(); // tableName, table

    public static final Logger LOGGER = LoggerFactory.getLogger(Database.class.getName());

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
    
    public boolean setName(String name){
        
        //проверяем корректность имени
        if(!DatabaseUtils.nameValidation(name)) {

            StringBuilder msg = new StringBuilder();
            msg.append("Incorrect database name: ").append(name);
            LOGGER.error(msg.toString(), Level.ERROR);

            return false;
        }
        //связываемся с имеющимся файлом и проверяем, существует ли он
        File dbDir = new File(this.path);
        if(!dbDir.exists()){

            StringBuilder msg = new StringBuilder();
            msg.append("Directory with path [").append(this.path).append("] is not found.");
            LOGGER.error(msg.toString(), Level.ERROR);

            return false;
        }
        
        //переименовываем Database
        this.name = name;
        
        //меняем путь на новый и переименовываем
        this.path = DatabaseUtils.getPath() + name + '/';
        dbDir.renameTo(new File(this.path));

        StringBuilder msg = new StringBuilder();
        msg.append("Database [").append(this.name).append("] renamed to [").append(name).append("] successfully.");
        LOGGER.info(msg.toString(), Level.INFO);
                
        return true;
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
