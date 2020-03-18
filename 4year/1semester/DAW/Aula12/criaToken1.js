var jwt = require('jsonwebtoken')
const segredo = "daw2019"
var token = jwt.sign({sub: 'token gerado na aula de DAW2019'}, segredo, {
    expiresIn: 300, 
    issuer: "criaToken1.js",
    audience: "alunos"
})

console.log('Token: ' + token)