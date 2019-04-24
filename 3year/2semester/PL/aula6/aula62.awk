#Escrever um ficheiro com nome ano.html, onde tem: <li>numero de processo - dwdwd </li>, etc    > cria e destroi se existir || >> append ao que existe

BEGIN { FS="::"; RS="\n+" }
    { split($2, date, "[/.-]"); contaDatas[date[1]]++;}
END { for(c in contaDatas) {print c "--> "contaDatas[c] > "processos.out";}}
