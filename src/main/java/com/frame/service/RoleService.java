package com.frame.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.frame.dto.MenuFunctionDto;
import com.frame.entity.RoleEntity;
import com.frame.mapper.MenusMapper;
import com.frame.mapper.RoleMapper;
import com.frame.service.base.BaseService;
import com.frame.utils.StringUtil;
import com.frame.utils.idworker.IdUtil;
import com.frame.utils.pager.Pager;
import com.frame.utils.pager.SystemContext;

@Service
public class RoleService extends BaseService<RoleEntity>{
	
	@Autowired
	private MenusMapper menusMapper;
	@Autowired
	private RoleMapper  roleMapper;
	
	public Pager<RoleEntity> findRolePage(String accountId,String roleName){
		Pager<RoleEntity> pager = new Pager<RoleEntity>();
		int pageSize=SystemContext.getPageSize();
		pager.setSize(pageSize);
		int offset = SystemContext.getPageOffset();
		pager.setOffset(offset);
		
		Map<String,Object> parameters=new HashMap<>();
		parameters.put("accountId", accountId);
		if(StringUtil.isNotEmpty(roleName)){
			parameters.put("roleName",roleName+'%');//只匹配开头
		}
		pager.setTotal(roleMapper.countRolePage(parameters));//统计多少条
		parameters.put("offset", offset);
		parameters.put("pageSize", pageSize);
		
		pager.setData(roleMapper.findRolePage(parameters));
		return  pager;
	}
 	
	
	public  void updateRoleFunction(List<String> addFunctions,List<String> deleteFunctions,String roleId){
		/*先处理增加*/
		if(addFunctions!=null&&addFunctions.size()!=0){
		    Date createDate=new Date();
		    List<Map<String,Object>>  addList=new ArrayList<Map<String,Object>>();
		    for(String addFun:addFunctions){
		    	Map<String,Object> roleFunctionMap=new HashMap<String,Object>();
		    	roleFunctionMap.put("id", IdUtil.randomUUID());
		    	roleFunctionMap.put("createDate", createDate);
		    	roleFunctionMap.put("roleId", roleId);
		    	roleFunctionMap.put("functionId", addFun);
		    	addList.add(roleFunctionMap);
		    }
		    if(addList.size()!=0){
		    	menusMapper.createRoleFunction(addList);
		    }
		}
	 
		if(deleteFunctions!=null&&deleteFunctions.size()!=0){
		    List<Map<String,String>>  deleteList=new ArrayList<Map<String,String>>();
		    for(String deleteFun:deleteFunctions){
		    	Map<String,String> roleFunctionMap=new HashMap<String,String>();
		    	roleFunctionMap.put("roleId", roleId);
		    	roleFunctionMap.put("functionId", deleteFun);
		    	deleteList.add(roleFunctionMap);
		    }
		    if(deleteList.size()!=0){
		    	menusMapper.deleteRoleFunction(deleteList);
		    }
		}
	}
	
