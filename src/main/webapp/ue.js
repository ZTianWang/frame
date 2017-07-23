define(function(require, exports) {
	var ownAtt;
	/**
	 * {
	 * attList:
	 * recordId:
	 * }
	 */
	exports.initAtt=function(ownAttObj){
		ownAtt=ownAttObj;
	};
	exports.init = function(editorId) {
		ownAtt=null;
		var ue =UE.getEditor(editorId);
		registerAttBtn();
		ue.ready(function(){/*富文本编辑器加载完毕事件*/
			ue.focus(true);//默认设置焦点
		});
		return ue;
	};
function registerAttBtn(){
		UE.registerUI('附件', function(editor, uiName) {
		    //注册按钮执行时的command命令，使用命令默认就会带有回退操作
		    editor.registerCommand(uiName, {
		        execCommand: function() {
		        	attDialog(editor);
		        }
		    });
		    //创建一个button
		    var btn = new UE.ui.Button({
		        //按钮的名字
		        name: uiName,
		        //提示
		        title: uiName,
		        //添加额外样式，指定icon图标，这里默认使用一个重复的icon
		        cssRules: 'background-position: -620px -40px;',
		        //点击时执行的命令
		        onclick: function() {
		            //这里可以不用执行命令,做你自己的操作也可
		            editor.execCommand(uiName);
		        }
		    });
		    //当点到编辑内容上时，按钮要做的状态反射
		    editor.addListener('selectionchange', function() {
		        var state = editor.queryCommandState(uiName);
		        if (state == -1) {
		            btn.setDisabled(true);
		            btn.setChecked(false);
		        } else {
		            btn.setDisabled(false);
		            btn.setChecked(state);
		        }
		    });
		    //因为你是添加button,所以需要返回这个button
		    return btn;
		});
	};
	exports.registerAttBtn=registerAttBtn;
	/*附件弹出框处理*/
	
	function attDialog(editor){
		var editorId=editor.key;
		function setAttToHtml(htmlContent){
			$('#'+editorId).after('<div style="display:none;" id="'+editorId+'attFiles">'+htmlContent+'</div>');
		}
		
		 
         var      attList=new DLG({
			id:editorId+'AttDialog',
			title:'附件',
			width:450,
			height:350,
			content:'<div id="'+editorId+'AttsList"><input   type="button"  id="'+editorId+'Att" name="'+editorId+'Att"  class="btn" value="选择"/> </div>',
			ok: function () {
				var str='';
				var $queue=$('#'+editorId+'Att-queue');
				var $selectedAtt=$queue.children(".uploadify-queue-item-border");
				$selectedAtt.each(function(){
					var $item=$(this);
   		        var  $cancelFunction=$item.find('.cancelFunction:first-child');
   		        var  fileUniqueId=$cancelFunction.attr('fileUniqueId');
   		        var sName=$item.find('.fileName:first-child').attr('fileName');
   		        var suffix=sName.toLowerCase();
			       suffix=suffix.substring(suffix.lastIndexOf('.')+1);
			       if(suffix=='jpg'||suffix=='gif'||suffix=='png'||suffix=='jpeg'||suffix=='bmp'){
			    	   str+='<p><img src="'+KU.upload.host+fileUniqueId+'"   title="'+sName+'" alt="'+sName+'"/></p>';
			       }else{
			    	    suffix=='pptx'&&(suffix='ppt')||suffix=='xlsx'&&(suffix='xls')||suffix=='docx'&&(suffix='doc');
			    	   str+='<p style="line-height: 16px;"><img src="'+host+'resources/ue/dialogs/attachment/fileTypeImages/icon_'+suffix+'.gif"/><a style="font-size:12px; color:#0066cc;" href="'+KU.upload.host+fileUniqueId+'" title="'+sName+'">'+sName+'</a></p>';
			       }
			 });
				  str!=''&&editor.execCommand('insertHtml',str); 
				  setAttToHtml($queue.html());
			},
			cancel: function () { 
				  setAttToHtml($('#'+editorId+'Att-queue').html());	
			},
			onshow:function(){
				var uploadifyConfig={
						 auto:true,
						 fileTypeDesc    : '请选择文件',        
						 fileTypeExts    : '*.gif;*.jpg;*.jpeg;*.png;*.bmp;*.rar;*.zip;*.ppt;*.pptx;*.xls;*.xlsx;*.docx;*.doc;',  
					 };
				var $beforeAtt=$('#'+editorId+"attFiles");
				var  beforeAttHtml=$beforeAtt.html();
				 
				if(!ownAtt){
				 KU.renderFileTpl({
					 btnId : editorId+'Att',
					 fileType : -1,
					 maxNum:10,
					 uploadify:uploadifyConfig
				 });
				}else{
				if(beforeAttHtml){
					ownAtt.attList=[];
				}
					KU.renderFileTpl({
						attList : ownAtt.attList,
						btnId : editorId+'Att',
						recordId : ownAtt.recordId,
						fileType : -1,
						maxNum:10,
						uploadify:uploadifyConfig
					});
					
				}
				if(beforeAttHtml){
					$('#'+editorId+'Att-queue').html(beforeAttHtml);
					$beforeAtt.remove();
				}
				 $('#'+editorId+'AttsList').on('click', '.uploadify-queue-item', function(event) { //点击三级菜单
					 var $item=$(this);
					 $item.hasClass('uploadify-queue-item-border')?$item.removeClass('uploadify-queue-item-border'):$item.addClass('uploadify-queue-item-border');
				 });
				 
			 }
		 
		}).showModal();
	}
	
});