/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.education.crudlib.database;

import com.netcracker.education.crudlib.database.impl.DatabaseRepositoryImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 *
 * @author Ya
 */
//нужен ли, если есть getPath для Database
public class DatabaseUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtils.class.getName());

    public static String getPath() {

        /*---------------------------------------------------------------------------
        |Так как метод используется в разных местах и у метода есть блок try-catch, |
        |то может возникнуть проблема с определением места ошибки                   |
        |(сработает catch, а логгер не зафиксирует место срабатывания данного       |
        |метода)                                                                    |
        ----------------------------------------------------------------------------*/

        Properties property = new Properties();
        String dbRoot = "";

        try (InputStream fis = DatabaseUtils.class.getClassLoader().getResourceAsStream("settings.properties");) {

            property.load(fis);
            dbRoot = property.getProperty("db.root");

            File rootDirectory = new File(dbRoot);

            if (!rootDirectory.exists()) {
                rootDirectory.mkdirs();

                StringBuilder msg = new StringBuilder();
                msg.append("Root directory created with path [").append(dbRoot).append("].");
                LOGGER.info(msg.toString(), Level.INFO);
            }

        } catch (IOException e) {
            LOGGER.error("Property file is not found.", Level.ERROR);
        }
        return dbRoot;
    }
    
    public static boolean nameValidation(String name) {
        
        Pattern pattern = Pattern.compile("(.+)?[><\\|\\?*/:\\\\\"](.+)?");
        Matcher matcher = pattern.matcher(name);
        
        return !matcher.find();
    }
    
    public static boolean createDatabaseRepository(String dbName){
        
        if(!DatabaseUtils.nameValidation(dbName)) {

            StringBuilder msg = new StringBuilder();
            msg.append("Incorrect database name: ").append(dbName);
            LOGGER.error(msg.toString(), Level.ERROR);

            return false;
        }

        StringBuilder fullPath = new StringBuilder();
        fullPath.append(DatabaseUtils.getPath()).append(dbName).append("\\");
        File databaseDirectory = new File(fullPath.toString());
        boolean creatingTemp = databaseDirectory.mkdirs();
        if(!creatingTemp){

            StringBuilder msg = new StringBuilder();
            msg.append("Database [").append(dbName).append("] cant be crated.");
            LOGGER.error(msg.toString(), Level.ERROR);

            return false;
        }
        
        return true;
    }
    
    public static boolean deleteDatabaseRepository(String dbName){
        
        if (!DatabaseUtils.nameValidation(dbName)) {

            StringBuilder msg = new StringBuilder();
            msg.append("Incorrect database name: ").append(dbName);
            LOGGER.error(msg.toString(), Level.ERROR);

            return false;
        }
        
        String fullPath = DatabaseUtils.getPath() + dbName + '/';
        File databaseDirectory = new File(fullPath);
        boolean deletingTemp = databaseDirectory.delete();
        if(deletingTemp == false){

            StringBuilder msg = new StringBuilder();
            msg.append("Database [").append(dbName).append("] cant be deleted.");
            LOGGER.error(msg.toString(), Level.ERROR);

            return false;
        }
        
        return true;
    }
}