	/**
	 * 
	 * @param accountId  账号主键, 查询出来用户拥有的菜单，其中等于当前编辑角色的id的，则返回结果中增加一个checked
	 * @param roleId  当前编辑的角色id
	 * @return
	 * @throws JsonProcessingException 
	 */
	@SuppressWarnings("unchecked")
	public  List<Map<String,Object>>  findMenuFunctionByAccountId(String accountId,String roleId){
		List<MenuFunctionDto>  list=menusMapper.findFewAboutMenuFunctionByAccountId(accountId);
 		 Set<String> histMenuId=new HashSet<String>();//记录循环过的id
     	 Map<String,Map<String,Object>> resultMap=new HashMap<String,Map<String,Object>>();
     	 Map<String,Integer> fatherIndexMap=new HashMap<String,Integer>();
     	 
     	 Map<String, List<Map<String,Object>>> menuFunctionsMap=new  HashMap<String, List<Map<String,Object>>>(); 
         Map<String,Map<String,Object>>  functionsMap=new HashMap<String,Map<String,Object>>();  
     	 for(MenuFunctionDto dto:list){
         	 if(!histMenuId.contains(dto.getMenuId())){//如果该id没有处理过
         		List<Map<String,Object>> functionList=new ArrayList<Map<String,Object>>();
        		Map<String,Object> functionMap=new HashMap<String,Object>();
        		functionMap.put("functionName", dto.getFunctionName());
        		functionMap.put("functionId", dto.getFunctionId());
        		functionMap.put("sortNum", dto.getFunctionSortNum());
        		functionsMap.put(dto.getFunctionId(), functionMap);
        		functionList.add(functionMap);
        		menuFunctionsMap.put(dto.getFunctionId(), null);
        		menuFunctionsMap.put(dto.getMenuId(), functionList);
     		   if(dto.getGrandpaId()==null){//不是module的情况，没有爷爷id^_^
     			   if(!resultMap.containsKey(dto.getFatherId())){
     				   Map<String,Object>  fatherMap=new HashMap<String,Object>();
     				   fatherMap.put("menuId", dto.getFatherId());
     				   fatherMap.put("menuName", dto.getFatherName());
     				   fatherMap.put("sortNum",dto.getFatherSortNum());
     				   List<Map<String,Object>>   fatherChildren=new ArrayList<Map<String,Object>>();
     				   Map<String,Object> menuMap=new HashMap<String,Object>();
     				   menuMap.put("menuId", dto.getMenuId());
     				   menuMap.put("menuName", dto.getMenuName());
     				   menuMap.put("sortNum", dto.getMenuSortNum());
     				   menuMap.put("functions", functionList);
     				   fatherChildren.add(menuMap);
     				   fatherMap.put("children", fatherChildren);
     				   resultMap.put(dto.getFatherId(), fatherMap);
     			   }else{
     				   List<Map<String,Object>>   fatherChildren=(List<Map<String, Object>>) resultMap.get(dto.getFatherId()).get("children");
     				   Map<String,Object> menuMap=new HashMap<String,Object>();
     				   menuMap.put("menuId", dto.getMenuId());
     				   menuMap.put("menuName", dto.getMenuName());
     				   menuMap.put("sortNum", dto.getMenuSortNum());
     				   menuMap.put("functions", functionList);
     				   fatherChildren.add(menuMap);
     			   }
     		   
     		   }else{//如果是module的情况
     			   if(!resultMap.containsKey(dto.getGrandpaId())){
     				   Map<String,Object>  grandpaMap=new HashMap<String,Object>();
     				   grandpaMap.put("menuId", dto.getGrandpaId());
     				   grandpaMap.put("menuName", dto.getGrandpaName());
     				   grandpaMap.put("sortNum", dto.getGrandpaSortNum());
     				   Map<String,Object>  fatherMap=new HashMap<String,Object>();
     				   fatherMap.put("menuId", dto.getFatherId());
     				   fatherMap.put("menuName", dto.getFatherName());
     				   fatherMap.put("sortNum",dto.getFatherSortNum());
     				   Map<String,Object> menuMap=new HashMap<String,Object>();
     				   menuMap.put("menuId", dto.getMenuId());
     				   menuMap.put("menuName", dto.getMenuName());
     				   menuMap.put("sortNum", dto.getMenuSortNum());
     				   menuMap.put("functions", functionList);
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
     					   menuMap.put("sortNum", dto.getMenuSortNum());
     					  menuMap.put("functions", functionList);
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
     					   menuMap.put("sortNum", dto.getMenuSortNum());
     					   menuMap.put("functions", functionList);
     					   fatherChildren.add(menuMap);
     				   }
     			   }
     		   }
         		  
             }else{//如果是处理过的，则抽取function
            	 if(!menuFunctionsMap.containsKey(dto.getFunctionId())){
            		Map<String,Object> functionMap=new HashMap<String,Object>();
              		functionMap.put("functionName", dto.getFunctionName());
              		functionMap.put("functionId", dto.getFunctionId());
              		functionMap.put("sortNum", dto.getFunctionSortNum());
              		functionsMap.put(dto.getFunctionId(), functionMap);
              		menuFunctionsMap.put(dto.getFunctionId(), null);
                 	menuFunctionsMap.get(dto.getMenuId()).add(functionMap);
            	 }
            
             }
         	 if(roleId.equals(dto.getRoleId())){
         		 if(functionsMap.get(dto.getFunctionId())!=null){
         			 functionsMap.get(dto.getFunctionId()).put("checked", true);
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
         		 List<Map<String,Object>> functions=(List<Map<String,Object>>)secondLv.get("functions");
         		  if(functions!=null){
         			 Collections.sort(functions,sortMenuComparator);
         		  }
         		  List<Map<String,Object>> thirdList=(List<Map<String,Object>>)secondLv.get("children");
         		 if(thirdList!=null){
         			 Collections.sort(thirdList,sortMenuComparator);
         			 for(Map<String,Object> thirdLvObj:thirdList){
         				   functions=(List<Map<String,Object>>)thirdLvObj.get("functions");
                		  if(functions!=null){
                			 Collections.sort(functions,sortMenuComparator);
                		  }
         			 }
         		 }
         	 }
          }
		return rsList;
	}
	   private static Comparator<Map<String,Object>> sortMenuComparator=new Comparator<Map<String,Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
			 
				return Integer.parseInt(o1.get("sortNum").toString())-Integer.parseInt(o2.get("sortNum").toString());
			}
		};
	 
}
