var express = require('express');
var router = express.Router();

var Alunos = require('../controllers/alunos');

/* GET users listing. */
router.get('/alunos', function(req, res, next) {
  Alunos.listar()
    .then(dados => res.jsonp(dados))
    .catch(erro => res.status(500).jsonp(erro))
});

router.get('/alunos/:idAluno', function(req, res, next) {
    Alunos.consultar(req.params.idAluno)
        .then(dados => res.jsonp(dados))
        .catch(erro => res.status(500).jsonp(erro))
  });

router.post('/alunos', function(req, res, next) {
    Alunos.inserir(req.body)
        .then(dados => res.jsonp(dados))
        .catch(erro => res.status(500).jsonp(erro))
});  

router.delete('/alunos/:idAluno', function(req, res, next) {
    Alunos.remover(req.params.idAluno)
      .then(dados => res.jsonp(dados))
      .catch(erro => res.status(500).jsonp(erro))
  });

module.exports = router;
