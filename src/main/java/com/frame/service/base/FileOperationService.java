package com.frame.service.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frame.utils.ftp.FTPClientPool;

@Service
public class FileOperationService {
	@Autowired
	private FTPClientPool ftpClientPool;

	public   boolean uploadFile(String dir,String path, InputStream is) {
		FTPClient client = null;
		try {
			client = ftpClientPool.borrowObject();
			String uploadPath=ftpClientPool.getFactory().getConfig().getRootDir()+dir;
			client.makeDirectory(uploadPath);
			return client.storeFile(uploadPath + path, is);
		} catch (Exception e) {
			return false;
		} finally {
			try {
				ftpClientPool.returnObject(client);
			} catch (Exception e1) {
				// 不作处理
			}
			 if (is != null) {
				try {
					is.close();
				} catch (IOException e) {

				}
			} 
		}

	}
	
	public  boolean  deleteFile(String path){
		FTPClient client = null;
		try {
			client = ftpClientPool.borrowObject();
			return client.deleteFile(ftpClientPool.getFactory().getConfig().getRootDir() + path);
		} catch (Exception e) {
			return false;
		} finally {
			try {
				ftpClientPool.returnObject(client);
			} catch (Exception e1) {
				// 不作处理
				e1.printStackTrace();
			}
			}
	}
	

	public   boolean downloadFile(String path, OutputStream os) {
		FTPClient client = null;
		try {
			client = ftpClientPool.borrowObject();
			return client.retrieveFile(ftpClientPool.getFactory().getConfig().getRootDir() + path, os);
		} catch (Exception e) {
			return false;
		} finally {
			try {
				ftpClientPool.returnObject(client);
			} catch (Exception e1) {
				// 不作处理
			}
			 if (os != null) {
				try {
					os.close();
				} catch (IOException e) {

				}
			}
		}

	}
}
