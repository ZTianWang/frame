package com.frame.mapper;

import java.util.List;
import java.util.Map;


public interface AccountMapper {
	/**
	 * account和role的对应表
	 * @param accountRoles
	 */
	public void createAccountRole(List<Map<String,Object>> accountRoles);

	
	public void deleteAccountRole(List<Map<String,String>> list);
}
