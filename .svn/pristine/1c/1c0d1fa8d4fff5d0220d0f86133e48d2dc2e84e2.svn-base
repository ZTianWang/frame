package com.frame.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.frame.command.AuthorizationCommand;
import com.frame.dto.AuthorizationDto;
import com.frame.service.AuthorizationService;
import com.frame.utils.Constant;
import com.frame.utils.annotation.Login;
import com.frame.utils.annotation.Open;

@RestController
@RequestMapping("/authorization")
public class AuthorizationController {
	
	@Autowired
	private AuthorizationService   authorizationService;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Open
	@RequestMapping(method=RequestMethod.POST)
	public AuthorizationDto login(@RequestBody AuthorizationCommand cmd,@RequestHeader(Constant.DEVICE_TYPE) int   deviceType) throws NoSuchAlgorithmException{
		return authorizationService.queryLogin(cmd,deviceType);
	}
	
	@Login
	@RequestMapping(value="/roles",method=RequestMethod.GET)
	public Map<String,Object> findMenusByAuthorization(@RequestHeader(Constant.AUTHORIZATION) String authorization){
		AuthorizationDto authorizationDto = (AuthorizationDto) redisTemplate.opsForValue().get(authorization);
		Map<String,Object>  resultMap=authorizationService.findMenuFunctionByAccountId(authorizationDto);
		resultMap.put("roles", authorizationService.findRoleInfoByAccountId(authorizationDto.getAccountId()));
		return resultMap;
	}
	@Login
	@RequestMapping(value="/exit",method=RequestMethod.DELETE)
	public void exitSystem(@RequestHeader(Constant.AUTHORIZATION) String authorization){
		redisTemplate.delete(authorization);
	}

}
