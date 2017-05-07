package com.netcracker.education.crudlib.database;

import java.util.Set;

/**
 *
 * @author Ya
 */
public interface DatabaseRepository {

    public boolean create(String dbName);

    public boolean delete(String dbName);

    public boolean rename(String dbName, String newDbName);//+

    public Database getByName(String dbName);//+

    public Set<String> getAllNames();//+
}
