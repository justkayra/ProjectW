package com.semantyca.projectw.dto;

import java.util.HashMap;
import java.util.Map;

public class Page implements IPage {
    private Map<String, Object> values = new HashMap();

    public void addValue(String key, Object value) {
        values.put(key, value);
    }

    public Map getValues() {
        return values;
    }
}
