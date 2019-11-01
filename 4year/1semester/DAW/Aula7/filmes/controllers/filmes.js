var Filme = require('../models/filme')

const Filmes = module.exports

Filmes.listar = () =>{
    return Filme
        .find()
        .sort({title: 1})
        .exec()
}

Filmes.consultar = fid =>{
    return Filme
        .findOne({_id: fid})
        .exec()
}

Filmes.contar = () =>{
    return Filme
        .countDocuments()
        .exec()
}

Filmes.projetar = campos =>{
    return Filme
        .findOne({}, campos)
        .exec()
}

Filmes.agregar = fid =>{
    return Filme
        .findOne({_id: fid})
        .exec()
}