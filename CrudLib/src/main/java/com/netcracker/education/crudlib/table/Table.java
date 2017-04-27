/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.education.crudlib.table;

import com.netcracker.education.crudlib.utils.DatabaseUtils;

import com.netcracker.education.crudlib.utils.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author --
 */
public class Table {

    private String name;
    private List<String> fieldNames = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(Table.class.getName());

    public Table(String tableName, List<String> fieldNames) {
        this.name = tableName;
        this.fieldNames = fieldNames;
    }

    public String getName() {
        return name;
    }

    public boolean setName(String name) {
        if (!DatabaseUtils.nameValidation(name)) {

            StringBuilder msg = new StringBuilder();
            msg.append("Incorrect table name [").append(name).append("].");
            LOGGER.error(msg.toString(), Level.ERROR);

            return false;

        } else {

            this.name = name;

            StringBuilder msg = new StringBuilder();
            msg.append("Table table name [").append(name).append("].");
            LOGGER.info(msg.toString(), Level.INFO);

            return true;
        }
    }
}
