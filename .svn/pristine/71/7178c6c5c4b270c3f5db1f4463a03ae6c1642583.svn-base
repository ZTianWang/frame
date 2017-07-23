package com.frame.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.frame.command.AuthorizationCommand;
import com.frame.dto.AuthorizationDto;
import com.frame.dto.MenuFunctionDto;
import com.frame.entity.AccountEntity;
import com.frame.mapper.MenusMapper;
import com.frame.utils.HttpCode;
import com.frame.utils.KVMap;
import com.frame.utils.SecurityUtil;
import com.frame.utils.StringUtil;
import com.frame.utils.enumtype.LoginSourceEnum;
import com.frame.utils.exception.FrameException;
import com.frame.utils.idworker.IdUtil;

@Service
public class AuthorizationService {

	@Autowired
	private AccountService accountService;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private MenusMapper menusMapper;

	public void expire(String authorization, int deviceType) {
		if (deviceType == LoginSourceEnum.PC.code()) {
			redisTemplate.expire(authorization, 60, TimeUnit.MINUTES);
		}
		// 其余类型依次判断，当然，后续可以抽取到配置文件中定义
	}

	public AuthorizationDto findAuthorizationDtoByAuthorization(String authorization) {
		AuthorizationDto authorizationDto = (AuthorizationDto) redisTemplate.opsForValue().get(authorization);
		return authorizationDto;
	}

	/**
	 * 根据条件，判断是否存在用户
	 * 
	 * @param authorizationCommand
	 * @throws NoSuchAlgorithmException 
	 */
	public AuthorizationDto queryLogin(AuthorizationCommand cmd, int deviceType) throws NoSuchAlgorithmException {
		KVMap<String, Object> parameter = new KVMap<String, Object>();
		AuthorizationDto authorizationDto = new AuthorizationDto();
		    // 根据输入的内容，判断是   用户名|手机号|邮箱  校验用户名和密码，注意此处密码是客户端已经两次md5加密
		     String condition="account";
		     if(StringUtil.isCellPhone(cmd.getAccount())){
		    	 condition="cellPhone";
		     }else if(StringUtil.isEmail(cmd.getAccount())){
		    	 condition="email";
		     }
			parameter.add("param", cmd.getAccount()).add("password", SecurityUtil.md5(SecurityUtil.md5(cmd.getPassword())));
			List<AccountEntity> accountList = accountService.findList(condition+"=#{param} and password=#{password}", parameter, new String[] { "account" });
			//如果第一次查询不到，且是手机号或者邮箱的规则，则有可能用户账号本来就是如此，并非手机、邮箱字段有值
			if (accountList.isEmpty()&&(condition.equals("cellPhone")||condition.equals("email"))) {
				accountList = accountService.findList("account=#{param} and password=#{password}", parameter, new String[] { "account" });
			}
			
			if (accountList.isEmpty()) {
				throw new FrameException("用户名或密码错误", HttpCode.NOT_FOUND);
			}
			AccountEntity account = accountList.get(0);
			authorizationDto.setAccount(account.getAccount());
			authorizationDto.setAccountId(account.getId());

		 
		String token =IdUtil.randomUUID();
		authorizationDto.setAuthorization(token);
		// 如果是pc网页登录
		if (deviceType == LoginSourceEnum.PC.code()) {
			redisTemplate.opsForValue().set(token, authorizationDto, 60, TimeUnit.MINUTES);
		} 

		authorizationDto.setAccountId(null);// 缓存之后，不输出给调用者
		return authorizationDto;
	}

 
	

