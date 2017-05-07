package com.netcracker.education.crudlib.table;

import com.netcracker.education.crudlib.table.Table;

import java.util.List;

/**
 *
 * @author --
 */
public interface TableRepository {

    public boolean create(String dbName, String tableName, List<String> fieldNames);

    public boolean delete(String dbName, String tableName);

    public boolean rename(String dbName, String tableName, String newTableName);

    public Table getByName(String dbName, String tableName);

    public List<String> getAllNames(String dbName);
}
