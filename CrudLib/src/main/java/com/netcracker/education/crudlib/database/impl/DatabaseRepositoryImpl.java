/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.education.crudlib.database.impl;

import com.netcracker.education.crudlib.database.Database;
import com.netcracker.education.crudlib.database.DatabaseRepository;
import com.netcracker.education.crudlib.database.DatabaseUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ya
 */
public class DatabaseRepositoryImpl implements DatabaseRepository{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseRepositoryImpl.class.getName());
    
    //реализация паттерна Singleton
    private static DatabaseRepositoryImpl instance;
    private DatabaseRepositoryImpl(){} //запрещаем создание объекта извне
    
    public static DatabaseRepositoryImpl getInstance(){
        DatabaseRepositoryImpl localInstance = instance;
        
        if(localInstance == null){
            localInstance = instance;
            if(localInstance == null){
                localInstance = new DatabaseRepositoryImpl();
                instance = localInstance;
            }
        }
        
        return localInstance;
    }
    
    private final Map<String, Database> bases = new HashMap<>();
    
    //описание основных методов
    @Override
    public boolean create(String dbName) {
        
        //создаем файл
        boolean createDatabaseTemp = DatabaseUtils.createDatabaseRepository(dbName);
        if(!createDatabaseTemp){
            return false;
        }
        
        //создаем элемент в мапе
        Database tempDatabase = new Database(dbName);
        bases.put(dbName, tempDatabase);


        StringBuilder msg = new StringBuilder();
        msg.append("Database directory [").append(dbName).append("] created.");
        LOGGER.info(msg.toString(), Level.INFO);
        
        return true;
    }

    @Override
    public boolean delete(String dbName) {
        
        //удаляем файл
        boolean deleteDatabaseTemp = DatabaseUtils.deleteDatabaseRepository(dbName);
        if(!deleteDatabaseTemp){
            return false;
        }
        
        //удфляем из мапы
        Database tempDatabase = new Database(dbName);
        bases.remove(dbName, tempDatabase);
        
        StringBuilder msg = new StringBuilder();
        msg.append("Database [").append(dbName).append("] deleted successfully.");
        LOGGER.info(msg.toString(), Level.INFO);
        
        return true;
    }

    @Override
    public boolean update(String dbName, String newDbName) {

        /*Ай-ай-ай, почему удалили из базы файлик с уже имеющимися табличками и инфой?
        Тут лучше будет просто переименовать базу, изи скопировать данные из старой в новую, у новой задать НОВОЕ имя,
        старую удалить из мапы, а новую туда положить.*/

        //удаляем старый файл и загружаем новый
        boolean updateTemp = DatabaseUtils.deleteDatabaseRepository(dbName);
        if(!updateTemp){
            return false;
        }
        
        updateTemp = DatabaseUtils.createDatabaseRepository(newDbName);
        if(!updateTemp){
            return false;
        }
        
        //удаляем из мапы и добавляем новую
        Database tempDatabase = new Database(dbName);
        bases.remove(dbName, tempDatabase);
        
        tempDatabase = new Database(newDbName);
        bases.put(dbName, tempDatabase);
        
        StringBuilder msg = new StringBuilder();
        msg.append("Database [").append(dbName).append("] successfully renamed to [").append(newDbName).append("].");
        LOGGER.info(msg.toString(), Level.INFO);
        
        return true;
    }
    
    public boolean update(Database database, Database newDatabase){
        
        boolean updateTemp = update(database.getName(), newDatabase.getName());
        
        return updateTemp;
    }

    @Override
    public Database getByName(String dbName) {
        
        Database database = bases.get(dbName);
        
        StringBuilder msg = new StringBuilder();
        msg.append("The user requested a database named [").append(dbName).append("].");
        LOGGER.info(msg.toString(), Level.INFO);

        return database;
    }

    @Override
    public List<String> getAllNames() {
        
        List<String> names = (List<String>) bases.keySet();

        StringBuilder msg = new StringBuilder();
        msg.append("The user requested all database names.");
        LOGGER.info(msg.toString(), Level.INFO);

        return names;
    }
    
}
