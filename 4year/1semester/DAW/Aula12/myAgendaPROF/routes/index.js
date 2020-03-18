var express = require('express');
var router = express.Router();
var axios = require('axios')
var passport = require('passport')

router.get('/', verificaAutenticacao, function(req, res) {
    axios.get('http://localhost:5003')
      .then(dados => res.render('index', {lista: dados.data}))
      .catch(e => res.render('error', {error: e}))
});

router.get('/eventos/:id', verificaAutenticacao, function(req,res){
  axios.get('http://localhost:5003/eventos/' + req.params.id)
      .then(dados => res.render('evento', {evento: dados.data}))
      .catch(e => res.render('error', {error: e}))
})

router.get('/login', function(req,res){
  res.render('login')
})

router.post('/login', passport.authenticate('local', 
  { successRedirect: '/',
    successFlash: 'Utilizador autenticado com sucesso!',
    failureRedirect: '/login',
    failureFlash: 'Utilizador ou password inválido(s)...'
  })
)

function verificaAutenticacao(req,res,next){
  if(req.isAuthenticated()){
  //req.isAuthenticated() will return true if user is logged in
    next();
  } else{
    res.redirect("/login");}
}

module.exports = router;
