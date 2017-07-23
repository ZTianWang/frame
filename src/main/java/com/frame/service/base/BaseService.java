package com.frame.service.base;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.crazycake.utils.CamelNameUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.frame.entity.base.BaseEntity;
import com.frame.mapper.base.BaseMapper;
import com.frame.utils.HttpCode;
import com.frame.utils.KVMap;
import com.frame.utils.ReflectUtil;
import com.frame.utils.StringUtil;
import com.frame.utils.annotation.Version;
import com.frame.utils.exception.FrameException;
import com.frame.utils.mybatis.MyBatisHelper;
import com.frame.utils.pager.Pager;
import com.frame.utils.pager.SystemContext;

public class BaseService<T extends BaseEntity> {

	Logger  logger=LoggerFactory.getLogger(BaseService.class);
	@Autowired
	private BaseMapper baseMapper;
	private Class<?> clz;
	@Autowired
	private SqlSessionTemplate sqlSession;
	@Autowired
	@Qualifier("redisDBCache") 
	private RedisTemplate<String, T> redisDBCache;
	private String mappingPath = BaseMapper.class.getName();
	private Class<?> getClz() {

		if (clz == null) {
			// 获取泛型的Class对象
			clz = ((Class<?>) (((ParameterizedType) (this.getClass().getGenericSuperclass()))
					.getActualTypeArguments()[0]));
		}
		return clz;
	}
	
	/**
	 * 根据key。设置redis中数据的有效时间
	 * @param key
	 */
	private void expireDataByKey(String key){
		redisDBCache.expire(key, 30, TimeUnit.DAYS);
	}
	/**
	 * 增加
	 * @param entity
	 */
	public void create(T entity) {
		if(baseMapper.create(entity)!=0){
			redisDBCache.opsForHash().putAll(entity.getId(), ReflectUtil.object2Map(entity));
			expireDataByKey(entity.getId());
		}else{
			throw new FrameException("操作失败");
		}
	}

	/**
	 * 更新整个对象，需要先load出对象，修改后再传入
	 * @param entity
	 */
	public void update(T entity) {
		if(getClz().isAnnotationPresent(Version.class)){
			Map<String,Object> obj2map=ReflectUtil.object2Map(entity);
			HashOperations<String, String, Object> opsForHash =redisDBCache.opsForHash();
			String version_key=getClz().getAnnotation(Version.class).value();
//			int vVersion=(int)opsForHash.get(entity.getId(), version_key);
			int vVersion=(int)ReflectUtil.object2MapByProperty(load(entity.getId()), new String[]{version_key}).get(version_key);
			int version=(int) obj2map.get(version_key);//取得传来的version校验
			if(version!=vVersion){//校验是否一致
				throw new FrameException("数据version版本不匹配", HttpCode.VERSION_MATCH);
			}else{
				obj2map.put(version_key, vVersion+1);
				obj2map.put("where_version", vVersion);
				obj2map.put("clz", getClz());
		     if(baseMapper.updateByVersion(obj2map)==0){
			   throw new FrameException("数据更新与其他用户发生冲突", HttpCode.VERSION_MATCH);
		     }else{
		    	 obj2map.remove("where_version");
		    	 obj2map.remove("clz");
		    	 opsForHash.putAll(entity.getId(),obj2map);
		    	 expireDataByKey(entity.getId());
		     }
		 
		 }
		
		}else{
			if(baseMapper.update(entity)!=0){
				redisDBCache.opsForHash().putAll(entity.getId(), ReflectUtil.object2Map(entity));
				expireDataByKey(entity.getId());
			}
//			else{
//				throw new FrameException("操作失败");
//			}
		}
		
	}
	
  
	
