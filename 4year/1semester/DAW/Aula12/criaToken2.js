var jwt = require('jsonwebtoken')
const segredo = "2019-12-10"

/*
audiente - a quem se destina
issuer - quem esta a gera lo
jwtid - identificador do token
subject - assunto associado
noTimestamp - evita que se gere o iat
*/

var token = jwt.sign({sub: 'token gerado na aula de DAW2019',
                        teste: "token de teste"                    
                    }, segredo, {
                        expiresIn: 300, 
                        issuer: "criaToken2.js",
                        audience: "alunos",
                        noTimestamp: true
})

console.log('Token: ' + token)