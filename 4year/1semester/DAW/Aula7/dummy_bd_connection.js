var mongoose = require('mongoose')
/*
mongoose.connect('mongodb://localhost/filmes', {useNewUrlParser: true, useUnifiedTopology: true})

var db = mongoose.connection
db.on('error', console.error.bind(console, 'connection error:'))
db.once('open', function(){
    console.log("Ligação ao MongoDB feita com sucesso!")
})
*/
mongoose.connect('mongodb://localhost:27017/filmes', {useNewUrlParser: true, useUnifiedTopology: true})
    .then(() => console.log("Mongo ready: " + mongoose.connection.readyState))
    .catch(() => console.log("Mongo: erro na conexao."))

var filmesSchema = new mongoose.Schema({
    title: String,
    year: Number,
    cast: Array,
    genre: Array
})

var FilmeModel = mongoose.model('filmes ', filmesSchema)
var jcrMovie = new FilmeModel({ title: "Era uma vez...",
                                year: 2019,
                                cast: ["jcr","aluno1","aluno2"],
                                genres:["Thriller","Comédia"]})
console.log("Vou inserir este filme na BD: " + jcrMovie.title)

jcrMovie.save((err,filme) => {
    if(err) return console.error(err);
    else
        console.log(filme.title + " foi gravado com sucesso.")
})

FilmeModel.findOne({title: /Era uma/},(err,filme)=>{
    if(err) return console.error(err);
    else
        console.log("Recuperei o filme: " + filme)
})