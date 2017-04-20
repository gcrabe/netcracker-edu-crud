/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.education.crudlib.table.impl;

import com.netcracker.education.crudlib.database.DatabaseRepository;
import com.netcracker.education.crudlib.database.impl.DatabaseRepositoryImpl;
import com.netcracker.education.crudlib.database.DatabaseUtils;
import com.netcracker.education.crudlib.database.Database;
import com.netcracker.education.crudlib.table.Table;
import com.netcracker.education.crudlib.table.TableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author --
 */
public class TableRepositoryImpl implements TableRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(TableRepositoryImpl.class.getName());

    DatabaseRepository databaseRepositoryImplInstance = DatabaseRepositoryImpl.getInstance();

    private static volatile TableRepositoryImpl instance;
    private TableRepositoryImpl(){}

    public static TableRepositoryImpl getInstance(){
        if (instance != null){
            instance = new TableRepositoryImpl();
            return instance;
        } else return instance;
    }

    @Override
    public boolean create(String dbName, String tableName, List<String> fieldNames) {
        File file = null;

        try {
            StringBuilder fullTableName = new StringBuilder();

            StringBuilder checkValidationName = new StringBuilder();
            checkValidationName.append(dbName).append(tableName);

            if(DatabaseUtils.nameValidation(String.valueOf(checkValidationName))) {
                fullTableName.append(DatabaseUtils.getPath()).append(dbName).append('/').append(tableName).append(".txt");//добавляем расширение txt
            } else {
                StringBuilder msg = new StringBuilder();
                msg.append("Incorrect name [").append(dbName).append("] or [").append(tableName).append("].");
                LOGGER.error(msg.toString(), Level.ERROR);
                return false;
            }
            file = new File(dbName, String.valueOf(fullTableName));

            if (file.createNewFile()) {
                Table table = new Table(tableName, fieldNames);
                Database dbEx = databaseRepositoryImplInstance.getByName(dbName);
                dbEx.putTable(tableName, table);
                databaseRepositoryImplInstance.update(dbEx);//возврат объекта на место


                StringBuilder msg = new StringBuilder();
                msg.append("Table [").append(tableName).append("] in database [").append(dbName).append("] created successfully.");
                LOGGER.info(msg.toString(), Level.INFO);

                return true;
            } else {
                StringBuilder msg = new StringBuilder();
                msg.append("Can't create file [").append(tableName).append("] in database [").append(tableName).append("].");
                LOGGER.error(msg.toString(), Level.ERROR);

                return false;
            }
        }
        catch(IOException e){

//            LOGGER.error(...); что логать? WAT I NEED TO WRITE IN LOG FILE???
            StringBuilder msg = new StringBuilder();
            msg.append("Some shit.");
            LOGGER.error(msg.toString(), Level.ERROR);
            return false;
        }
    }

    @Override
    public boolean delete(String dbName, String tableName)  {
        File file = null;

        try{
            StringBuffer fullTableName = new StringBuffer();

            StringBuffer checkValidationName = new StringBuffer();
            checkValidationName.append(dbName).append(tableName);

            if(DatabaseUtils.nameValidation(String.valueOf(checkValidationName))) {
                fullTableName.append(DatabaseUtils.getPath()).append(dbName).append('/').append(tableName).append(".txt");//добавляем расширение txt
            }
            else {
                StringBuilder msg = new StringBuilder();
                msg.append("Incorrect name [").append(dbName).append("] or [").append(tableName).append("].");
                LOGGER.error(msg.toString(), Level.ERROR);
                return false;
            }
            file = new File(dbName, String.valueOf(fullTableName));
            if(file.exists()){
                Database dbEx = databaseRepositoryImplInstance.getByName(dbName);
                dbEx.removeTable(tableName);

                file.delete();

                StringBuilder msg = new StringBuilder();
                msg.append("Table [").append(tableName).append("] in database [").append(dbName).append("] deleted successfully.");
                LOGGER.info(msg.toString(), Level.INFO);

                return true;
            } else {

                StringBuilder msg = new StringBuilder();
                msg.append("Can't delete file [").append(tableName).append("] in database [").append(tableName).append("].");
                LOGGER.error(msg.toString(), Level.ERROR);

                return false;
            }
        }catch(Throwable e){
//            LOGGER.error(...); что логать? WAT I NEED TO WRITE IN LOG FILE???
            StringBuilder msg = new StringBuilder();
            msg.append("Some shit.");
            LOGGER.error(msg.toString(), Level.ERROR);
            return false;

        }
    }


    @Override
    public boolean update(String dbName, String tableName, String newTableName)  {
        File file = null;

        try {
            StringBuffer fullNameTable = new StringBuffer();

            StringBuffer checkValidationName = new StringBuffer();
            checkValidationName.append(dbName).append(tableName).append(newTableName);//создано для проверки корректности имени

            if(DatabaseUtils.nameValidation(String.valueOf(checkValidationName))) {
                fullNameTable.append(DatabaseUtils.getPath()).append(dbName).append('/').append(tableName).append(".txt");//добавляем расширение txt
            } else {
                StringBuilder msg = new StringBuilder();
                msg.append("Incorrect name [").append(dbName).append("], [").append(tableName).append("] or [").append(newTableName).append("].");
                LOGGER.error(msg.toString(), Level.ERROR);
                return false;
            }

            file = new File(String.valueOf(fullNameTable));

            if (file.exists()){

                Database dbEx = databaseRepositoryImplInstance.getByName(dbName);
                Table table = dbEx.getTable(tableName);
                table.renameTable(dbName, tableName, newTableName);
                databaseRepositoryImplInstance.update(dbEx);//возвращаем обновленную базу

                StringBuilder msg = new StringBuilder();
                msg.append("Table [").append(tableName).append("] in database [").append(dbName).append("] renamed to [").append(newTableName).append("].");
                LOGGER.info(msg.toString(), Level.INFO);

                return true;
            } else {
                StringBuilder msg = new StringBuilder();
                msg.append("Can't rename file [").append(tableName).append("] in database [").append(dbName).append("].");
                LOGGER.error(msg.toString(), Level.ERROR);

                return false;
            }

        } catch (Throwable e) {
//            LOGGER.error(...); что логать? WAT I NEED TO WRITE IN LOG FILE???
            StringBuilder msg = new StringBuilder();
            msg.append("Some shit.");
            LOGGER.error(msg.toString(), Level.ERROR);
            return false;

        }
    }


    @Override
    public Table getByName(String dbName, String tableName) {
        Database dbEx = databaseRepositoryImplInstance.getByName(dbName);

        StringBuilder msg = new StringBuilder();
        msg.append("User requested table [").append(tableName).append("] in database [").append(dbName).append("].");
        LOGGER.error(msg.toString(), Level.ERROR);

        return dbEx.getTable(tableName);
    }

    @Override
    public List<String> getAllNames(String dbName) {
        Database dbEx = databaseRepositoryImplInstance.getByName(dbName);

        StringBuilder msg = new StringBuilder();
        msg.append("User requested all table names in database [").append(dbName).append("].");
        LOGGER.error(msg.toString(), Level.ERROR);

        return dbEx.getAllTablesNames();
    }

}