	/**
	 * 根据指定的字段，更新数据
	 * @param entity 对象
	 * @param columns 指定的字段
	 */
	public void update(T entity,String [] columns){
		Map<String, Object> obj2map=ReflectUtil.object2MapByProperty(entity, columns);
		obj2map.put("columns", columns);
		obj2map.put("clz", getClz());
		obj2map.put("id", entity.getId());
		if(getClz().isAnnotationPresent(Version.class)){
			HashOperations<String, String, Object> opsForHash =redisDBCache.opsForHash();
			String version_key=getClz().getAnnotation(Version.class).value();
//			int vVersion=(int)opsForHash.get(entity.getId(), version_key);
			int vVersion=(int)ReflectUtil.object2MapByProperty(load(entity.getId()), new String[]{version_key}).get(version_key);
			int version=(int) obj2map.get(version_key);//取得传来的version校验
			if(version!=vVersion){//校验是否一致
				throw new FrameException("数据version版本不匹配", HttpCode.VERSION_MATCH);
			}else{
				obj2map.put(version_key, vVersion+1);
				obj2map.put("where_version", vVersion);
		     if(baseMapper.updateColumnsByVersion(obj2map)==0){
			   throw new FrameException("数据更新与其他用户发生冲突", HttpCode.VERSION_MATCH);
		     }else{
		    	 obj2map.remove("columns");
		    	 obj2map.remove("where_version");
		    	 obj2map.remove("clz");
		    	 opsForHash.putAll(entity.getId(),obj2map);
		    	 expireDataByKey(entity.getId());
		     }
		 
		 }
		}else{
			if(baseMapper.updateColumns(obj2map)!=0){
				obj2map.remove("columns");
				obj2map.remove("clz");
				redisDBCache.opsForHash().putAll(entity.getId(),obj2map);
				expireDataByKey(entity.getId());
			}
//			else{
//				 throw new FrameException("操作失败");
//			}
		}
		
	}

	/**
	 * 根据单个id删除数据
	 * @param id
	 */
	public void delete(String id) {
		if(baseMapper.delete(new KVMap<String, Object>().add("id", id).add("clz", getClz()))!=0){
			redisDBCache.delete(id);
		}
	}
	/**
	 * 根据多个id删除数据
	 * @param ids
	 */
	public void delete(String [] ids){
		if(ids==null||ids.length==0){
			return;
		}
		 baseMapper.deleteByIds(new KVMap<String, Object>().add("ids", ids).add("clz", getClz()));
		 redisDBCache.delete(Arrays.asList(ids));
	}

	/**
	 * 根据id进行load
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T load(String id) {
		 HashOperations<String, String, Object> opsForHash=redisDBCache.opsForHash();
		Map<String, Object> redisMap= opsForHash.entries(id);
		if (redisMap.size()!=0) {
			return (T) ReflectUtil.map2Object(redisMap, getClz());
		} else {
			Map<String, Object> resultMap = baseMapper
					.load(new KVMap<String, Object>().add("id", id).add("clz", getClz()));
			if (resultMap != null) {
				try {
					T t= (T) ReflectUtil.map2ObjectByUnderscore2camel(resultMap, getClz());
					opsForHash.putAll(id, ReflectUtil.map2MapByUnderscore2camel(resultMap));
					return t;
				} catch (Exception  e) {
					throw new FrameException(id+"load错误",e);
				}

			}
		}
		return null;
	}
	
	
	
	public int count(String condition, KVMap<String, Object> parameter){
		return count(condition,parameter,true);
	}
 
    /**
     * 必须指定条件进行count，不要包含order by
     * @param condition where后面的条件对象
     * @param parameter 参数
     * @return 结果
     */
	private int count(String condition, KVMap<String, Object> parameter,boolean camel2underline) {
		StringBuilder sql = new StringBuilder("select count(id) from ");
		sql.append(MyBatisHelper.getTableName(getClz()));
		if (StringUtil.isNotEmpty(condition)) {
			condition=MyBatisHelper.getCondition(condition);
			sql.append(" where ");
			if(camel2underline)
				sql.append(StringUtil.camel2underline(condition));
			else
				sql.append(condition);
		}
		if (parameter == null) {
			parameter = new KVMap<String, Object>();
		}

		return baseMapper.count(parameter.add("sql", sql));
	}
	
	

 

 
	public List<T> findList(String condition,KVMap<String, Object> parameter,String [] columns){
		return findListBySql(condition,parameter,columns);
	}
	public List<T> findList(String condition,KVMap<String, Object> parameter){
		return findListBySql(condition,parameter,null);
	}
	
	
	private List<T> findListBySql(String condition,KVMap<String, Object> parameter,String [] columns){
		StringBuilder sql = new StringBuilder("select ");
		if(columns!=null){
			sql.append("id ");
			for(String column:columns){
					sql.append(',').append(CamelNameUtils.camel2underscore(column));
			}
		}else{
			sql.append(MyBatisHelper.selectColumnList(getClz()));
		}
		sql.append(" from ")  
				.append(MyBatisHelper.getTableName(getClz()));
		if (condition != null) {
			condition=MyBatisHelper.getCondition(condition);
			sql.append(" where ").append(StringUtil.camel2underline(condition));
		}
		if (parameter == null) {
			parameter = new KVMap<String, Object>();
		}
	return findList(parameter.add("sql", sql));
	}
	

 
    /**
     * 指定条件、参数、列名
     * @param condition where后面的条件
     * @param parameter 参数
     * @param columns 列名
     * @return pager对象
     */
	public Pager<T> findPage(String condition, KVMap<String, Object> parameter,String [] columns) {
		
		return findPageBySql(condition, parameter,columns);
	}
	
