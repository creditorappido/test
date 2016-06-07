//var nova = require("nova");
var nova = require("/Users/likaituan/github/nova/server/core.js");

nova.addStatic({
	path: "/seekjs/",
	//dir: "/usr/local/lib/node_modules/seekjs/"
	dir: "/Users/likaituan/github/seekjs/"
});

nova.addStatic({
	path: "/",
	dir: "./"	
});

nova.addRemote({
	path: "/service/",
	file: require("./service")
});

nova.port = 9999;
nova.start();
