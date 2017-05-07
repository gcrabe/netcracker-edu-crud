package com.netcracker.education.crudlib.database.impl;

import com.netcracker.education.crudlib.database.Database;
import com.netcracker.education.crudlib.database.DatabaseRepository;
import com.netcracker.education.crudlib.table.Table;
import com.netcracker.education.crudlib.utils.DatabaseUtils;
import java.io.File;

import com.netcracker.education.crudlib.utils.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Ya
 */
/*Logger is correctly described for all methods in class. by ermolaxe*/
public final class DatabaseRepositoryImpl implements DatabaseRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseRepositoryImpl.class.getName());

    //реализация паттерна Singleton
    private static DatabaseRepositoryImpl instance;

    private Map<String, Database> bases = new HashMap<>();

    //-------Constructors and methods-------
    private DatabaseRepositoryImpl() {
        bases = getExistBases();
    } //запрещаем создание объекта извне

    public static DatabaseRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new DatabaseRepositoryImpl();
        }
        return instance;
    }

    //описание основных методов

    /*Logger is correctly described. by ermolaxe*/
    @Override
    public boolean create(String dbName) {

        //создаем файл
        boolean createDatabaseTemp = DatabaseUtils.createDatabaseRepository(dbName);
        if (!createDatabaseTemp) {
            return false;
        }

        //создаем файл конфигураций
        File tableStore = new File(DatabaseUtils.getPath() + dbName + '/' + dbName + "TableStore.txt");
        try {
            tableStore.createNewFile();

            StringBuilder msg = new StringBuilder();
            msg.append("Configuration file for database [").append(dbName).append("] created successfully.");
            LOGGER.info(msg.toString(), Level.INFO);
        } catch (IOException e) {
            StringBuilder msg = new StringBuilder();
            msg.append("Can't create configuration file for database [").append(dbName).append("] with exception [").append(e.getMessage()).append("].");
            LOGGER.error(msg.toString(), Level.ERROR);
        }

        //создаем элемент в мапе
        Database tempDatabase = new Database(dbName);
        bases.put(dbName, tempDatabase);

        StringBuilder msg = new StringBuilder();
        msg.append("Database directory [").append(dbName).append("] created successfully.");
        LOGGER.info(msg.toString(), Level.INFO);

        return true;
    }

    /*Logger is correctly described. by ermolaxe*/
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

    public boolean update(Database database) {

        //проверка наличия файла
        File databaseDir = new File(database.getPath());
        if (!databaseDir.exists()) {

            StringBuilder msg = new StringBuilder();
            msg.append("Database with path [").append(databaseDir.getPath()).append("] isn't exist.");
            LOGGER.error(msg.toString(), Level.ERROR);

            return false;
        }

        //обновляем базу
        bases.replace(database.getName(), database);

        /*есть мнение, что логирование об этой операции (а метод нужен нам для того, чтобы Макс
        спокойно мог добавить табличку в базу и поменять объект в bases), я добавлю на уровне работы с таблицами*/
        return true;
    }

    /*Logger is correctly described. by ermolaxe*/
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
        database.setName(newDbName);

        bases.remove(dbName);
        bases.put(newDbName, database);

        StringBuilder msg = new StringBuilder();
        msg.append("Database [").append(dbName).append("] renamed to [").append(newDbName).append("] successfully.");
        LOGGER.info(msg.toString(), Level.INFO);

        return true;
    }

    /*Logger is correctly described. by ermolaxe*/
    @Override
    public Database getByName(String dbName) {

        Database database = bases.get(dbName);

        if (database.equals(null)) {
            StringBuilder msg = new StringBuilder();
            msg.append("Method get from bases map return null for request with name [").append(dbName).append("].");
            LOGGER.error(msg.toString(), Level.ERROR);
        }

        StringBuilder msg = new StringBuilder();
        msg.append("The user requested a database named [").append(dbName).append("].");
        LOGGER.info(msg.toString(), Level.INFO);

        return database;
    }

    /*Logger is correctly described. by ermolaxe*/
    @Override
    public Set<String> getAllNames() {

        Set<String> names = bases.keySet();

        StringBuilder msg = new StringBuilder();
        msg.append("The user requested all database names.");
        LOGGER.info(msg.toString(), Level.INFO);

        return names;
    }

    //получаем базы при новом запуске
    private static Map<String, Database> getExistBases() {

        Map<String, Database> databases = new HashMap<>();
        File dbRoot = new File(DatabaseUtils.getPath());
        File[] dbFolders = dbRoot.listFiles();

        for (File dbFolder : dbFolders) {
            Database database = new Database(dbFolder.getName());
            Map<String, Table> curDatabaseTables = getExistTables(database);
            database.setTables(curDatabaseTables);
            databases.put(database.getName(), database);
        }

        return databases;
    }

    private static Map<String, Table> getExistTables(Database database) {
        Map<String, Table> tables = new HashMap();
        File databaseStoreFile = new File(DatabaseUtils.getPath()
                + database.getName() + '/' + database.getName() + "TableStore.txt");

        ArrayList<Table> tablesList = (ArrayList<Table>) TableUtils.readTablesFromStorage(database);

        for (Table table : tablesList) {
            tables.put(table.getName(), table);
        }

        return tables;
    }

}
