package com.frame.entity;



import javax.persistence.Column;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.frame.entity.base.BaseEntity;

@Table(name = "account")
@JsonInclude(Include.NON_EMPTY)
public class AccountEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
    @Column(updatable=false)
	private String account;
    @Column
	private String password;
    @Column
	private String cellPhone;
    @Column
	private String email;
    @Column
    private String content;
 
 

	@Override
	public String toString() {
		return "[id=" + getId() + ",account=" + account + ",password=" + password + ",cellPhone=" + cellPhone
				+ ",email=" + email+",createDate=" + getCreateDate() + ",content="+content+"]";
	}

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
