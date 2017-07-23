define(function(require, exports) {
	var selectedRoles;
	var query = function() {/* 定义查询方法 */
		    KU.send({
					url : 'role/select/page',
					data :{
						roleName:$('#selectRolesDialogRoleName').val(),
						pageSize:5
					},
					success : function(data) {
						data.initChecked=function(roleId){
							if(selectedRoles[roleId]){
								return  'checked=true';
							}
						}
						$('#selectRolesDialogResultTable').html(template('selectRolesDialogListTpl',data));
						bindFunction();
						/* 进行分页操作 */
						data.query = query;
						data.containerId='selectRolesDialogResultTable';
						$('#selectRolesDialogPager').pager(data);
					}
				});
		  
	};
	
	function bindFunction() {
		 $('#selectRolesDialogResultTable tr td:last-child :checkbox').on('click', function() {
			 var $this=$(this);
			 var roleId=$this.attr('selectRoleId')
			 if($this.prop('checked')){
				 selectedRoles[roleId]={id:roleId,name:$this.attr('selectRoleName')}; 
			 }else{
				 delete selectedRoles[roleId];
			 }
		}); 
	}
	var tpl='{{each $data as value i}} [{{$data[i].name}}]&nbsp;'+ 
		'<input type="hidden"  value="{{$data[i].id}}" name="selectedRolesIds"/> {{/each}}';
     function compileByTpl(id,roles){
    	 $('#'+id).html(template.compile(tpl)(roles));
     }
     
     exports.compile=compileByTpl;
	exports.init = function(id,hadSelectedRoles,okFunction) {
		selectedRoles=hadSelectedRoles;
		/* 把已经拥有的角色进行初始化 */
		var   selectRolesDialog=new DLG({
			id:'selectRolesDialog',
			title:'请选择角色',
			width:450,
			height:350,
			ok: function () {
				/* 回调传递进来的方法，参数给调用者 */
				compileByTpl(id,selectedRoles);
				okFunction(selectedRoles);
			},
			cancel: function () {},
			onshow:function(){
				$.ajax({
					 url:'/role/select-roles-dialog.html',
					 success:function(content){
						   selectRolesDialog.content(content);
						   initPlaceHolder();
						   query(); 
						   $('#selectRolesDialogSearchBtn').on('click', function() {
							 query();
						   });
					} 
				});
			}
		}).showModal();
	};
});