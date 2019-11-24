package com.example.nomorekickout_teacher;

import java.util.HashMap;
import java.util.Map;

public class statics {
    public static Map<Integer, Boolean> isAwake;
    public static void initialize(){
        isAwake = new HashMap<>();
    }
    public static void putmap(Integer key, Boolean val) {
        isAwake.put(key, val);
    }
    public static Boolean getmap(Integer key) {
        return isAwake.get(key);
    }
}
