package com.frame.mapper.base;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;





public interface BaseMapper  {
	@InsertProvider(type = MapperTemplate.class, method = "insert")
	public   <T> int create(T entity);

	@DeleteProvider(type = MapperTemplate.class, method = "delete")
	public int delete(Map<String, Object> parameter);
	
	@DeleteProvider(type = MapperTemplate.class, method = "deleteByIds")
	public void deleteByIds(Map<String,Object> parameter);
	
	@UpdateProvider(type = MapperTemplate.class, method = "update")
	public   <T> int update(T entity);
	
	@UpdateProvider(type = MapperTemplate.class, method = "updateByVersion")
	public    int updateByVersion(Map<String, Object> parameter);
	
	
	
	@UpdateProvider(type = MapperTemplate.class, method = "updateColumns")
	public   int updateColumns(Map<String, Object> parameter);
	
	@UpdateProvider(type = MapperTemplate.class, method = "updateColumnsByVersion")
	public   int updateColumnsByVersion(Map<String, Object> parameter);
	
	@SelectProvider(type = MapperTemplate.class, method = "load")
	public   HashMap<String,Object> load(Map<String, Object> parameter);
 
	public int count(Map<String,Object> parameter);
	

 
}
