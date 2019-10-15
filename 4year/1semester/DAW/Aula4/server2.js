var http = require('http')
var meta = require('./my-mod')

var servidor = http.createServer(function (req, res) {
    res.writeHead(200, {'Content-Type': 'text/html; charset=utf-8'})
    res.write("Criada com o node.js por " + 
    meta.myName() + " em " + meta.myDateTime())
    res.end()
})

servidor.listen(7777)
console.log('Servidor à escuta na porta 7777...')
