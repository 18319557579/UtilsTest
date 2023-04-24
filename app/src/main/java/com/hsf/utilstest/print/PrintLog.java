package com.hsf.utilstest.print;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public enum PrintLog {
    INSTANCE;

    private final Map<String, String> logMap = new HashMap<>();

    public PrintLog addParam(String key, Object value) {
        String finalValue = value == null ? "null" : value.toString();
        logMap.put(key, finalValue);
        return this;
    }

    public void print() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, String> entry : logMap.entrySet()) {
            if(!sb.toString().isEmpty()) {
                sb.append("\n");
            }
            sb.append(entry.getKey()).append(" : ").append(entry.getValue());
        }
        Log.d("Daisy", sb.toString());

        logMap.clear();
    }
}
