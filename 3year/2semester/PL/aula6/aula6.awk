BEGIN { FS="\t" }
#a) NR >= 5 && NR <= 15 {for(i = 1; i <= 2; i++){print $i;} print "Fim do User: " NR "\n";}
#NR == 2    {for(i = 1; i <= NF; i++){print i" " $i;}}
#b) $10~/[Ii][Nn][Dd][Ii][Vv][Ii][Dd][Uu][Aa][Ll]/ && $12~/[Vv][Aa][Ll][Oo][Nn][Gg][Oo]/ {print $1 " --> " $10 " | " $12;}
$1~/Paulo|Ricardo/ && $11~/^91[0-9]{7}/ {print $1 ":" $11 " --> " $5;}
END {}
