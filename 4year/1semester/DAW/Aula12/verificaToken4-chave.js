var jwt = require('jsonwebtoken')
var fs = require('fs')

/*
ssh-keygen -t rsa -b 4096 -m PEM -f jwtRS256.key
openssl rsa -in jwtRS256.key -pubout -outform PEM -out jwtRS256.key.pub
*/

const chavePrivada = fs.readFileSync('jwtRS256.key')
var token = jwt.sign({sub: 'token gerado na aula de DAW2019',
                    }, chavePrivada, {
                        algorithm: 'RS256',
                        expiresIn: 300, 
                        issuer: "verificaToken2.js",
})

console.log("Token: " + token)

const readline = require('readline').createInterface({
    input: process.stdin,
    output: process.stdout
})

readline.question('Introduza o nome do ficheiro da chave publica: ', (s) => {
    var chavePublica = fs.readFileSync(s)
    jwt.verify(token, chavePublica, (err,decoded) => {
        if(err)
            console.log("Erro: " + err)
        else
            console.dir(decoded)
    })
    readline.close()    
})