var Aluno = require('../models/alunos');

module.exports.listar = () => {
    return Aluno
        .find()
        .exec()
}

module.exports.consultar = id => {
    return Aluno
        .findOne({_id: id})
        .exec()
}

module.exports.contar = () => {
    return Aluno
        .countDocuments()
        .exec()
}

module.exports.inserir = aluno => {
    var novo = new Aluno(aluno)
    return novo.save()
}

module.exports.remover = id => {
    return Aluno
        .deleteOne({_id: id})
}

module.exports.atualizar = aluno => {
    var novo = new Aluno(aluno)
    return Aluno
        .findOneAndUpdate({_id: aluno._id}, {
            $set: {
                nome: novo.nome,
                curso: novo.curso,
            }
        })
        .exec()
}