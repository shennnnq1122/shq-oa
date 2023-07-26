package com.shq.algorithm;

import java.lang.reflect.Field;

public class GetNumber {

    public static Object get(Object obj,String key){
        Class<?> aClass = obj.getClass();
        Field field = null;
        try {
             field = aClass.getDeclaredField(key);


        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        field.setAccessible(true);

        Object o = null;
        try {
            o = field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return o;

    }


}
