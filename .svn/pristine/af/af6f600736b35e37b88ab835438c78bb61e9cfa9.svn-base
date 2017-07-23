package com.frame.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.frame.entity.AttachmentEntity;
import com.frame.service.AttachmentService;
import com.frame.utils.annotation.Login;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {
	
	@Autowired
	private AttachmentService attachmentService;
	
	@Login
	@RequestMapping(value="/record/{id}",method=RequestMethod.GET)
	public Map<Integer, List<AttachmentEntity>> findAttachementListByRecordId(@PathVariable String id,Integer fileType){
		return attachmentService.findAttachementListByRecordId(id,fileType);
	}

}
