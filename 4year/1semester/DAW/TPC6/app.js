var xmlParser = require('xml-js')
var fs = require('fs')

var path = require('path');
var logger = require('morgan');
var createError = require('http-errors');
var express = require('express');

var musicasRouter = require('./routes/index');

var app = express();

fs.readFile('arq-son-EVO.json',(err,data)=>{
    if(err){
        fs.readFile('arq-son-EVO.xml', (err,data)=>{
            if(!err){
                fs.writeFile('arq-son-EVO.json', xmlParser.xml2json(data, {compact: true, spaces: 4}), (err)=>{
                    if(err)
                        console.log("Can't write to file.")
                })
            }
        })
    }
    else
        console.log(data)
})

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'pug');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', musicasRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;

