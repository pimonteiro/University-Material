var http = require('http')
var url = require('url')

http.createServer(function (req, res) {
res.writeHead(200, {'Content-Type': 'text/html; charset=utf-8'})
    console.log(req.method + ' ' + req.url)
    var q = url.parse(req.url, true).query
    res.write('True: <pre>' + JSON.stringify(q) + '</pre>')
    var qtext = url.parse(req.url, false).query
    res.end('False: <pre>' + JSON.stringify(qtext) + '</pre>')
}).listen(7777)

console.log('Servidor à escuta na porta 7777...')