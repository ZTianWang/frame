package com.frame.command;

import java.util.List;

/**
 * account页面传参
 * @author Lxp
 *
 */
public class AccountCommand {
	private String account;
	private String password;
	private String cellPhone;
	private String email;
	private String content;
	private List<String> headIds;
	private List<String> idCardIds;
	private List<String> contentAttIds;
	private List<String> addRoles;
	private List<String> deleteRoles;
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	 

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	 

	 

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	 
 
 

	public List<String> getAddRoles() {
		return addRoles;
	}

	public void setAddRoles(List<String> addRoles) {
		this.addRoles = addRoles;
	}

	public List<String> getDeleteRoles() {
		return deleteRoles;
	}

	public void setDeleteRoles(List<String> deleteRoles) {
		this.deleteRoles = deleteRoles;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getHeadIds() {
		return headIds;
	}

	public void setHeadIds(List<String> headIds) {
		this.headIds = headIds;
	}

	public List<String> getIdCardIds() {
		return idCardIds;
	}

	public void setIdCardIds(List<String> idCardIds) {
		this.idCardIds = idCardIds;
	}

	public List<String> getContentAttIds() {
		return contentAttIds;
	}

	public void setContentAttIds(List<String> contentAttIds) {
		this.contentAttIds = contentAttIds;
	}

	 

	 

 

}
