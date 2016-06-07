seekjs.config({
	ns: {
		ex: seekjs.resolve("./ex/")
	},
	alias:{
		ajax: seekjs.resolve("./ex/ajax")
	}	
});

define(function(req, exp){
	"use strict";
	var app = req("sys.app");
	
	app.setPath({
		js: seekjs.resolve("./js/"),
		tp: seekjs.resolve("./templates/")
	});

	app.init("home");
	
});
