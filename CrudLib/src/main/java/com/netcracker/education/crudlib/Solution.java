/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.education.crudlib;

import com.netcracker.education.crudlib.database.DatabaseRepository;
import com.netcracker.education.crudlib.database.impl.DatabaseRepositoryImpl;

/**
 *
 * @author Ya
 */
public class Solution {

    public static void main(String[] args) {

        DatabaseRepository dbr = DatabaseRepositoryImpl.getInstance();
        dbr.create("test");
        dbr.create("qwerty");
    }

}
