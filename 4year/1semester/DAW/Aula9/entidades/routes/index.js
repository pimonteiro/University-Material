var express = require('express');
var Axios = require('axios');
var router = express.Router();

const link = "http://clav-api.dglab.gov.pt/api/entidades"
const apikey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE1NzM0ODgwMDgsImV4cCI6MTU3NjA4MDAwOH0.UD0UdMrzKcWDop8HlwqdeOuK_ZzZxHvOMOP2DMkIjUQ"

router.get('/:id', function(req, res, next) {
  Axios.get(link + '/' + req.params.id + '?apikey=' + apikey)
      .then(response1 => {
        Axios.get(link + '/' + req.params.id + '/intervencao/dono' + '?apikey=' + apikey)
          .then(response2 => {
            Axios.get(link + '/' + req.params.id + '/intervencao/participante' + '?apikey=' + apikey)
              .then(response3 => {
                res.render('entidade',{ent: response1.data, dono: response2.data, part: response3.data})
              })
              .catch(error => {
                res.render('error', {error: error})
              })        
          })
          .catch(error => {
            res.render('error', {error: error})
          })    
      })
      .catch(error => {
        res.render('error', {error: error})
      })
});

router.get('/', function(req, res, next) {
    Axios.get(link + '?apikey=' + apikey)
        .then(respose => {
          res.render('index',{lista: respose.data})
        })
        .catch(error => {
          res.render('error', {error: error})
        })
});

module.exports = router;
