package com.frame.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.frame.command.RoleCommand;
import com.frame.dto.AuthorizationDto;
import com.frame.entity.RoleEntity;
import com.frame.service.RoleService;
import com.frame.utils.Constant;
import com.frame.utils.KVMap;
import com.frame.utils.annotation.Login;
import com.frame.utils.pager.Pager;

@RestController
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	
	@RequestMapping(method=RequestMethod.POST)
	public Map<String,String> createRole(@RequestBody RoleCommand cmd)  {
		RoleEntity role = new RoleEntity();
		role.setName(cmd.getName());
		
		roleService.create(role);
		return new KVMap<String,String>().add("id", role.getId());
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public RoleEntity loadRoleById(@PathVariable String id){
		return roleService.load(id);
	}
	 
	/**
	 * 根据角色id，查出来用户拥有的菜单和角色菜单的交集
	 * @return
	 */
	@RequestMapping(value="/menusfunctions/{roleId}",method=RequestMethod.GET)
	public List<Map<String, Object>> findMenusFunctions(@RequestHeader(Constant.AUTHORIZATION) String authorization,@PathVariable String roleId){
		AuthorizationDto authorizationDto = (AuthorizationDto) redisTemplate.opsForValue().get(authorization);
		return roleService.findMenuFunctionByAccountId(authorizationDto.getAccountId(),roleId);
	}
	/**
	 * 修改编辑角色拥有的function
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/functions/{roleId}",method=RequestMethod.PUT)
    public void updateFunctionsOfRole(@PathVariable String roleId,@RequestBody Map<String,Object> parameters){
	   roleService.updateRoleFunction((List<String>)parameters.get("addFunctions"), (List<String>)parameters.get("deleteFunctions"), roleId);
    }
	
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public void updateRoleById(@PathVariable String id,@RequestBody RoleCommand cmd) {
		RoleEntity role = roleService.load(id);
		role.setName(cmd.getName());
		roleService.update(role);
	}
	
	@RequestMapping(value="/page",method=RequestMethod.GET)
	public Pager<RoleEntity> findRolePage(@RequestHeader(Constant.AUTHORIZATION) String authorization,String roleName){
		AuthorizationDto authorizationDto = (AuthorizationDto) redisTemplate.opsForValue().get(authorization);
		return  roleService.findRolePage(authorizationDto.getAccountId(), roleName);
	}
	/**
	 * 特殊规则uri命名，只要登录，都允许访问，和具体拥有的权限无关
	 * @param authorization  token
	 * @param roleName  角色名称
	 * @return 分页数据
	 */
	@Login
	@RequestMapping(value="/select/page",method=RequestMethod.GET)
	public Pager<RoleEntity> findSessionRolePage(@RequestHeader(Constant.AUTHORIZATION) String authorization,String roleName){
		AuthorizationDto authorizationDto = (AuthorizationDto) redisTemplate.opsForValue().get(authorization);
		return  roleService.findRolePage(authorizationDto.getAccountId(), roleName);
	}
	
	
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public void deleteRoleById(@PathVariable String id)  {
		RoleEntity role = roleService.load(id);
		role.setName("delete");
		roleService.update(role);
	}
	
}