	private Map<String, List<Map<String, Object>>> findFunctionsInfoByAuthorization(AuthorizationDto authorizationDto, List<MenuFunctionDto> menuFunctionList ) {
		Map<String, List<Map<String, Object>>> resultMap = new HashMap<String, List<Map<String, Object>>>();
 
		List<Map<String, Object>> tempList = null;
		Map<String, Object> tempMap = null;
		String url = null;
		Set<String> hashSet=new HashSet<String>();
		for (MenuFunctionDto functionsDto : menuFunctionList) {
			url = functionsDto.getFunctionUrl();
			hashSet.add(url+functionsDto.getType());//提取url和type，以便aop过滤权限使用
			if (resultMap.containsKey(functionsDto.getMenuUrl())) {
				tempList = resultMap.get(functionsDto.getMenuUrl());
			} else {
				tempList = new ArrayList<Map<String, Object>>();
				resultMap.put(functionsDto.getMenuUrl(), tempList);
			}
			tempMap = new HashMap<String, Object>();
			tempMap.put("name", functionsDto.getFunctionName());
			tempMap.put("flag", functionsDto.getFlag());
			tempMap.put("sortNum", functionsDto.getFunctionSortNum());
			tempList.add(tempMap);
		}
		/*加入拥有的function，然后放入authorizationDto，二者共生死，之后放回缓存，*/
		authorizationDto.setFunctions(hashSet);
		redisTemplate.opsForValue().set(authorizationDto.getAuthorization(), authorizationDto, 60, TimeUnit.MINUTES);
		for(List<Map<String, Object>> functionsList:resultMap.values()){
			 Collections.sort(functionsList,sortMenuComparator);
		}
		return resultMap;
	}
    
 	public List<Map<String,String>>  findRoleInfoByAccountId(String accountId){
 		return menusMapper.findRoleInfoByAccountId(accountId);
	} 
 	 
