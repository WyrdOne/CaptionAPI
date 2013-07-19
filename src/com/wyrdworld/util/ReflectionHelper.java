package com.wyrdworld.util;

import java.util.*;
import java.lang.reflect.*;

public class ReflectionHelper {
	private static boolean isObfuscated;
	private static Properties obfuscation = new Properties();
	
	static {
		isObfuscated = false;
		try {
			Class.forName("net.minecraft.src.MathHelper", false, ReflectionHelper.class.getClassLoader());
		} catch (ClassNotFoundException e) {
			isObfuscated = true;
		}
	}

	public static boolean isObfuscated() {
		return isObfuscated;
	}

	public static String obfuscatedName(String fieldName) {
		if (isObfuscated) {
			fieldName = obfuscation.getProperty(fieldName);
		}
		return fieldName;  
	}

	public static void addObfuscationMapping(Object obj, String fieldName, String obfuscatedName) {
		String key = obj.getClass().getName()+fieldName;
		obfuscation.put(key, obfuscatedName);
	}
	
	public static <T, E> T getPrivateValue(Class <? super E> cls, E obj, int idx) throws IllegalAccessException {
		Field field = cls.getDeclaredFields()[idx];
		field.setAccessible(true);
		return (T)field.get(obj);
    }
	
	public static <T, E> T getPrivateValue(E obj, int idx) throws IllegalAccessException {
		Class <? super E> cls = (Class)obj.getClass();
		return getPrivateValue(cls, obj, idx);
	}

	public static <T, E> T getPrivateValue(Class <? super E> cls, E obj, String fieldName) throws IllegalAccessException, NoSuchFieldException {
		Field field = cls.getDeclaredField(obfuscatedName(fieldName));
		field.setAccessible(true);
		return (T)field.get(obj);
	}
	
	public static <T, E> T getPrivateValue(E obj, String fieldName) throws IllegalAccessException, NoSuchFieldException {
		Class <? super E> cls = (Class)obj.getClass();
		return getPrivateValue(cls, obj, fieldName);
	}
	
	public static <T, E> T getPrivateValue(Class <? super E> cls, E obj, Class <? super T> fieldType, int count) throws IllegalAccessException {
		Field[] fields = cls.getDeclaredFields();
		T returnObject = null;
		
		for (int idx=0; idx<fields.length; idx++) {
			if (fields[idx].getType()==fieldType) {
				if (count==0) {
					fields[idx].setAccessible(true);
					returnObject = (T)fields[idx].get(obj);
					break;
				}
				count--;
			}
		}
		return returnObject;
	}

	public static <T, E> T getPrivateValue(E obj, Class <? super T> fieldType, int count) throws IllegalAccessException {
		Class <? super E> cls = (Class)obj.getClass();
		return getPrivateValue(cls, obj, fieldType, count);
	}
	
	public static <T, E> void setPrivateValue(Class <? super E> cls, E obj, int idx, T value) throws IllegalAccessException {
        Field field = cls.getDeclaredFields()[idx];
        field.setAccessible(true);
        field.set(obj, value);
    }
	
	public static <T, E> void setPrivateValue(E obj, int idx, T value) throws IllegalAccessException {
		Class <? super E> cls = (Class)obj.getClass();
		setPrivateValue(cls, obj, idx, value);
	}

	public static <T, E> void setPrivateValue(Class <? super E> cls, E obj, String fieldName, T value) throws IllegalAccessException, NoSuchFieldException {
		Field field = cls.getDeclaredField(obfuscatedName(fieldName));
		field.setAccessible(true);
		field.set(obj, value);
	}
	
	public static <T, E> void setPrivateValue(E obj, String fieldName, T value) throws IllegalAccessException, NoSuchFieldException {
		Class <? super E> cls = (Class)obj.getClass();
		setPrivateValue(cls, obj, fieldName, value);
	}
	
    public static <T, E> void setPrivateValue(Class <? super E> cls, E obj, Class <? super T> fieldType, int count, T value) throws IllegalAccessException {
		Field[] fields = cls.getDeclaredFields();
		
		for (int idx=0; idx<fields.length; idx++) {
			if (fields[idx].getType()==fieldType) {
				if (count==0) {
					fields[idx].setAccessible(true);
					fields[idx].set(obj, value);
					break;
				}
				count--;
			}
		}
    }

    public static <T, E> void setPrivateValue(E obj, Class <? super T> fieldType, int count, T value) throws IllegalAccessException {
		Class <? super E> cls = (Class)obj.getClass();
		setPrivateValue(cls, obj, fieldType, count, value);
    }
    
    public static Method getPrivateMethod(Class declaringClass, String methodName, Class... propertyType) {
    	Method[] declaredMethods = declaringClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
        	if (method.getName().equals(obfuscatedName(methodName)) && Arrays.equals(method.getParameterTypes(), propertyType) ) {
        		method.setAccessible(true);
        		return method;
        	}
        }
        return getPrivateMethod(declaringClass.getSuperclass(), methodName, propertyType);
    }
}
