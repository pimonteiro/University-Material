var bcrypt = require('bcryptjs')
var hash = bcrypt.hashSync("222", 6)

console.log(bcrypt.compareSync("222", hash))