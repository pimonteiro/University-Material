var express = require('express');
var router = express.Router();
var passport = require('passport')
var flash = require('connect-flash')

/* GET home page. */
router.get('/', function(req, res) {
  console.log('Na CB da homepage...')
  console.log(req.sessionID)
  res.render('index');
});

/* Login page. */
router.get('/login', function(req, res) {
  console.log('Na CB da GET login...')
  console.log(req.sessionID)
  res.render('login');
});

router.post('/login', passport.authenticate('local',
  {
    successRedirect: '/protegida',
    successFlash: 'Utilizador autenticado com sucesso!',
    failureRedirect: '/login',
    failureFlash: 'Utilizador ou password inválido(s)...'
  })
)

function verificaAutenticacao(req,res,next){
  if(req.isAuthenticated()){
    next();
  } else{
    res.redirect("/login")
  }
}

router.get('/protegida', verificaAutenticacao, (req,res) => {
  res.send('Atingiste a área protegida: ', JSON.stringify(req.user))
  console.log('Atingiste a área protegida: ', JSON.stringify(req.user))
})

module.exports = router;