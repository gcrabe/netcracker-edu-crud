package com.netcracker.education.crudlib.record;

import java.util.List;
import java.util.Map;

/**
 * Description of operations with the records in the tables
 * @author batyrev
 */
public interface RecordDAO {
    
    public void create(String dbName, String tableName, Map<String, String> fields);
    
    public void delete(String dbName, String tableName, String field, String value);
    
    public void update(String dbName, String tableName, String field, String value);
    
    public List<String> getAll();
    
    public Record getByField(String dbName, String tableName, String field, String value);
}
