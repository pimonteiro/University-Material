var express = require('express');
var router = express.Router();
var Utilizadores = require('../controllers/utilizadores')

/* GET users listing. */
router.get('/', function(req, res) {
  Utilizadores.listar()
    .then(dados => res.jsonp(dados))
    .catch(e => res.status(500).jsonp(e))
});

router.get('/:email', function(req, res) {
  Utilizadores.consultar(req.params.email)
    .then(dados => res.jsonp(dados))
    .catch(e => res.status(500).jsonp(e))
});

module.exports = router;
