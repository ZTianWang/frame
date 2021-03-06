define(function(require, exports) {
	exports.init = function() {
		 initPlaceHolder();
		$('#login').on('click', function() {
			KU.send({
				url : 'authorization',
				type : 'POST',
				sText : false,
				eText : {404:'用户名或密码错误'},
				data : {
					account : $('#account').val(),
					password : $('#password').val(),
				},
				success : function(data) {
					$.cookie(KU.cookieRelative.authorization, data.authorization);
					$.cookie(KU.cookieRelative.account, data.account);
					location.href='main.html#url=index';
				}
			});
		});
	}
});