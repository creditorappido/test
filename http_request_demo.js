/**
 * Created by likaituan on 16/1/15.
 */



var req = require("http").request({
        host:"115.28.25.240",
        port:8010,
        path:"/java/get_user_by_cellphone",
        method:"post",
        header:{
            "Content-Type":"application/x-www-form-urlencoded"
        }
    },function(res){
        res.on("data",function(a){
            console.log(a);
        });
    });

req.write('{"cellphone":13601187438}');
req.end();