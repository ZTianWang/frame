package com.frame.entity.base;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.frame.utils.idworker.IdUtil;

public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(updatable=false)
	private String id;
	@Column(updatable=false)
	private Date createDate;

	// 默认返回uuid，无须手动赋值
	public String getId() {
		if(id==null){
			id=IdUtil.randomUUID();
		}
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		if(createDate==null){
			createDate=new Date();
		}
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
