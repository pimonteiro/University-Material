var express = require('express');
var router = express.Router();
var axios = require('axios');

router.get('/inserir', function(req, res, next) {
  res.render('form-aluno')
});

router.get('/editar/:idAluno', function(req, res, next){
  axios.get('http://localhost:3000/api/alunos/' + req.params.idAluno)
    .then(dados => {
      res.render('edita-aluno', {aluno: dados.data})
    })
    .catch(erro => {
      res.render('error', {error: erro})
    })
})

router.post('/editar', function(req, res, next){
  axios.put('http://localhost:3000/api/alunos', req.body)
    .then(dados => {
      res.redirect('/')
    })
    .catch(erro => {
      res.render('error', {error: erro})
    })
})

router.post('/', function(req, res, next) {
  axios.post('http://localhost:3000/api/alunos', req.body)
    .then(dados => {
      res.redirect('/')
    })
    .catch(erro => {
      res.render('error', {error: erro})
    })
});


/* GET users listing. */
router.get('/', function(req, res, next) {
  axios.get('http://localhost:3000/api/alunos')
    .then(dados => {
      res.render('lista-alunos', {lista: dados.data})
    })
    .catch(erro => {
      res.render('error', {error: erro})
    })
});

router.get('/:idAluno', function(req, res, next) {
  axios.get('http://localhost:3000/api/alunos/' + req.params.idAluno)
  .then(dados => {
    res.render('pag-aluno', {aluno: dados.data})
  })
  .catch(erro => {
    res.render('error', {error: erro})
  })
});
module.exports = router;
