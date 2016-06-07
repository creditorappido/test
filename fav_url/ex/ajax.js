define(function(req, exp, mod){
	"use strict";
	var ajax = req("sys.emit");
	ajax.config = req("root.config");
	var emit = ajax.emit;

	mod.exports = {
		getFavList: emit("getFavList")
	};

});
