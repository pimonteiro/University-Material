var jwt = require('jsonwebtoken')
const segredo = "2019-12-10"

var token = jwt.sign({sub: 'token gerado na aula de DAW2019',
                        teste: "token de teste"                    
                    }, segredo, {
                        expiresIn: 300, 
                        issuer: "verificaToken2.js",
                        audience: "alunos",
                        noTimestamp: true
})

console.log("Token: " + token)

const readline = require('readline').createInterface({
    input: process.stdin,
    output: process.stdout
})

readline.question('Introduza o segredo: ', (s) => {
    jwt.verify(token, segredo, (err,decoded) => {
        if(err)
            console.log("Erro: " + err)
        else
            console.dir(decoded)
    })
    readline.close()    
})