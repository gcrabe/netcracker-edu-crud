package com.netcracker.education.crudlib.record;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the values of the fields 
 * returned by RecordDAO interface methods
 * @author gc
 */
public class Record {
    
    List<String> fields = new ArrayList<>();
    
    public Record() {}
    
    public Record(List<String> fields) {
	this.fields = fields;
    }
    
    public List<String> getFields() {
        return fields;
    }
    
    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
