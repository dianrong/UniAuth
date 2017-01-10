$(function() {
	//初始化函数
	var init = function() {
		//刷新滚动条
		login_scroll_img_show();
		
		//添加验证码替换功能
		$('#cas_login_captcha_change_img').click(function(){
			refresh_verfypic($('#cas_login_captcha_change_img'));
		});
		$('#cas_login_captcha_change_a').click(function(){
			refresh_verfypic($('#cas_login_captcha_change_img'));
		});
	}
	
	//首页滚动图片自适应
	var loginscrollset = function(){
		//正常的长宽比例
		var nomarlWidth = 500, normalHeight= 320, normalBoxHeight = 350;
		//获取box的实际高度
		var realBoxHeight = $('#content').height();
		var realBoxWidth = $('#content').width();
		var realLoginBoxWidth = $('#login').width();
		
		//算出最大的宽度
		var divMaxWidth = realBoxWidth - realLoginBoxWidth - 20;
		
		var realDivHeight = realBoxHeight / normalBoxHeight * 320;
		var realDivWidth = realDivHeight / normalHeight * 500;
		
		if(realDivWidth > divMaxWidth){
			//按最大的宽度重新计算
			realDivHeight = realDivHeight * (divMaxWidth / realDivWidth);
			realDivWidth = divMaxWidth;
		} 
		setLoginScrollDivWidthHeight(realDivWidth, realDivHeight);
	}
	
	var setLoginScrollDivWidthHeight = function(realDivWidth, realDivHeight){
		//开始设置长宽数据  包括div和backgroundColor的属性
		$('#cas-ad-div').height(realDivHeight).width(realDivWidth);
		
		$('#banner_tabs .slides img').css("width",realDivWidth+"px").css("height", realDivHeight+"px");
	}
	
	//获取登陆首页的滚动图片并显示
	var login_scroll_img_show = function(){
		var getUrl = context_path + "/uniauth/cascfg/imges/login/scrolling";
		$.ajax({  
            type : "GET", 
            url : getUrl,
            success : function(data) {
            	//刷新滚动条的数据
            	$('#cas-ad-div').html(data);
            	//页面自适应
            	loginscrollset();
            	//显示
            	$('#cas-ad-div').removeClass("hiddenbtn");
            },
            error: function(jqXHR, textStatus, errorMsg){
            	logOperation.error(errorMsg);
            },
        });  
	}
	//执行init操作
	init();
});