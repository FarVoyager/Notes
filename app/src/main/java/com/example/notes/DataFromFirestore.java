package com.example.notes;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DataFromFirestore extends Note {

    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_DATE = "date";

    public DataFromFirestore(String name, String description, String date) {
        super(name, description, date);
    }

    public DataFromFirestore(String id, String name, String description, String date) {
        this(name, description, date);
        setId(id);
    }

    public DataFromFirestore(String id, Map<String, Object> fields) {
        this(id, (String) fields.get(FIELD_NAME), (String) fields.get(FIELD_DESCRIPTION), (String) fields.get(FIELD_DATE));
    }

    public DataFromFirestore(Note data) {
        this(data.getId(), data.getName(), data.getDescription(), data.getDate());
    }

    public final Map<String, Object> getFields() {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put(FIELD_NAME, getName());
        fields.put(FIELD_DESCRIPTION, getDescription());
        fields.put(FIELD_DATE, getDate());
        return Collections.unmodifiableMap(fields);
    }

}
