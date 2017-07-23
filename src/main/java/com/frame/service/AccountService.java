package com.frame.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frame.entity.AccountEntity;
import com.frame.entity.AttachmentEntity;
import com.frame.mapper.AccountMapper;
import com.frame.mapper.MenusMapper;
import com.frame.service.base.BaseService;
import com.frame.utils.idworker.IdUtil;

@Service
public class AccountService extends BaseService<AccountEntity> {

	@Autowired
	private AccountMapper accountMapper;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private MenusMapper menusMapper;

	public List<Map<String, String>> findRolesByAccountId(String accountId) {
		return menusMapper.findRoleInfoByAccountId(accountId);
	}

	public void createAccountInfo(AccountEntity account, List<String> files, List<String> rolesId) {
		create(account);// 先存account
		/* 再更新文件 */
		for (String attId : files) {
			AttachmentEntity att = new AttachmentEntity();
			att.setId(attId);
			att.setRecordId(account.getId());
			attachmentService.update(att, new String[] { "recordId" });
		}
		/* 再更新角色 */
		if (!rolesId.isEmpty()) {
			List<Map<String, Object>> accountRoles = new ArrayList<Map<String, Object>>();
			for (String roleId : rolesId) {
				Map<String, Object> accountRole = new HashMap<String, Object>();
				accountRole.put("id", IdUtil.randomUUID());
				accountRole.put("accountId", account.getId());
				accountRole.put("roleId", roleId);
				accountRole.put("createDate", account.getCreateDate());
				accountRoles.add(accountRole);
			}
			accountMapper.createAccountRole(accountRoles);
		}

	}

	public void updateAccountInfo(AccountEntity account, List<String> addRoles, List<String> deleteRoles) {
		update(account);
		//增加
		if (!addRoles.isEmpty()) {
			List<Map<String, Object>> createAccountRoles = new ArrayList<Map<String, Object>>();
			for (String roleId : addRoles) {
				Map<String, Object> accountRole = new HashMap<String, Object>();
				accountRole.put("id", IdUtil.randomUUID());
				accountRole.put("accountId", account.getId());
				accountRole.put("roleId", roleId);
				accountRole.put("createDate", account.getCreateDate());
				createAccountRoles.add(accountRole);
			}
			accountMapper.createAccountRole(createAccountRoles);
		}
		//删除
		if (!deleteRoles.isEmpty()) {
			List<Map<String, String>> deleteAccountRoles = new ArrayList<Map<String, String>>();
			for (String roleId : deleteRoles) {
				Map<String, String> accountRole = new HashMap<String, String>();
				accountRole.put("accountId", account.getId());
				accountRole.put("roleId", roleId);
				deleteAccountRoles.add(accountRole);
			}
			accountMapper.deleteAccountRole(deleteAccountRoles);
		}
	}

}
