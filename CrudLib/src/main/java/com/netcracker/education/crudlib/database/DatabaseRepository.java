/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.education.crudlib.database;

import java.util.List;

/**
 *
 * @author Ya
 */
public interface DatabaseRepository {
    public boolean create(String dbName);
    public void delete(String dbName);
    public void update(String dbName, String newDbName);
    public Database getByName(String dbName);
    public List<String> getAllNames();
}
