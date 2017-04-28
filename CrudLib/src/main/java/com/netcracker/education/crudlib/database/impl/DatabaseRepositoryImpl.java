/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.education.crudlib.database.impl;

import com.netcracker.education.crudlib.database.Database;
import com.netcracker.education.crudlib.database.DatabaseRepository;
import com.netcracker.education.crudlib.table.Table;
import com.netcracker.education.crudlib.utils.DatabaseUtils;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.netcracker.education.crudlib.utils.DatabaseUtils.getPath;

/**
 *
 * @author Ya
 */
public class DatabaseRepositoryImpl implements DatabaseRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseRepositoryImpl.class.getName());

    //реализация паттерна Singleton
    private static DatabaseRepositoryImpl instance;

    private final Map<String, Database> bases = new HashMap<>();

    //-------Constructors and methods-------

    private DatabaseRepositoryImpl() {
    } //запрещаем создание объекта извне

    public static DatabaseRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new DatabaseRepositoryImpl();
        }
        return instance;
    }

    //описание основных методов
    @Override
    public boolean create(String dbName) {

        //создаем файл
        boolean createDatabaseTemp = DatabaseUtils.createDatabaseRepository(dbName);
        if (!createDatabaseTemp) {
            return false;
        }

        //создаем файл конфигураций
        File tableStore = new File(dbName + "TableStore.txt");
        try {
            tableStore.createNewFile();
        } catch (IOException e) {
            //LOGGER
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
        if (!deleteDatabaseTemp) {
            return false;
        }

        //удaляем из мапы
        Database tempDatabase = new Database(dbName);
        bases.remove(dbName, tempDatabase);

        StringBuilder msg = new StringBuilder();
        msg.append("Database [").append(dbName).append("] deleted successfully.");
        LOGGER.info(msg.toString(), Level.INFO);

        return true;
    }

    @Override
    public boolean update(Database database) {

        //проверка наличия файла
        File databaseDir = new File(database.getPath());
        if (!databaseDir.exists()) {
            return false;
        }

        //обновляем базу
        bases.replace(database.getName(), database);

        /*есть мнение, что логирование об этой операции (а метод нужен нам для того, чтобы Макс
        спокойно мог добавить табличку в базу и поменять объект в bases), я добавлю на уровне работы с таблицами*/

        /*StringBuilder msg = new StringBuilder();
        msg.append("Database [").append().append("] successfully renamed to [").append(newDbName).append("].");
        LOGGER.info(msg.toString(), Level.INFO);*/

        return true;
    }

    @Override
    public boolean rename(String dbName, String newDbName) {

        //проверка корректности имени
        if (!DatabaseUtils.nameValidation(dbName)) {

            StringBuilder msg = new StringBuilder();
            msg.append("Incorrect database name: ").append(dbName);
            LOGGER.error(msg.toString(), Level.ERROR);

            return false;
        }
        //проверка наличия объекта в мапе
        if (!bases.containsKey(dbName)) {

            StringBuilder msg = new StringBuilder();
            msg.append("Database [").append(dbName).append("] is not found.");
            LOGGER.error(msg.toString(), Level.ERROR);

            return false;
        }

        Database database = bases.get(dbName);
        Database newDatabase = new Database(dbName);
        newDatabase.setName(newDbName);

        bases.remove(dbName);
        bases.put(newDbName, newDatabase);

        StringBuilder msg = new StringBuilder();
        msg.append("Database [").append(dbName).append("] renamed to [").append(newDbName).append("] successfully.");
        LOGGER.info(msg.toString(), Level.INFO);

        return true;
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
    public Set<String> getAllNames() {

        Set<String> names = bases.keySet();

        StringBuilder msg = new StringBuilder();
        msg.append("The user requested all database names.");
        LOGGER.info(msg.toString(), Level.INFO);

        return names;
    }

    //получаем базы при новом запуске
    public static Map<String, Database> getExistBases() {

        Map<String, Database> bases = new HashMap<>();
        File dbRoot = new File(DatabaseUtils.getPath());
        File[] dbNames = dbRoot.listFiles();

        for (int i = 0; i < dbNames.length; i++) {

            Database database = new Database(dbNames[i].getName());
            database.setTables(getExistTables(database));
            bases.put(database.getName(), database);
        }

        return bases;
    }

    private static Map<String, Table> getExistTables(Database database) {

        Map<String, Table> tables = new HashMap();
        File databaseFile = new File(DatabaseUtils.getPath() + database.getName());
        File[] tableFiles = databaseFile.listFiles();

        for (int i = 0; i < tableFiles.length; i++) {

            tables.put(tableFiles[i].getName(), new Table(tableFiles[i].getName(), null));
        }

        return tables;
    }

}
