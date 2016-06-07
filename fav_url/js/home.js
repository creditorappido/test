define(function(req, exp){
	"use strict";
	var ajax = req("ajax");

	exp.list = {
		data:[]
	};

	exp.onInit = function(done){
		ajax.getFavList(exp.list, done);
	};

});
