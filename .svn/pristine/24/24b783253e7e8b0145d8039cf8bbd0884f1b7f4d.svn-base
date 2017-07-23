define(function(require, exports) {
	require('jvalid');// 引入jquery校验组件
	require('js/uploadify');
	/*示范引入指定的js  开始*/
	var accountCommon=require('account/common');
	accountCommon.test1('嘿嘿');
	accountCommon.test2('嘿嘿');
	c(accountCommon.resultGirl);
	/*示范引入指定的js  结束*/
	var rules = {
		account : {
			required : true,
			requiredText : '账号不能为空',
			maxlength : 15,
			maxlengthText : '最长{0}个字符',
		},
		password : {
			required : true,
			maxlength : 12,
			maxlengthText : '最长{0}个字符',
		},
		password_again : {
			equalTo : "#password",
		},
		cellPhone : {
			required : true,
			mobile : true
		},
		email : {
			required : true,
			email : true
		}
	};

	var selectedRoles;/*操作选择的角色*/
	/* 页面初始化方法 */
	exports.init = function() {
		var ue=require('ue');
		var contentEditor=ue.init('contentEditor');
		selectedRoles={};
		/* 初始化加载校验组件及操作 */
		var $accountForm=$('#accountForm');
		$accountForm.validForm(rules, function() {
			/* 如果验证通过，则排除相关input之后，序列化表单内容 */
			var formJson = $accountForm.serializeJson([ 'password_again',  'headAttId','idCardAttId','contentEditorAttId' ]);
			/* 设置上传的头像字段 */
			formJson['headIds'] = $accountForm.find('input[name="headId"]').arrayVal();
			/* 设置上传的身份证字段 */
			formJson['idCardIds'] = $accountForm.find('input[name="idCardId"]').arrayVal();
			formJson['contentAttIds'] = $accountForm.find('input[name="contentEditorAttId"]').arrayVal();
			formJson['addRoles'] = $accountForm.find('input[name="selectedRolesIds"]').arrayVal();
			formJson['content']=contentEditor.getContent();
		KU.send({
				url : 'account',
				type : 'POST',
				data : formJson,
				success : function() {
					//成功则返回
					back();
				}
			});

		});

		/**
		 * 初始化加载上传组件及操作
		 */
		KU.renderFileTpl({
			btnId : 'head',/*指定绑定按钮*/
			fileType : 0,/*头像的类型设置为0*/
			uploadify:{
				auto:true
			}
		});
		KU.renderFileTpl({
			btnId : 'idCard',/*指定绑定按钮*/
			fileType : 1/*身份证的类型设置为1*/
		});

		
		/*弹出窗口选择角色*/
		var selectRolesDialog=require('role/select-roles-dialog');
		$('#selectRolesBtn').on('click', function() {
			selectRolesDialog.init('selectedRolesHtml',selectedRoles,function(roles){
				selectedRoles=roles;
			});
		});
	}
});