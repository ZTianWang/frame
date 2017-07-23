define(function(require, exports) {
	/* 得到本菜单对应的按钮级function */
	var roleFunctions =  KU.getFunction('role/manager');
	/* 根据权限，得到用户拥有的列表中的按钮，返回值是用户实际拥有的，参数是列表中的全部按钮 */
	var listFunction = KU.getFunction(roleFunctions, [ 'editMenus']);
    /*pageNo分页时，参数传递，修改pager公共js*/
	var queryData;
	var query = function() {/* 定义查询方法 */
		queryData=KU.getHist('role/page');
		if(queryData){/*如果有历史缓存条件，则回显*/
		       $('#roleName').val(queryData.roleName);
		}else{
			/*如果没有历史缓存条件*/
			queryData={roleName:$('#roleName').val()};
		} 
		    KU.send({
					url : 'role/page',
					data :queryData,
					success : function(data) {
						data.functionBtns = listFunction;// 指定列表模板中需要显示的按钮
						$('#resultTable').html(template('roleListTpl', data));/* 填充页面数据 */
						/* 真实数据产生之后，针对按钮绑定事件 */
						bindFunction();
						/* 进行分页操作 */
						data.query = query;
						$('#pager').pager(data);
					}
				});
		  
	}
	/* 判断列表按钮 */
	function bindFunction() {
		/* 绑定所有funBtn为roleFunBtn的按钮click事件 */
		/* 高效绑定事件 */
		/* 选择id为resultTable的元素的tr中的最后一个td中的class为roleFunBtn的元素，绑定click事件 */
		 $('#resultTable tr td:last-child .roleFunBtn').on('click', function() {
			/*根据按钮上的key的值，获得隐藏域对象*/
			var $this = $(this);
			var flag = $this.attr('flag');
			if(flag=='editMenus'){
				/*编辑之前，记录查询条件的历史记录，然后才跳转*/
				KU.setHist('role/page',queryData);
				location.hash='url=role/edit-menus&roleId='+$this.attr('id')+'&roleName='+$this.attr('roleName');
			} 
			
		}); 
	}
	exports.init = function() {
		query();/* 默认直接查询 */
		$('#searchBtn').on('click', function() {
			query();
		});
		
	};

});