package com.frame.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.frame.entity.AttachmentEntity;
import com.frame.service.base.BaseService;
import com.frame.utils.KVMap;

/**
 * 上传文件业务类
 */
@Service
public class AttachmentService extends BaseService<AttachmentEntity> {

	public Map<Integer, List<AttachmentEntity>> findAttachementListByRecordId(String recordId, Integer fileType) {
		String condition = "recordId=#{recordId}";
		KVMap<String, Object> parameters = new KVMap<String, Object>().add("recordId", recordId);
		if (fileType != null) {
			condition += " and fileType=#{fileType}";
			parameters.put("fileType", fileType);
		}
		List<AttachmentEntity> lists = findList(condition, parameters);
		Map<Integer, List<AttachmentEntity>> resultMap = new HashMap<Integer, List<AttachmentEntity>>();
		List<AttachmentEntity> tempList = null;
		for (AttachmentEntity attachment : lists) {
			Integer type = attachment.getFileType();
			if (!resultMap.containsKey(type)) {
				tempList = new ArrayList<AttachmentEntity>();
				tempList.add(attachment);
				resultMap.put(type, tempList);
			} else {
				resultMap.get(type).add(attachment);
			}
		}

		return resultMap;
	}

}
