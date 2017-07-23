package com.frame.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.frame.dto.MenuFunctionDto;

public interface MenusMapper {

	/**
	 * 根据账号id，通过角色等表关联，查询出用户具有的方法与菜单信息
	 * 
	 * @return
	 */
	
	public List<MenuFunctionDto> findMenuFunctionByAccountId(@Param("accountId") String accountId);
	
	/**
	 * 根据账号id，找到所属角色
	 * @param accountId
	 */
	public List<Map<String,String>> findRoleInfoByAccountId(@Param("accountId") String accountId);
	
	/**
	 * 编辑角色拥有的菜单和方法时使用
	 * @param accountId
	 * @return
	 */
	
	public List<MenuFunctionDto> findFewAboutMenuFunctionByAccountId(@Param("accountId") String accountId);
	
	/*批量插入角色对应的function，故意不适用缓存*/
	public void createRoleFunction(List<Map<String,Object>> list);
	/*批量删除角色对应的function，故意不适用缓存*/
	public void deleteRoleFunction(List<Map<String,String>> list);
}
