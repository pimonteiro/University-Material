var Obra = require('../models/obra');

module.exports.listar = query => {
    return Obra
        .find({
            $and: [
                {$or: [{undefined: {$eq: query.ano}}, {'anoCriacao': query.ano}]},
                {$or: [{undefined: {$eq: query.compositor}}, {'compositor': query.compositor}]},
                {$or: [{undefined: {$eq: query.duracao}}, {'duracao': {$gte: query.duracao}}]},
                {$or: [{undefined: {$eq: query.periodo}}, {'periodo': query.periodo}]}
            ]
        })
        .exec()
}

module.exports.listarUm = idObra => {
    return Obra
            .find({'_id': idObra})
            .exec()
}

module.exports.listarCompositores = () => {
    return Obra
            .distinct("compositor")
            .exec()
}

module.exports.listarPeriodos = () => {
    return Obra
            .distinct("periodo")
            .exec()
}
