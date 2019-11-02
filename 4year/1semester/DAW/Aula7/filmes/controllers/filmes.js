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

Filmes.remover = fid => {
    return Filme
        .findByIdAndRemove({_id: fid})
        .exec()
}

Filmes.adicionar = campos => {
    var new_filme = new Filme({ 
        title: campos['title'],
        year: campos['year'],
        cast: campos['cast'],
        genres: campos['genres']})
    return Filme
        .create(new_filme)
}

Filmes.atualizar = (fid, campos) =>{
    return Filme
        .findOneAndUpdate({_id: fid}, {
            $set: {
                title: campos['title'],
                year: campos['year'],
                cast: campos['cast'],
                genres: campos['genres']       
            }
        })
        .exec()
}