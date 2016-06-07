define(function(req, exp){
	"use strict";
	var app = req("sys.app");
    var $ = req("sys.query");

	var tip = function(message){
		app.viewEx.alert(message);
	};

    exp.baseUri = "/service/";

	//AJAX设置
	exp.ajaxSetup = function() {
		$.ajaxSetup({
			global: true,
			cache: false,
			dataType: "JSON",
			timeout: 30000,
			beforeSend: function (xhr) {
				app.plugin.mask.showMask({
					loading: true,
					bg: "#fff",
					opacity: 0
				});
				xhr.setRequestHeader("sessionId", sessionStorage.sessionId);
				xhr.setRequestHeader("userId", sessionStorage.userId);
				xhr.setRequestHeader("cellphone", sessionStorage.cellphone);
				xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			},
			complete: function (rs) {
				app.plugin.mask.hide();
			},
			error: function (rs) {
				if (rs.status == 403) {
					tip("登录超时,请重新登录!");
					app.go(app.loginpage);
				} else if (rs.status == 404) {
					console.warn("找不到nodejs服务或nodejs未启动");
					tip("系统出错,请联系客服,致电400-991-0099");
				} else if (rs.statusText == "timeout") {
					tip("系统超时, 请刷新重试, 如多次刷新无效,请联系客服,致电400-991-0099");
				} else if (rs.statusText == "error") {
					tip("对不起, 您没有权限访问,请联系客服,致电400-991-0099");
					//tip("登录超时,请重新登录!");
					//app.go(app.inipage);
				} else {
					tip("系统故障, 请刷新重试, 如多次刷新无效,请联系客服,致电400-991-0099");
				}
			}
		});
	};

    //AJAX数据处理
	exp.ajaxRes = function(callback, params){
		return function(rs){
			if(rs.code==-1) {
				if(sessionStorage.deviceType=="android"){
					return window.session.sessionInvalidAction();
				}else if(sessionStorage.deviceType=="ios"){
					return app.go("cellphone/again");
				}
				//sessionStorage.login_or_register_go_page = location.href.split("#")[1];
				if(sessionStorage.sessionId){
                    app.viewEx.go_login("登录超时,请重新登录!");
                }else{
                    if(sessionStorage.invitedoutCode){
                        sessionStorage.login_or_register_go_page = location.href.split("#")[1];
                    }
					app.viewEx.go_login(/*"亲, 您未登录或未注册"*/);
                }
			}
			else if(rs.code==500){
				tip("系统故障, 请刷新重试, 如多次刷新无效,请联系客服,致电400-991-0099!");
			}
			else{
				if(params && params.data && rs.data){
					params.data = rs.data;
				}
				callback({success:rs.code==0, code:rs.code, message:rs.message, data:rs.data});
			}
		}
	};

});