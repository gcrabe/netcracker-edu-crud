/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.education.crudlib;

import com.netcracker.education.crudlib.database.Database;
import com.netcracker.education.crudlib.database.DatabaseRepository;
import com.netcracker.education.crudlib.database.impl.DatabaseRepositoryImpl;
import com.netcracker.education.crudlib.record.RecordRepository;
import com.netcracker.education.crudlib.record.impl.RecordRepositoryImpl;
import com.netcracker.education.crudlib.table.Table;
import com.netcracker.education.crudlib.table.TableRepository;
import com.netcracker.education.crudlib.table.impl.TableRepositoryImpl;
import com.netcracker.education.crudlib.utils.TableUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author Ya
 */
public class Solution {

    public static void main(String[] args) {
        DatabaseRepository databaseRepository = DatabaseRepositoryImpl.getInstance();
        TableRepository tableRepository = TableRepositoryImpl.getInstance();
        RecordRepository recordRepository = RecordRepositoryImpl.getInstance();

//        databaseRepository.create("base");
//
//        List<String> fieldList = new ArrayList<>();
//        fieldList.add("first_name");
//        fieldList.add("last_name");
//
//        tableRepository.create("base", "table", fieldList);
//
//        TreeMap<String, String> map = new TreeMap<>();
//        map.put("first_name", "1");
//        map.put("last_name", "2");
//
//        recordRepository.create("base", "table", map);

        Database db = databaseRepository.getByName("base");
        System.out.println(databaseRepository.);
    }

}
