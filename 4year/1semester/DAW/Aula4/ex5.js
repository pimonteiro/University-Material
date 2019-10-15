var http = require('http')
var url = require('url')

http.createServer(function (req, res) {
res.writeHead(200, {'Content-Type': 'text/html'})
    var q = url.parse(req.url, true).query
    console.log(q);
    var r = parseInt( q.a, 10) + parseInt( q.b, 10)
    var txt = q.a + " + " + q.b + " = " + r
    res.end(txt)
}).listen(7777);
console.log("Starting server on port 7777...")