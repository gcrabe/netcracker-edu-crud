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
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 *
 * @author Ya
 */
//нужен ли, если есть getPath для Database
public class DatabaseUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseRepositoryImpl.class.getName());
    public static String getPath(){
        
        
        /*---------------------------------------------------------------------------
        |Так как метод используется в разных местах и у метода есть блок try-catch, |
        |то может возникнуть проблема с определением места ошибки                   |
        |(сработает catch, а логгер не зафиксирует место срабатывания данного       |
        |метода)                                                                    |
        ----------------------------------------------------------------------------*/
        
        
        // TODO: parse settings.properties(root path)
        FileInputStream fis;
        Properties property = new Properties();
        String dbRoot = "";
        try {
            fis = new FileInputStream("src/main/resources/config.properties");
            synchronized(fis){
                property.load(fis);
                dbRoot = property.getProperty("dbRoot");

                //вынести в отдельный метод?
                //если корень не указан, то создаем 
                if(dbRoot.isEmpty()){
                    File folderOfProject = new File("dbRoot");
                    folderOfProject.createNewFile();
                    String newDbRoot = folderOfProject.getPath();
                    property.setProperty(dbRoot, newDbRoot);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Property file is not found.", Level.ERROR);
        }
        return dbRoot;
    }
}