	/**
	 * 指定条件、参数，查询所有列名
	 * @param condition 条件
	 * @param parameter 参数
	 * @return pager对象
	 */
    public Pager<T> findPage(String condition, KVMap<String, Object> parameter) {
		
		return findPageBySql(condition, parameter,null);
	}
	
	 
	private Pager<T> findPageBySql(String condition, KVMap<String, Object> parameter,String [] columns) {
		Pager<T> pager = new Pager<T>();
		int pageSize=SystemContext.getPageSize();
		pager.setSize(pageSize);
		int offset = SystemContext.getPageOffset();
		pager.setOffset(offset);
		if (StringUtil.isNotEmpty(condition)) {
			condition=StringUtil.camel2underline(MyBatisHelper.getCondition(condition));
			}
		int total = count(condition, parameter,false);
		if (total==0||offset >= total) {//如果查询起始位置大于等于总条数，则直接返回，正常操作下不会发生
			pager.setData(new ArrayList<T>());
			return pager;
		}
		pager.setTotal(total);
			StringBuilder sql = new StringBuilder("select ");
			if(columns!=null){
				sql.append("id ");
				for(String column:columns){
						sql.append(',').append(CamelNameUtils.camel2underscore(column));
				}
			}else{
				sql.append(MyBatisHelper.selectColumnList(getClz()));
			}
			sql.append(" from ")
					.append(MyBatisHelper.getTableName(getClz()));
			if (StringUtil.isNotEmpty(condition)) {
				sql.append(" where ").append(condition);
			}
			if (parameter == null) {
				parameter = new KVMap<String, Object>();
			}
			 String sort=SystemContext.getSort();
			 String order=SystemContext.getOrder();
			 if(sort!=null&&order!=null){
				 String [] sortArray=sort.split(",");
				 String [] orderArray=order.split(",");
				 sql.append(" order by ");
				 for(int i=0;i<sortArray.length;i++){
					 sql.append(CamelNameUtils.camel2underscore(sortArray[i])).append(" ").append(orderArray[i]);
					 if(i<sortArray.length-1)sql.append(",");
				 }
			 }
			
			   sql.append(" limit ").append(offset).append(",").append(pageSize);
	           pager.setData(findList(parameter.add("sql", sql)));
	    return pager;
	}
	
	
 
     
	/**
	 * 根据sql和参数，查询列表
	 * @param parameter  包含sql和参数的map
	 * @return list集合
	 */
	@SuppressWarnings("unchecked")
	private List<T> findList(KVMap<String, Object> parameter){
					final List<T> dbList=new ArrayList<T>();
					sqlSession.select(mappingPath + ".selectList", parameter, new ResultHandler() {
						@Override
						public void handleResult(ResultContext context) {
								Map<String, Object> resultMap = (Map<String, Object>) context.getResultObject();
								T t= (T) ReflectUtil.map2ObjectByUnderscore2camel(resultMap, getClz());
								dbList.add(t);
						}
					});
				 
		 return dbList;
	}
	
	

}
