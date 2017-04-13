/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.education.crudlib.database;

import com.netcracker.education.crudlib.table.Table;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Ya
 */
public class Database {
    String name;
    String path;
    //нужны сеттеры и геттеры для мапы?
    Map<String, Table> tables = new HashMap<>(); // tableName, table
    
    public Database(String name) {
        FileInputStream fis;
        Properties property = new Properties();
        String dbRoot = null;
        
        try {
            fis = new FileInputStream("src/main/resources/config.properties");
            property.load(fis);
            dbRoot = property.getProperty("dbRoot");
            
            //если корень не указан, то создаем 
            if(dbRoot.isEmpty()){
                File folderOfProject = new File("dbRoot");
                folderOfProject.createNewFile();
                String newDbRoot = folderOfProject.getPath();
                property.setProperty(dbRoot, newDbRoot);
            }
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
        this.name = name;
        this.path = dbRoot + name;
        //проверить корректность пути и имени
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
   
}
