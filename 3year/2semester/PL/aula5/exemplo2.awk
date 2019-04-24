BEGIN { FS =":" }
     { conta[$1]++ }
END { for(user in conta) {print user " -> " conta[user] }; print "Numero de utilizadores: " NR }
