package com.zhilong.springcloud.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.*;

public class EntityUtils {
    private static Logger logger = LoggerFactory.getLogger(EntityUtils.class);

    /**
     * Converts array data to entity class
     * The array elements must be in the same order as the attributes in the entity class constructor
     *
     * @param list
     * @param clazz
     * @param <T>
     * @param model
     * @return
     */
    public static <T> List<T> castEntity(List<Object[]> list, Class<T> clazz, Object model) {
        List<T> returnList = new ArrayList<T>();
        if (list.isEmpty()) {
            return returnList;
        }
        // Gets the number of elements in each array collection
        Object[] co = list.get(0);

        // Gets the property name, property value, and property category of the current entity class
        List<Map> attributeInfoList = getFiledsInfo(model);
        //Create an array of attribute categories
        Class[] c2 = new Class[attributeInfoList.size()];
        // An error occurs if the number of array collection elements does not match the number of entity class attributes
        if (attributeInfoList.size() != co.length) {
            return returnList;
        }
        // Deterministic construction method
        for (int i = 0; i < attributeInfoList.size(); i++) {
            c2[i] = (Class) attributeInfoList.get(i).get("type");
        }
        try {
            for (Object[] o : list) {
                Constructor<T> constructor = clazz.getConstructor(c2);
                logger.info("Constructor : " + constructor.toString());
                returnList.add(constructor.newInstance(o));
            }
        } catch (Exception ex) {
            logger.error("An exception occurs when entity data is converted to an entity class: exception information：{}", ex.getMessage());
            return returnList;
        }
        return returnList;
    }

    /**
     * Gets the property value based on the property name
     *
     * @param fieldName
     * @param modle
     * @return
     */
    private static Object getFieldValueByName(String fieldName, Object modle) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = modle.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(modle, new Object[]{});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets a list of maps of attribute types, attribute names, and attribute values
     *
     * @param model
     * @return
     */
    private static List<Map> getFiledsInfo(Object model) {
        Field[] fields = model.getClass().getDeclaredFields();
        List<Map> list = new ArrayList(fields.length);
        Map infoMap = null;
        for (int i = 0; i < fields.length; i++) {
            infoMap = new HashMap(3);
            infoMap.put("type", fields[i].getType());
            infoMap.put("name", fields[i].getName());
            infoMap.put("value", getFieldValueByName(fields[i].getName(), model));
            list.add(infoMap);
        }
        return list;
    }

    private static <T> Map<String, Method> getFiledSetters(Class<T> clazz, List<Map<String,Object>> map) {
        logger.info("Class Name : " + clazz.getName());
        try {
            if (map.size() > 0) {
                Map<String, Object> objectMap = map.get(0);
                Set<String> filedNames = objectMap.keySet();
                Method[] declaredMethods = clazz.getDeclaredMethods();
                Map<String,Method> methodMap = new LinkedHashMap<>();
                filedNames.forEach(filedName -> {
                    try {
                        Field field = clazz.getDeclaredField(filedName);
                        Class<?> type = field.getType();
                        for (Method declaredMethod : declaredMethods) {
                            if (declaredMethod.getName().equalsIgnoreCase("set" + filedName)) {
                                //declaredMethod.invoke(instance,objectMap.get(filedName));
                                methodMap.put(filedName,declaredMethod);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("",e);
                    }
                });
                return methodMap;
            }
        } catch (Exception e) {
            logger.error("",e);
        }
        return null;
    }

    public static <T> List<T> mapCast2Entity(Class<T> clazz, List<Map<String,Object>> map) {
        List<T> list = null;
        if (map != null && map.size() > 0) {
            list = new ArrayList<>();
            Map<String, Method> filedSetters = getFiledSetters(clazz, map);
            for (Map<String, Object> objectMap : map) {
                try {
                    T instance = clazz.newInstance();
                    Set<String> keys = objectMap.keySet();
                    keys.forEach(key -> {
                        Method method = filedSetters.get(key);
                        try {
                            if(objectMap.get(key) instanceof  BigInteger) {
                                Class<?> type = clazz.getDeclaredField(key).getType();
                                if (type.getName().contains("Long") || type.getName().contains("long")) {
                                    method.invoke(instance, ((BigInteger)objectMap.get(key)).longValue());
                                }

                                if (type.getName().contains("Integer") || type.getName().contains("int")) {
                                    method.invoke(instance, ((BigInteger)objectMap.get(key)).intValue());
                                }

                            } else {
                                method.invoke(instance, objectMap.get(key));
                            }
                        } catch (Exception e) {
                            logger.error("",e);
                        }
                    });
                    list.add(instance);
                } catch (Exception e) {
                    logger.error("",e);
                }
            }
        }
        return list;
    }
}