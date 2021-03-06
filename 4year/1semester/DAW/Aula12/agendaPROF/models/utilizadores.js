const mongoose = require('mongoose')
const crypto = require('crypto');
const jwt = require('jsonwebtoken');

var utilizadorSchema = new mongoose.Schema({
    email: { type: String, required: true },
    nome: { type: String, required: true },
    hash: String,
    salt: String,
    ultimoAcesso: String
  });


  utilizadorSchema.methods.setPassword = function(password) {
    this.salt = crypto.randomBytes(16).toString('hex');
    this.hash = crypto.pbkdf2Sync(password, this.salt, 10000, 512, 'sha512').toString('hex');
  };
  
  utilizadorSchema.methods.validatePassword = function(password) {
    const hash = crypto.pbkdf2Sync(password, this.salt, 10000, 512, 'sha512').toString('hex');
    return this.hash === hash;
  };
  
  utilizadorSchema.methods.generateJWT = function() {
    const today = new Date();
    const expirationDate = new Date(today);
    expirationDate.setDate(today.getDate() + 60);
  
    return jwt.sign({
      email: this.email,
      id: this._id,
      exp: parseInt(expirationDate.getTime() / 1000, 10),
    }, 'secret');
  }
  
  utilizadorSchema.methods.toAuthJSON = function() {
    return {
      _id: this._id,
      email: this.email,
      token: this.generateJWT(),
    };
  };


module.exports = mongoose.model('utilizadores', utilizadorSchema)