 	@SuppressWarnings("unchecked")
	public  Map<String,Object> findMenuFunctionByAccountId(AuthorizationDto authorizationDto){
 		Map<String,Object> jsonMap=new HashMap<String,Object>();
 		List<MenuFunctionDto> list=menusMapper.findMenuFunctionByAccountId(authorizationDto.getAccountId());
 		jsonMap.put("functions", findFunctionsInfoByAuthorization(authorizationDto, list));
 		
 		 Set<String> histMenuId=new HashSet<String>();//记录循环过的id
     	 Map<String,Map<String,Object>> resultMap=new HashMap<String,Map<String,Object>>();
     	 Map<String,Integer> fatherIndexMap=new HashMap<String,Integer>();
           for(MenuFunctionDto dto:list){
         	 if(!histMenuId.contains(dto.getMenuId())){//如果该id没有处理过
     		   if(dto.getGrandpaId()==null){//不是module的情况，没有爷爷id^_^
     			  
     			   if(!resultMap.containsKey(dto.getFatherId())){
     				   Map<String,Object>  fatherMap=new HashMap<String,Object>();
     				   fatherMap.put("menuId", dto.getFatherId());
     				   fatherMap.put("menuName", dto.getFatherName());
     				   fatherMap.put("icon", dto.getFatherIcon());
     				   fatherMap.put("sortNum",dto.getFatherSortNum());
     				   List<Map<String,Object>>   fatherChildren=new ArrayList<Map<String,Object>>();
     				   Map<String,Object> menuMap=new HashMap<String,Object>();
     				   menuMap.put("menuId", dto.getMenuId());
     				   menuMap.put("menuName", dto.getMenuName());
     				   menuMap.put("menuUrl", dto.getMenuUrl());
     				   menuMap.put("sortNum", dto.getMenuSortNum());
     				   fatherChildren.add(menuMap);
     				   fatherMap.put("children", fatherChildren);
     				   resultMap.put(dto.getFatherId(), fatherMap);
     			   }else{
     				   List<Map<String,Object>>   fatherChildren=(List<Map<String, Object>>) resultMap.get(dto.getFatherId()).get("children");
     				   Map<String,Object> menuMap=new HashMap<String,Object>();
     				   menuMap.put("menuId", dto.getMenuId());
     				   menuMap.put("menuName", dto.getMenuName());
     				   menuMap.put("menuUrl", dto.getMenuUrl());
     				   menuMap.put("sortNum", dto.getMenuSortNum());
     				   fatherChildren.add(menuMap);
     			   }
     		   
     		   }else{//如果是module的情况
     			   if(!resultMap.containsKey(dto.getGrandpaId())){
     				   Map<String,Object>  grandpaMap=new HashMap<String,Object>();
     				   grandpaMap.put("menuId", dto.getGrandpaId());
     				   grandpaMap.put("menuName", dto.getGrandpaName());
     				   grandpaMap.put("icon", dto.getGrandpaIcon());
     				   grandpaMap.put("sortNum", dto.getGrandpaSortNum());
     				   Map<String,Object>  fatherMap=new HashMap<String,Object>();
     				   fatherMap.put("menuId", dto.getFatherId());
     				   fatherMap.put("menuName", dto.getFatherName());
     				   fatherMap.put("sortNum",dto.getFatherSortNum());
     				   Map<String,Object> menuMap=new HashMap<String,Object>();
     				   menuMap.put("menuId", dto.getMenuId());
     				   menuMap.put("menuName", dto.getMenuName());
     				   menuMap.put("menuUrl", dto.getMenuUrl());
     				   menuMap.put("sortNum", dto.getMenuSortNum());
     				   List<Map<String,Object>>    fatherChildren=new ArrayList<Map<String,Object>>();
     				   fatherChildren.add(menuMap);
     				   fatherMap.put("children", fatherChildren);
     				   List<Map<String,Object>>    grandpaChildren=new ArrayList<Map<String,Object>>();
     				   grandpaChildren.add(fatherMap);
     				   grandpaMap.put("children", grandpaChildren);
     				   
     				   fatherIndexMap.put(dto.getFatherId(), grandpaChildren.size()-1);
     				   resultMap.put(dto.getGrandpaId(), grandpaMap);
     			   }else{
     				   List<Map<String,Object>>    grandpaChildren=(List<Map<String, Object>>) resultMap.get(dto.getGrandpaId()).get("children");
     				   if(!fatherIndexMap.containsKey(dto.getFatherId())){
     					   Map<String,Object>  menuMap=new HashMap<String,Object>();
     					   menuMap.put("menuId", dto.getMenuId());
     					   menuMap.put("menuName", dto.getMenuName());
     					   menuMap.put("menuUrl", dto.getMenuUrl());
     					   menuMap.put("sortNum", dto.getMenuSortNum());
     					   Map<String,Object> fatherMap=new HashMap<String,Object>();
     					   fatherMap.put("menuId",dto.getFatherId());
     					   fatherMap.put("menuName", dto.getFatherName());
     					   fatherMap.put("sortNum",dto.getFatherSortNum());
     					   List<Map<String,Object>>    fatherChildren=new ArrayList<Map<String,Object>>();
     					   fatherChildren.add(menuMap);
     					   fatherMap.put("children", fatherChildren);
     					   grandpaChildren.add(fatherMap);
     					   fatherIndexMap.put(dto.getFatherId(), grandpaChildren.size()-1);
     				   }else{
     					   int fatherIndex=fatherIndexMap.get(dto.getFatherId());
     					   Map<String,Object> fatherMap=grandpaChildren.get(fatherIndex);
     					   List<Map<String,Object>>  fatherChildren=(List<Map<String, Object>>) fatherMap.get("children");
     					   Map<String,Object> menuMap=new HashMap<String,Object>();
     					   menuMap.put("menuId", dto.getMenuId());
     					   menuMap.put("menuName", dto.getMenuName());
     					   menuMap.put("menuUrl", dto.getMenuUrl());
     					   menuMap.put("sortNum", dto.getMenuSortNum());
     					   fatherChildren.add(menuMap);
     				   }
     			   }
     		   }
         		  
             }
     	 histMenuId.add(dto.getMenuId());//循环过之后，把id记录一下
     	 }
     	 
           List<Map<String, Object>> rsList= new ArrayList<Map<String, Object>>(resultMap.values());
          
           Collections.sort(rsList,sortMenuComparator);//排序最外层
           //排序第二层
          for(Map<String,Object> map:rsList){
         	 List<Map<String,Object>> secondList=(List<Map<String,Object>>)map.get("children");
         	 Collections.sort(secondList,sortMenuComparator);
         	 //如果有第三层，则排序第三层
         	 for(Map<String,Object> secondLv:secondList){
         		 List<Map<String,Object>> thirdLV=(List<Map<String,Object>>)secondLv.get("children");
         		 if(thirdLV!=null){
         			 Collections.sort(thirdLV,sortMenuComparator);
         		 }
         	 }
          }
          jsonMap.put("menus",rsList);
 		return jsonMap;
 	}

    private static Comparator<Map<String,Object>> sortMenuComparator=new Comparator<Map<String,Object>>() {
		@Override
		public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		 
			return Integer.parseInt(o1.get("sortNum").toString())-Integer.parseInt(o2.get("sortNum").toString());
		}
	};
 
}
