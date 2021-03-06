package com.netcracker.education.crudlib.record;

import java.util.List;
import java.util.Map;

/**
 * Description of operations with the records in the tables
 *
 * @author batyrev
 */
public interface RecordRepository {

    public boolean create(String dbName, String tableName, Map<String, String> fields);

    public boolean delete(String dbName, String tableName, Map<String, String> fields);

    public boolean update(String dbName, String tableName, Map<String, String> fields,
            String newKey, String newValue);

    public List<Record> getAll(String dbName, String tableName);

    public List<Record> getByFields(String dbName, String tableName, Map<String, String> fields);
}
