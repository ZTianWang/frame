package com.frame.mapper;

import java.util.List;
import java.util.Map;

import com.frame.entity.RoleEntity;

public interface RoleMapper {
    /**
     * 分页
     * @param parameters
     * @return
     */
	public List<RoleEntity> findRolePage(Map<String, Object> parameters);

	 /**
	  * 统计符合条件有多少条
	  * @param parameters
	  * @return
	  */
	public Integer countRolePage(Map<String, Object> parameters);
}
