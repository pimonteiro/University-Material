var http = require('http')
var url = require('url')
var pug = require('pug')
var fs = require('fs')
var jsonfile = require('jsonfile')

var {parse} = require('querystring')

var myBD = "alunos.json"

var myServer = http.createServer((req,res) => {
    var purl = url.parse(req.url, true)
    var query = purl.query

    console.log(req.method + ' ' + purl.pathname)

    if(req.method == 'GET'){
        if((purl.pathname == '/') || (purl.pathname == '/gestaoAlunos')){
            res.writeHead(200,{'Content-Type': 'text/html; charset=utf-8'})
            res.write(pug.renderFile('index.pug'))
            res.end()
        }
        else if(purl.pathname == '/w3.css'){
            res.writeHead(200,{'Content-Type': 'text/css'})
            fs.readFile('w3.css', (err,data)=>{
                if(!err){
                    res.write(data)
                }
                else{
                    res.write("<p>Erro: " + err + "</p>")
                }
                res.end()
            })
        }
        else if(purl.pathname == '/favicon.ico'){
            res.writeHead(200,{'Content-Type': 'image/x-icon'})
            fs.readFile('favicon.ico', (err,data)=>{
                if(!err){
                    res.write(data)
                }
                else{
                    res.write("<p>Erro: " + err + "</p>")
                }
                res.end()
            })
        }
        else if(purl.pathname == '/listar'){
            res.writeHead(200,{'Content-Type': 'text/html; charset=utf-8'})
            jsonfile.readFile(myBD,(err,data)=>{
                if(!err){
                    res.write(pug.renderFile('lista-alunos.pug',{lista: data}))
                }
                else{
                    res.write(pug.renderFile('erro.pug', {e: "Erro na leitura da BD..." + err}))
                }
                res.end()
            })
        }
        else if(purl.pathname == '/registar'){
            res.writeHead(200,{'Content-Type': 'text/html; charset=utf-8'})
            res.write(pug.renderFile('form-aluno.pug'))
            res.end()
        }
    }
    else if(req.method == 'POST'){
        if(purl.pathname == '/aluno'){
            recuperaInfo(req, resultado =>{
                jsonfile.readFile(myBD, (err,alunos)=>{
                    if(!err){
                        alunos.push(resultado)
                        jsonfile.writeFile(myBD,alunos, err=>{
                            if(err)
                                console.log(err)
                            else
                                console.log("Registo gravado com sucesso...")
                        })
                    }
                    else{
                        res.write(pug.renderFile('erro.pug', {e: "Erro na leitura da BD..." + err}))
                    }
                })
            })
        }
    }
    else{
        res.writeHead(200,{'Content-Type': 'text/html; charset=utf-8'})
        console.log("ERROR: " + req.method + " não  suportado...")
        res.write(pug.renderFile('erro.pug',
                        {e: "ERROR: " + req.method + " não  suportado..."})
        )
        res.end()
    }
})

function recuperaInfo(request, callback){
    if(request.headers['content-type'] == 'application/x-www-form-urlencoded'){
        let body = ''
        request.on('data', bloco =>{
            body += bloco.toString()
        })
        request.on('end', ()=>{
            callback(parse(body))
        })
    }
    else{
        console.log("Wrong enconding!")
        console.log(request.headers)
    }
}

console.log("Starting server...")
myServer.listen(5005, ()=>{
    console.log("Server started! Use port 5005.")
})
