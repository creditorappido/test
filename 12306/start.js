~function(req, exp){
	"use strict";
	//var server = req("../../github/nova/server/server");
	var server = req("./nova/server/core");
	server.addStatic({
		path: "/",
		dir: "public/"
	});
	//remoteUri = "https://kyfw.12306.cn/otn/leftTicket/queryT?";
	server.addRemote({
		path: "/service/",
		file: require("./service"),
		host: "kyfw.12306.cn",
		port: 443,
        getResult: function(rs){
            return {
                success: rs.httpstatus==200,
                data: rs.data,
                message: rs.messages.join(",")
            }
        }
	});
    server.port = 8888;
	server.start();
}(require, exports);
