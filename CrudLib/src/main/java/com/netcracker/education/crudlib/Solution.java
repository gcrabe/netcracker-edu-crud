/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.education.crudlib;

import com.netcracker.education.crudlib.database.DatabaseRepository;
import com.netcracker.education.crudlib.database.impl.DatabaseRepositoryImpl;
import com.netcracker.education.crudlib.record.RecordRepository;
import com.netcracker.education.crudlib.record.impl.RecordRepositoryImpl;
import com.netcracker.education.crudlib.table.TableRepository;
import com.netcracker.education.crudlib.table.impl.TableRepositoryImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Ya
 */
public class Solution {

    public static void main(String[] args) {

        DatabaseRepository databaseRepository = DatabaseRepositoryImpl.getInstance();
        TableRepository tableRepository = TableRepositoryImpl.getInstance();
        RecordRepository recordRepository = RecordRepositoryImpl.getInstance();
        
//        HashMap<String, String> map = new HashMap<>();
//        map.put("first", "1");
//        map.put("second", "2");
//        recordRepository.create("test_db", "test_table", map);
//        recordRepository.create("test_db", "test_table", map);
//        map.clear();
//        map.put("third", "3");
//        recordRepository.create("test_db", "test_table", map);
        
//        recordRepository.delete("test_db", "test_table", map);
    }

}
