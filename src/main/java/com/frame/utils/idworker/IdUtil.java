package com.frame.utils.idworker;

import java.util.UUID;

public class IdUtil {

	/**
	 * 生成32位uuid
	 * @return
	 */
	public static String randomUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

}
