package com.projects.socialmediaapi.security.jwt.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MapUtils {
    // -----------------------------------------------------------------------------------------------------------------

    public static Map<String, Object> toClaims(Object obj) {
        Map<String, Object> map = new HashMap<>();

        Class<?> objClass = obj.getClass();

        Arrays.stream(objClass.getDeclaredFields())
                .forEach(field -> addFieldsToMap(obj, map, field));

        return map;
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void addFieldsToMap(Object obj, Map<String, Object> map, Field field) {
        field.setAccessible(true);
        try {
            map.put(field.getName(), field.get(obj));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            field.setAccessible(false);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
}
