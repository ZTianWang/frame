package com.frame.utils.ftp;

import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FTPClient工厂类，通过FTPClient工厂提供FTPClient实例的创建和销毁
 * 
 */
public class FTPClientFactory {
	private static Logger logger = LoggerFactory.getLogger(FTPClientFactory.class);
	private FTPClientConfigure config;

	// 给工厂传入一个参数对象，方便配置FTPClient的相关参数
	public FTPClientFactory(FTPClientConfigure config) {
		this.config = config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.pool.PoolableObjectFactory#makeObject()
	 */
	public FTPClient makeObject() throws Exception {
		FTPClient ftpClient = new FTPClient();
		ftpClient.setConnectTimeout(config.getClientTimeout());
		try {
			ftpClient.connect(config.getHost(), config.getPort());
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				logger.warn("FTPServer refused connection");
				return null;
			}
			boolean result = ftpClient.login(config.getUsername(), config.getPassword());
			if (!result) {
				throw new RuntimeException(
						"ftpClient登陆失败! userName:" + config.getUsername() + " ; password:" + config.getPassword());
			}
			ftpClient.setFileType(config.getTransferFileType());
			ftpClient.setBufferSize(1024*500);
			ftpClient.setControlEncoding(config.getEncoding());
			if (config.getPassiveMode().equals("true")) {
				ftpClient.enterLocalPassiveMode();
			}
		} catch (IOException e) {
			logger.error("创建ftp连接对象失败:",e);
			throw e;
		} 
		return ftpClient;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.pool.PoolableObjectFactory#destroyObject(java.lang.
	 * Object)
	 */
	public void destroyObject(FTPClient ftpClient) throws Exception {
		try {
			if (ftpClient != null && ftpClient.isConnected()) {
				ftpClient.logout();
			}
		} catch (IOException io) {
			logger.error("销毁ftp连接对象失败:",io);
			 throw io;
		} finally {
			// 注意,一定要在finally代码中断开连接，否则会导致占用ftp连接情况
			try {
				ftpClient.disconnect();
			} catch (IOException io) {
				logger.error("disconnect ftp连接对象失败:",io);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.pool.PoolableObjectFactory#validateObject(java.lang.
	 * Object)
	 */
	public boolean validateObject(FTPClient ftpClient) {
		try {
			return ftpClient.sendNoOp();
		} catch (IOException e) {
			throw new RuntimeException("Failed to validate client: " + e, e);
		}
	}

	public void activateObject(FTPClient ftpClient) throws Exception {
	}

	public void passivateObject(FTPClient ftpClient) throws Exception {

	}

	public FTPClientConfigure getConfig() {
		return config;
	}

	public void setConfig(FTPClientConfigure config) {
		this.config = config;
	}
}