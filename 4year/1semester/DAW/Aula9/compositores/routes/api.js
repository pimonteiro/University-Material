var express = require('express');
var router = express.Router();
var Compositor = require('../controllers/compositor')

/* GET home page. */
router.get('/compositores', function(req, res, next) {
  Compositor.listar()
    .then(data => res.jsonp(data))
    .catch(erro => res.status(500).jsonp(error))
});

module.exports = router;
