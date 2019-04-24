%{
#include <stdio.h>
int c = 0;
%}
%%
[Jj][Aa]([Nn][Ee][Ii][Rr][Oo])?     { c++;printf(" 1"); }
(?i:Fevereiro)   { printf(" 2"); }
(?i:Marco)       { printf(" 3"); }
%%

int yywrap(){
    return 1;
    }

int main(){
    printf("Vou começar a filtrar\n");
    yylex();
    printf("Terminado, Janeiros: %d\n",c);
    return 0;
    }
