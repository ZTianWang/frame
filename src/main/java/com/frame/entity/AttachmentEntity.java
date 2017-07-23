package com.frame.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.frame.entity.base.BaseEntity;

@Table(name = "attachment")
public class AttachmentEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Column
	private String recordId;// 记录id，文件属于哪条记录，如果为-1则说明是上传了，但未关联记录
	@Column
	private String fileUniqueId;//文件的唯一标示，注意，此处不是文件md5的值，而是在文件服务器上的唯一标识
	@Column
	private String fileName;// 文件名字
	@Column
	private int fileType;// 文件类型，哪里上传的文件，哪里定义
	@Column
	private String fileSuffix;// 文件后缀
	@Column
	private long fileSize;// 文件大小
	@Column
	private int downloads;// 下载次数
	@Column
	private String accountId;// 用户id

	@Override
	public String toString() {
		return "[id="+getId()+",recordId="+recordId+",fileName="+fileName+",fileType="+fileType+",fileSuffix="+fileSuffix+",fileSize="+fileSize+",downloads="+downloads+",accountId="+accountId+",createDate="+getCreateDate()+"]";
	}
	
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public int getDownloads() {
		return downloads;
	}

	public void setDownloads(int downloads) {
		this.downloads = downloads;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getFileUniqueId() {
		return fileUniqueId;
	}

	public void setFileUniqueId(String fileUniqueId) {
		this.fileUniqueId = fileUniqueId;
	}
	
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
	@Override
	public Date getCreateDate() {
		 
		return super.getCreateDate();
	}

}
