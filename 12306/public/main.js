define(function(req,exp,mod){
	"use strict";
	var app = req("sys.app");
	var config = req("root.config");
	
	config.ajaxSetup();
	app.viewEx = req("root.viewEx");
	
	app.setPath({
		js: mod.resolve("./"),
		tp: mod.resolve("./")	
	});

	app.usePlugin("sys.ui.dialog");
	app.usePlugin("sys.ui.mask");

	app.init("home");

});
