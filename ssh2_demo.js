var Client = require('ssh2').Client;

var conn = new Client();
conn.on('ready', function() {
		  console.log('Client :: ready');
		    conn.sftp(function(err, sftp) {
				    if (err) throw err;
					    sftp.readdir('test2', function(err, list) {
							      if (err) throw err;
								        console.dir(list);
										      conn.end();
											      });
						  });
			}).connect({
				  host: '115.28.25.240',
				    port: 22,
					  username: 'root',
					    password: 'YWNiMDJ1NjBjds1'
						});
