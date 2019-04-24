BEGIN { IGNORECASE=1; RS="\n" }
/<A HREF=/>/        {conta++; print $0;}
END {print "#Tags: " conta;}
