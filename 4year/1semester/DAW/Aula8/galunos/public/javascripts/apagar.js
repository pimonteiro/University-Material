function apagaAluno(ident){
    axios.delete('/api/alunos/' + ident)
        .then(response => window.location.assign('/alunos'))
        .catch(error => console.log(error))
}