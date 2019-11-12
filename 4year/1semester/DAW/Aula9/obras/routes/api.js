var express = require('express');
var router = express.Router();
var Obra = require('../controllers/obra');

/* GET users listing. */
router.get('/obras', function(req, res, next) {
  Obra.listar(req.query)
    .then(dados => res.jsonp(dados))
    .catch(error => res.status(500).jsonp(error))
});

router.get('/obras/:idObra', function(req, res, next) {
  Obra.listarUm(req.params.idObra)
    .then(dados => res.jsonp(dados))
    .catch(error => res.status(500).jsonp(error))
})

router.get('/compositores', function(req, res, next) {
  Obra.listarCompositores()
    .then(dados => res.jsonp(dados))
    .catch(error => res.status(500).jsonp(error))
})

router.get('/periodos', function(req, res, next) {
  Obra.listarPeriodos()
    .then(dados => res.jsonp(dados))
    .catch(error => res.status(500).jsonp(error))
})
module.exports = router;
