var Utilizador = require('../models/utilizadores')

module.exports.adicionar = user => {
    var novo = new Utilizador(user)
    novo.setPassword(user.password)
    novo.save()
    return novo.toAuthJSON()
}

module.exports.listar = () => {
    return Utilizador
        .find()
        .exec()
}

module.exports.consultar = email => {
    return Utilizador
        .findOne({email: email})
        .exec()
}