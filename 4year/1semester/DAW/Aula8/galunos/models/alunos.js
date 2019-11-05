var mongoose = require('mongoose');

var notaSchema = new mongoose.Schema({
    indicador: String,
    nota: Number
});

var alunoSchema = new mongoose.Schema({
    _id: String,
    nome: String,
    curso: String,
    notas: [notaSchema]    
});


module.exports = mongoose.model('aluno', alunoSchema);