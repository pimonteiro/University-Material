var http = require('http');

http.createServer(function(req,res){
    res.writeHead(200,{'Content-Type': 'text/plain'});
    console.log(req);
    res.write(req.url);
    res.end('\nOlá turma de 2019!');
}).listen(7777);
console.log('Servidor à escuta na porta 7777...');