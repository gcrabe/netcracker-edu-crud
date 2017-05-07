package com.netcracker.education.crudlib.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Ya
 */
public class Utils {

    public static boolean nameValidation(String name) {
        Pattern pattern = Pattern.compile("(.+)?[><\\|\\?*/:\\\\\"](.+)?");
        Matcher matcher = pattern.matcher(name);

        return !matcher.find();
    }
}
