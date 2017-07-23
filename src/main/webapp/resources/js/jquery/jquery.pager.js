define(function(require, exports, module) {
  /*$('head').append('<link rel="stylesheet" href="'+host+'resources/js/pager/pager.css" />');*/
  var containerId='';
  $.fn.pager = function(data) {
	    containerId=(data.containerId?data.containerId:'resultTable');
	    var $containerId=$('#'+containerId);
	    $containerId.show();
	  if(data.total==0){
	    /*如果传递了结果容器ID，则动态使用传递的id，否则以resultTable为主*/
		  $containerId.hide();
	    $(this).empty().append('<div style="text-align:center;">暂无相关数据</div>');
	    return ;
	  }
	  var options={
	          pagenumber: data.offset/data.size+1,
	          buttonClickCallback:function(clickedPage){
	        		  ky.utils.page.pageNo=clickedPage;data.query(clickedPage);
	        	 },
	          pagecount:data.pagesCount,
	          totalcount:data.total
	  };
		return this.each(function() {   
		  $(this).empty().append('<div style="float: right;"  id="pagerDiv'+containerId+'" ></div>');
      $('#pagerDiv'+containerId).append(renderpager(parseInt(options.pagenumber), parseInt(options.pagecount), options.buttonClickCallback,parseInt(options.totalcount)));
			/*$('.paging a').mouseover(function() { document.body.style.cursor = "pointer"; }).mouseout(function() { document.body.style.cursor = "auto";});  */
		});
	};    // render and return the pager with the supplied options
	
	function renderpager(pagenumber, pagecount, buttonClickCallback,totalcount) {        // setup $pager to hold render     
		var $pager = $('<ul class="paging"></ul>');        // add in the previous and next buttons 
		//$pager.append(renderButton('首页', pagenumber, pagecount, buttonClickCallback)).append(renderButton('上一页', pagenumber, pagecount, buttonClickCallback));        // pager currently only handles 10 viewable pages ( could be easily parameterized, maybe in next version ) so handle edge cases     
		$pager.append(renderButton('上一页', pagenumber, pagecount, buttonClickCallback));
		var startPoint = 1; 
		var endPoint = 7;
		var thpoint="<span>...</span>";
		if (pagenumber > 5&&endPoint<pagecount) {
			startPoint = pagenumber - 2;
			endPoint = pagenumber + 2; 
			for (var page = 1; page <= 2; page++) {
				var currentButton = $('<a class="page-number">' + (page) + '</a>');
			     currentButton.click(function() {
					buttonClickCallback(this.firstChild.data);
				 });
				currentButton.appendTo($pager);
			}  
			$pager.append('<span>...</span>');
		} 
 
		if (endPoint > pagecount) { 
			startPoint = pagecount - 4;
			endPoint = pagecount;
		}
		if(pagecount-1<=6){
			startPoint=1;
		}
		endPoint==pagecount&&(thpoint='');
        
		if (startPoint < 1) {
			startPoint = 1;
		}        // loop thru visible pages and render buttons
		for (var page = startPoint; page <= endPoint; page++) {
			var currentButton = $('<a class="page-number">' + (page) + '</a>');
			page == pagenumber ? currentButton.addClass('pgCurrent') : currentButton.click(function() {
				buttonClickCallback(this.firstChild.data);
			});
			currentButton.appendTo($pager);
		}        // render in the next and last buttons before returning the whole rendered control back.
		$pager.append(thpoint).append(renderButton('下一页', pagenumber, pagecount, buttonClickCallback))/*.append(renderButton('末页', pagenumber, pagecount, buttonClickCallback))*/;
		$pager.append("<a class='thpoint' style='margin-top:3px;'>共"+pagecount+"页</a>");
		if(pagecount>7){
			var strgoto = $("<a class='thpoint'>到第<input class='input-text' type='text' value='"+pagenumber+"'maxlength='6' id='gotoval"+containerId+"' style='width:35px; height:20px;line-height:20px;margin:-3px 5px 0px;text-align:center '/>页</a>");
			$pager.append(strgoto);
			$pager.append(changepage('go',pagecount,buttonClickCallback,pagenumber));
		}
		return $pager;
}    // renders and returns a 'specialized' button, ie 'next', 'previous' etc. rather than a page number button

function changepage(buttonLabel,pagecount,buttonClickCallback,pagenumber){
	var $btngoto = $('<a class="pgNext">'+ buttonLabel+'</a>');
	$btngoto.click(function() {
		var gotoval = $('#gotoval'+containerId).val();
		var patrn = /^[1-9]\d*$/;
		if (!patrn.exec(gotoval)){
			newsHint("页码无效！");
			return false;
		}
		var intval = parseInt(gotoval);
		if(intval > pagecount){
			newsHint("超出最大页数"+pagecount);
			$("#gotoval"+containerId).val(pagenumber);
			return ;
		}
		buttonClickCallback(intval);
	});
	return $btngoto;
}

function renderButton(buttonLabel, pagenumber, pagecount, buttonClickCallback) {     
	var $Button = $('<a class="pgNext">' + buttonLabel + '</a>');   
	var destPage = 1;        // work out destination page for required button type   
	switch (buttonLabel) {
		case "首页":
			destPage = 1;
			break;
		case "上一页":   
			destPage = pagenumber - 1;
			break;
		case "下一页":
			destPage = pagenumber + 1;          
		break;
		case "末页":
			destPage = pagecount;        
		break;     
	}        // disable and 'grey' out buttons if not needed.       
	if (buttonLabel == "首页" || buttonLabel == "上一页") {     
		pagenumber <= 1 ? $Button.addClass('pgEmpty') : $Button.click(function() { buttonClickCallback(destPage); });     
	}       
	else {     
		pagenumber >= pagecount ? $Button.addClass('pgEmpty') : $Button.click(function() { buttonClickCallback(destPage); }); 
	}     
	return $Button;  
 }    // pager defaults. hardly worth bothering with in this case but used as placeholder for expansion in the next version

 $.fn.pager.defaults = {   
	 pagenumber: 1,     
	 pagecount: 1};
});