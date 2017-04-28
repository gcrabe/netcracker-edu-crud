/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.education.crudlib.table.impl;

import com.netcracker.education.crudlib.database.Database;
import com.netcracker.education.crudlib.database.DatabaseRepository;
import com.netcracker.education.crudlib.database.impl.DatabaseRepositoryImpl;
import com.netcracker.education.crudlib.table.Table;
import com.netcracker.education.crudlib.table.TableRepository;
import com.netcracker.education.crudlib.utils.TableUtils;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(TableRepositoryImpl.class.getName());

    DatabaseRepositoryImpl databaseRepositoryImplInstance = DatabaseRepositoryImpl.getInstance();

    private static TableRepositoryImpl instance;

    private TableRepositoryImpl() {
    }

    public static TableRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new TableRepositoryImpl();
        }
        return instance;
    }

    @Override
    public boolean create(String dbName, String tableName, List<String> fieldNames) {
        File file = null;

        String fullName;

        try {
            if (TableUtils.getValidation(dbName, tableName)) {
                fullName = TableUtils.getFullName(dbName, tableName);
            } else {
                return false;
            }

            file = new File(fullName);

            if (file.createNewFile()) {
                Table table = new Table(tableName, fieldNames);
                Database dbEx = databaseRepositoryImplInstance.getByName(dbName);
                dbEx.putTable(tableName, table);
                databaseRepositoryImplInstance.update(dbEx);//возврат объекта на место

                TableUtils.writeToTableStore(dbName, table);

                StringBuilder msg = new StringBuilder();
                msg.append("Table [").append(tableName).append("] in database [").append(dbName).append("] created successfully.");
                LOGGER.info(msg.toString(), Level.INFO);

                return true;
            } else {
                StringBuilder msg = new StringBuilder();
                msg.append("Can't create file [").append(tableName).append("] in database [").append(dbName).append("].");
                LOGGER.error(msg.toString(), Level.ERROR);

                return false;
            }
        } catch (IOException e) {

//            LOGGER.error(...); что логать? WhAT I NEED TO WRITE IN LOG FILE???
            StringBuilder msg = new StringBuilder();
            msg.append("Some shit.");
            LOGGER.error(msg.toString(), Level.ERROR);
            return false;
        }
    }

    @Override
    public boolean delete(String dbName, String tableName) {
        File file = null;

        String fullName;

        try {
            if (TableUtils.getValidation(dbName, tableName)) {
                fullName = TableUtils.getFullName(dbName, tableName);
            } else {
                return false;
            }

            file = new File(fullName);
            if (file.exists()) {
                Database dbEx = databaseRepositoryImplInstance.getByName(dbName);
                dbEx.removeTable(tableName);

                if (file.delete()) {

                    databaseRepositoryImplInstance.update(dbEx);

                    StringBuilder msg = new StringBuilder();
                    msg.append("Table [").append(tableName).append("] in database [").append(dbName).append("] deleted successfully.");
                    LOGGER.info(msg.toString(), Level.INFO);
                } else {
                    StringBuilder msg = new StringBuilder();
                    msg.append("Table [").append(tableName).append("] in database [").append(dbName).append("] is not deleted.");
                    LOGGER.info(msg.toString(), Level.INFO);

                }

                return true;
            } else {

                StringBuilder msg = new StringBuilder();
                msg.append("Can't delete file [").append(tableName).append("] in database [").append(dbName).append("].");
                LOGGER.error(msg.toString(), Level.ERROR);

                return false;
            }
        } catch (Throwable e) {
            StringBuilder msg = new StringBuilder();
            msg.append("File [").append(tableName).append("] not found.");
            LOGGER.error(msg.toString(), Level.ERROR);
            return false;

        }
    }

    @Override
    public boolean update(String dbName, String tableName, String newTableName) {
        File file = null;
        String fullName;

        try {
            if (TableUtils.getValidation(dbName, tableName, newTableName)) {
                fullName = TableUtils.getFullName(dbName, tableName);
            } else {
                return false;
            }

            file = new File(fullName);

            if (file.exists()) {

                Database dbEx = databaseRepositoryImplInstance.getByName(dbName);
                Table table = dbEx.getTable(tableName);
                table.setName(newTableName);

                String newFullName = TableUtils.getFullName(dbName, newTableName);

                if (file.renameTo(new File(newFullName))) {
                    databaseRepositoryImplInstance.update(dbEx);
                } else {
                    //LOGGER
                }

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
            StringBuilder msg = new StringBuilder();
            msg.append("File [").append(tableName).append("] not found.");
            LOGGER.error(msg.toString(), Level.ERROR);
            return false;

        }
    }

    @Override
    public Table getByName(String dbName, String tableName) {
        Database dbEx = databaseRepositoryImplInstance.getByName(dbName); //Я даже не тестил, но вижу здесь NPE.

        StringBuilder msg = new StringBuilder();
        msg.append("User requested table [").append(tableName).append("] in database [").append(dbName).append("].");
        LOGGER.error(msg.toString(), Level.ERROR);

        return dbEx.getTable(tableName);
    }

    @Override
    public List<String> getAllNames(String dbName) {
        /*Database dbEx = databaseRepositoryImplInstance.getByName(dbName);*/ //Надо ли объяснять? Снова NPE.

        StringBuilder msg = new StringBuilder();
        msg.append("User requested all table names in database [").append(dbName).append("].");
        LOGGER.error(msg.toString(), Level.ERROR);

//        return dbEx.getAllTablesNames();
        return null; //заглушка
    }

}
