/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.education.crudlib.database.impl;

import com.netcracker.education.crudlib.database.Database;
import com.netcracker.education.crudlib.database.DatabaseRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Ya
 */
public class DatabaseRepositoryImpl implements DatabaseRepository{
    
    @Override
    public void create(String dbName) {
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
        
        dbName = dbRoot + dbName;
        File database = new File(dbName);
        database.mkdirs();
        //проверить корректность пути, если уже существует и прочее
    }

    @Override
    public void delete(String dbName) {
        File database = new File(dbName);
        database.delete();
        //проверить корректность пути, наличие файла и прочее
    }

    @Override
    public void update(String dbName, String newDbName) {
        File database = new File(dbName); //нужен dbRoot?
        database.renameTo(new File(newDbName));//нужен dbRoot?
        //проверить корректность имени, существование файла и прочее
    }

    @Override
    public Database getByName(String dbName) {
        Database database = new Database(dbName);
        return database;
    }

    @Override
    public List<String> getAllNames() {
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
        
        File root = new File(dbRoot);
        String[] arrayNames = root.list();
        List<String> names = new ArrayList<>();
        names.addAll(Arrays.asList(arrayNames));//заполняем список имен
        
        return names;
    }
    
}
