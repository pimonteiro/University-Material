BEGIN { FS =":" }
#{for(i=1; i <= NF; i++){print $i}; print "FIM DO REGISTO NR: " NR}
#$1=="prh"   {print "PRH"}
#$1~/j.+/    {print "JOSE"}
#/a.*a/       {print "ANA"}
$3>="20"    {print "A " $3}
$4>=20      {print "B " $4}
END { print "Numero de utilizadores: " NR }
