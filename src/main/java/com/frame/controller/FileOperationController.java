package com.frame.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frame.dto.AttachmentDto;
import com.frame.dto.AuthorizationDto;
import com.frame.entity.AttachmentEntity;
import com.frame.service.AttachmentService;
import com.frame.service.AuthorizationService;
import com.frame.service.base.FileOperationService;
import com.frame.utils.HttpCode;
import com.frame.utils.StringUtil;
import com.frame.utils.annotation.Login;
import com.frame.utils.annotation.Open;
import com.frame.utils.exception.FrameException;
import com.frame.utils.idworker.IdUtil;

@Controller
@RequestMapping("/upload")
public class FileOperationController {
	
	@Autowired
	private FileOperationService fileOperationService;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private AttachmentService  attachmentService;
	@Autowired
	private AuthorizationService authorizationService;
	
	@Open
	@RequestMapping(value="/uploadFile",method=RequestMethod.POST)
	public void upload(MultipartFile attachment,String authorization,int deviceType,HttpServletResponse response,Integer fileType,String recordId) throws IOException{
		if(StringUtil.isEmpty(authorization)){
			throw new FrameException("登录失效，请登录",HttpCode.NOT_LOGGED_IN);
		}
		AuthorizationDto authorizationDto=(AuthorizationDto) redisTemplate.opsForValue().get(authorization);
		if(authorizationDto==null){
			throw new FrameException("登录失效，请登录",HttpCode.NOT_LOGGED_IN);
		}
		authorizationService.expire(authorization, deviceType);
		long fileSize=attachment.getSize();
		if(attachment.getSize()==0){
			throw new FrameException("无效的空文件",HttpCode.INVALID_OPERATION);
		}
		String originalFilename=attachment.getOriginalFilename();
		int lastPointIndex=originalFilename.lastIndexOf(".");
		if(lastPointIndex==-1){
			throw new FrameException("文件没有后缀",HttpCode.INVALID_OPERATION);
		}
		String name=originalFilename.substring(0,lastPointIndex);
		String suffix= originalFilename.substring(lastPointIndex);
		String fileUniqueId=IdUtil.randomUUID();
		String fileName=fileUniqueId+suffix;
		Date date=new Date();
		String datePath=new SimpleDateFormat("yyyyMMdd").format(date)+"/";
		boolean uploadResult=fileOperationService.uploadFile(datePath,fileName, attachment.getInputStream());
	   if(!uploadResult){
		   throw new FrameException("服务器处理失败");
	   }
 
	   AttachmentEntity att =new AttachmentEntity();
	   att.setFileUniqueId(datePath+fileUniqueId);
	   att.setFileName(name);
	   att.setFileSize(fileSize);
	   att.setFileSuffix(suffix);
	   att.setAccountId(authorizationDto.getAccountId());
	   att.setCreateDate(date);
	   att.setFileType(fileType==null?0:fileType.intValue());
	   if(StringUtil.isNotEmpty(recordId)){
		   att.setRecordId(recordId);  
	   }
	   attachmentService.create(att);
	   
	   
	   AttachmentDto attachmentDto=new AttachmentDto();
	   attachmentDto.setId(att.getId());
	   attachmentDto.setName(datePath+fileName);
	    PrintWriter writer= response.getWriter();
	    ObjectMapper  objectMapper=new ObjectMapper();
	    writer.write(objectMapper.writeValueAsString(attachmentDto));
	    writer.flush();
	    writer.close();
	}
	
	@Login
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteAttachment(@PathVariable String id,@RequestBody Map<String,String> cmdMap){
		fileOperationService.deleteFile(cmdMap.get("fileName"));
		attachmentService.delete(id);//直接删除文件
	}
	
	

}
