package com.frame.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;

import org.crazycake.utils.CamelNameUtils;

import com.frame.utils.exception.FrameException;

public class ReflectUtil {
	/**
	 * 缓存类的Field数组
	 */
	private static ConcurrentHashMap<String,Field[]> fieldsMap=new ConcurrentHashMap<String, Field[]>();
	/**
	 * 得到类以及父类的@Column注解字段,先这样处理
	 * @param clz
	 * @return Field数组
	 */
	public static  Field[] getFields(Class<?> clz){
		String clzName=clz.getName();
		 if(!fieldsMap.containsKey(clzName)){
			 List<Field> list=new ArrayList<Field>();
			 Field[] entityFields = clz.getDeclaredFields();  
			 Field[] superFields = clz.getSuperclass().getDeclaredFields();  
			 for(Field field:superFields){
				 if(field.isAnnotationPresent(Column.class)){
					 list.add(field);
				 }
			 }
			 for(Field field:entityFields){
				 if(field.isAnnotationPresent(Column.class)){
					 list.add(field);
				 }
			 }
			 fieldsMap.put(clzName, list.toArray(new Field[list.size()]));
		 }
		 return fieldsMap.get(clzName);
	}
	
	
	
	public  static  Object map2ObjectByUnderscore2camel(Map<String,Object> map,Class<?> clz){
		
		return map2Object(map, clz,true);
	}
	
   public  static  Object map2Object(Map<String,Object> map,Class<?> clz){
		
		return map2Object(map, clz,false);
	}
	
	/**
	 * 转下划线->驼峰的前提下，把map转换为Object
	 * @param map  参数map
	 * @param clz  对应的class
	 * @return 返回指定的clz类型的object对象
	 */
	private static  Object map2Object(Map<String,Object> map,Class<?> clz,boolean underscore2camel){
		try{
			 Object obj=clz.newInstance();
			    PropertyDescriptor pd;
			    Method wm;
			    String pro;
			    for(String key:map.keySet()){
			    	if(underscore2camel){
			    		pro=CamelNameUtils.underscore2camel(key);
			    	}else{
			    		pro=key;
			    	}
					 pd=new PropertyDescriptor(pro, clz);
					 wm=pd.getWriteMethod();
					 wm.invoke(obj, map.get(key));
			    }
			    return obj;
		}catch(Exception e){
			throw new FrameException(map+"转换obj错误",e);
		}
	}
	
	/**
	 * 在key值由下划线转为驼峰的前提下，返回一个符合实体类命名的map
	 * @param dbMap
	 * @return
	 */
	public static Map<String,Object> map2MapByUnderscore2camel(Map<String,Object> map){
		Map<String,Object> entity=new HashMap<String,Object>();
		for(String key:map.keySet()){
				entity.put(CamelNameUtils.underscore2camel(key), map.get(key));
		}
		
		return entity;
	}
	/**
	 * 对象转换为map
	 * @param obj  指定的对象
	 * @return 返回map
	 */
	public static Map<String,Object> object2Map(Object obj)  {
		Map<String, Object> map=new HashMap<String,Object>(); 
		Class<?> clz=obj.getClass();
		Field [] fields=getFields(clz);
		String fieldName;
	    PropertyDescriptor pd;
	    Method rm;
	    try{
	    	for(Field field:fields){
				fieldName=field.getName();
				pd =new PropertyDescriptor(fieldName, clz);   
				rm = pd.getReadMethod();
				map.put(fieldName, rm.invoke(obj));
			}
	    }catch(Exception e){
	    	throw new FrameException(obj+"转换map错误",e);
	    }
		return map;
	}
	
	/**
	 * 根据指定的属性，把对应的字段放入map对象并返回
	 * @param obj 指定的对象
	 * @param properties 指定的属性
	 * @return 封装的map
	 */
	public static Map<String,Object> object2MapByProperty(Object obj,String [] properties)  {
		Map<String, Object> map=new HashMap<String,Object>(); 
		Class<?> clz=obj.getClass();
	    PropertyDescriptor pd;
	    Method rm;
	    try{
	    	for(String  propertyName:properties){
				pd =new PropertyDescriptor(propertyName, clz);   
				rm = pd.getReadMethod();
				map.put(propertyName, rm.invoke(obj));
			}
	    }catch(Exception e){
	    	throw new FrameException(obj+"转换map错误"+properties,e);
	    }
		return map;
	}
	
}
