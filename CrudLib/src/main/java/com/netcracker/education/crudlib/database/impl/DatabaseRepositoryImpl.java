/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.education.crudlib.database.impl;

import com.netcracker.education.crudlib.database.Database;
import com.netcracker.education.crudlib.database.DatabaseRepository;
import com.netcracker.education.crudlib.database.DatabaseUtils;
import com.netcracker.education.crudlib.table.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ya
 */
public class DatabaseRepositoryImpl implements DatabaseRepository{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseRepositoryImpl.class.getName());
    
    //реализация паттерна Sinleton
    private static volatile DatabaseRepositoryImpl instance;
    private DatabaseRepositoryImpl(){} //запрещаем создание объекта извне
    
    public static DatabaseRepositoryImpl getInstance(){
        DatabaseRepositoryImpl localInstance = instance;
        if(localInstance == null){
            synchronized(DatabaseRepositoryImpl.class){
                localInstance = instance;
                if(localInstance == null){
                    localInstance = new DatabaseRepositoryImpl();
                    instance = localInstance;
                }
            }
        }
        return localInstance;
    }
    
    //такая мапа должна быть?
    private final Map<String, Table> tables = new HashMap<>();
    
    //описание основных методов
    @Override
    public void create(String dbName) {       
        dbName = DatabaseUtils.getPath() + dbName;
        File database = new File(dbName);
        database.mkdirs();
        //проверить корректность пути, если уже существует и прочее
        
        LOGGER.info("Database directory created.", Level.INFO);
    }

    @Override
    public void delete(String dbName) {
        File database = new File(dbName);
        database.delete();
        //проверить корректность пути, наличие файла и прочее

        StringBuilder msg = new StringBuilder();
        msg.append("Database [").append(dbName).append("] deleted successfully.");
        LOGGER.info(msg.toString(), Level.INFO);
    }

    @Override
    public void update(String dbName, String newDbName) {
        File database = new File(dbName); //нужен dbRoot?
        database.renameTo(new File(newDbName));//нужен dbRoot?
        //проверить корректность имени, существование файла и прочее

        StringBuilder msg = new StringBuilder();
        msg.append("Database [").append(dbName).append("] successfully renamed to [").append(newDbName).append("].");
        LOGGER.info(msg.toString(), Level.INFO);
    }

    @Override
    public Database getByName(String dbName) {
        Database database = new Database(dbName);

        StringBuilder msg = new StringBuilder();
        msg.append("The user requested a database named [").append(dbName).append("].");
        LOGGER.info(msg.toString(), Level.INFO);

        return database;
    }

    @Override
    public List<String> getAllNames() {        
        File root = new File(DatabaseUtils.getPath());
        String[] arrayNames = root.list();
        List<String> names = new ArrayList<>();
        names.addAll(Arrays.asList(arrayNames));//заполняем список имен

        StringBuilder msg = new StringBuilder();
        msg.append("The user requested all database names.");
        LOGGER.info(msg.toString(), Level.INFO);

        return names;
    }
    
}
