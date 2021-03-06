package com.frame.utils.mybatis;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Table;

import org.crazycake.utils.CamelNameUtils;

import com.frame.utils.ReflectUtil;
import com.frame.utils.exception.FrameException;

public class MyBatisHelper {
	
	/*
	 * 缓存entity的sql语句，提高效率，避免每次解析生成
	 */
	public static ConcurrentHashMap<String,String> sqlMap=new ConcurrentHashMap<String, String>();
	
	  /**
	   * 手动绑定数据库字段值到实体
	   * @param entity 实体
	   * @param map 数据库查询结果
	   */
		public static void copyProp1(Object entity, Map<String, Object> map) throws FrameException {
			 Field[] fields = ReflectUtil.getFields(entity.getClass());
//			 ReflectUtil.map2Object(map, entity.)
			try {
			    //此处有待商榷
				for (Field field : fields) {
					String fieldName=CamelNameUtils.camel2underscore(field.getName());
					if(map.containsKey(fieldName)){
						field.setAccessible(true);
						field.set(entity, map.get(fieldName));
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
          	throw new FrameException(entity.getClass().getName() + " copyProp exception");
			}
		}

		public static String getTableName(Class<?> clz){
			String key=clz+":tableName";
             if(!sqlMap.containsKey(key)){
            	 Table table = clz.getAnnotation(Table.class);
            	 if (table != null)
            	 sqlMap.put(key, table.name());
            	 else
            		 throw new RuntimeException(clz.getName() + " need use @Table");
             }
             return sqlMap.get(key);
		}
		
		public  static String selectColumnList(Class<?> clz) {  
			String key=clz+":selectColumnList";
			if(!sqlMap.containsKey(key)){
				Field  [] fields=ReflectUtil.getFields(clz);
		        StringBuilder sb = new StringBuilder();  
		        
		        int i=0;
		        for(Field field:fields){
		        	  String fieldName=CamelNameUtils.camel2underscore(field.getName());
		        	     if(i++ != 0)  
		                     sb.append(',');  
		                 sb.append(fieldName); 
		        }
		        sqlMap.put(key, sb.toString());
			}
	        return sqlMap.get(key);  
	    }  
		
		public static  String generateKey(StringBuilder sql, Map<String, Object> parameter) {
			String keyRs = sql.toString();
			if (parameter.size() != 1) {
				StringBuilder generateKey = new StringBuilder(sql);
				generateKey.append(":");
				for (String tempKey : parameter.keySet()) {
					if (!tempKey.equals("sql")) {
						generateKey.append(tempKey).append(":").append(String.valueOf(parameter.get(tempKey))).append(":");
					}

				}
				keyRs = generateKey.toString();
			}
			return String.valueOf(keyRs.hashCode());//因为是根据sql比较，所以string的hashcode正合适
		}
		
		
		public static String getCondition(String condition){
			condition=condition.trim();
			if(condition.startsWith("and")){
				return condition.substring(3);
			} 
				return condition;
		}
}
