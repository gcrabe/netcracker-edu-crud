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
    String name;
    String path;
    Map<String, Table> tables = new HashMap<>(); // tableName, table
    
    public Database(String name) {
        this.name = name;
        //обработать путь
    }
    
    String getName(){
        return name;
    }
    
    void setName(String newName){
        name = newName;
    }
    
    String getPath(){
        return path;
    }
    
    void setPath(String newPath){
        path = newPath;
        //обработать путь
    }
   
}
