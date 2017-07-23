package com.frame.controller;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.frame.command.AccountCommand;
import com.frame.command.AuthorizationCommand;
import com.frame.entity.AccountEntity;
import com.frame.service.AccountService;
import com.frame.utils.KVMap;
import com.frame.utils.SecurityUtil;
import com.frame.utils.StringUtil;
import com.frame.utils.pager.Pager;
import com.frame.utils.pager.SystemContext;

@RestController
@RequestMapping("/account")
public class AccountController {
	@Autowired
	private AccountService accountService;
	

	
	@RequestMapping(method=RequestMethod.PATCH)
	public AccountEntity  patchAccount(@RequestBody AuthorizationCommand accountDti){
		System.out.println(accountDti);
		AccountEntity account=new AccountEntity();
		account.setAccount("patch方法");
		account.setCellPhone("15801187545");
		System.out.println("来到patch方法");
		return account;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public Map<String,String> createAccount(@RequestBody AccountCommand cmd) throws NoSuchAlgorithmException{
		AccountEntity account=new AccountEntity();
		account.setAccount(cmd.getAccount());
		account.setCellPhone(cmd.getCellPhone());
		account.setEmail(cmd.getEmail());
		account.setContent(cmd.getContent());
		account.setPassword(SecurityUtil.md5(SecurityUtil.md5(cmd.getPassword())));
		
		List<String> files=new ArrayList<String>();
		files.addAll(cmd.getHeadIds());
		files.addAll(cmd.getIdCardIds());
		files.addAll(cmd.getContentAttIds());
		//放进service中处理，这样形成事务，在controller中调用各个service的方法时，整体不是事务
		accountService.createAccountInfo(account, files, cmd.getAddRoles());
		return new KVMap<String,String>().add("id", account.getId());
	}
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public Map<String,Object> loadAccountById(@PathVariable String id){
		AccountEntity accountEntity=accountService.load(id);
		//不想返回的字段，设置为null
		accountEntity.setPassword(null);
		 Map<String,Object> result=new HashMap<String,Object>();
		 result.put("account", accountEntity);
		 Map<String,Map<String,String>> rolesMap=new HashMap<String,Map<String,String>>();
		 List<Map<String, String>> list=accountService.findRolesByAccountId(id);
		 for(Map<String, String> role:list){
			 String roleId=role.get("id");
			 rolesMap.put(roleId, new KVMap<String,String>().add("id", roleId).add("name", role.get("name")));
		 }
		 result.put("roles", rolesMap);
		return result;
	}
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public void updateAccountById(@PathVariable String id,@RequestBody AccountCommand cmd) throws NoSuchAlgorithmException{
		AccountEntity accountEntity=accountService.load(id);
		if(StringUtil.isNotEmpty(cmd.getPassword())){
			accountEntity.setPassword(SecurityUtil.md5(SecurityUtil.md5(cmd.getPassword())));
		}
		accountEntity.setCellPhone(cmd.getCellPhone());
		accountEntity.setEmail(cmd.getEmail());
		accountEntity.setContent(cmd.getContent());
		accountService.updateAccountInfo(accountEntity, cmd.getAddRoles(), cmd.getDeleteRoles());
	}
	
	@RequestMapping(value="/page",method=RequestMethod.GET)
	public Pager<AccountEntity>  findAccountPage(String account,String cellPhone,String email,String startDate,String endDate){
		 StringBuilder condition=new StringBuilder();
		 KVMap<String, Object>  parameter=new KVMap<String, Object>();
		 
		 if(StringUtil.isNotEmpty(startDate)){
			 condition.append(" createDate >=#{startDate}");
			 parameter.put("startDate", startDate);
		 }
		 
		 if(StringUtil.isNotEmpty(endDate)){
			 condition.append(" and createDate <=#{endDate}");
			 parameter.put("endDate", endDate);
		 }
		 
		 if(StringUtil.isNotEmpty(account)){
			 condition.append(" and account like #{account}");
			 parameter.put("account", account+"%");
		 }
		 
		 if(StringUtil.isNotEmpty(cellPhone)){
			 condition.append(" and cellPhone like #{cellPhone}");
			 parameter.put("cellPhone", cellPhone+"%");
		 }
		 
		 if(StringUtil.isNotEmpty(email)){
			 condition.append(" and email like #{email}");
			 parameter.put("email", email+"%");
		 }
		 SystemContext.setSort("createDate");
		 SystemContext.setOrder("desc");
 	return accountService.findPage(condition.toString(), parameter, new String[]{"account","cellPhone","email","createDate"});
  }
	
	@RequestMapping(method=RequestMethod.DELETE)
	public AccountEntity  deleteAccount(){
		AccountEntity account=new AccountEntity();
		account.setAccount("delete方法");
		account.setCellPhone("15801187545");
		System.out.println("来到delete方法");
		return account;
	}
	
	 

}
