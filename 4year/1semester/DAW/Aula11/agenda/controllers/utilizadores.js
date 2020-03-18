var Utilizador = require('../models/utilizadores')

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