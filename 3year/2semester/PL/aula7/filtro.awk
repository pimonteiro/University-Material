BEGIN { RS="\n" }
/<[a-zA-Z][^>]*>/           {conta++; print $0;}
/<\/[a-zA-Z][^>]*>/        {conta++; print $0;}
END {print "#Tags: " conta;}
