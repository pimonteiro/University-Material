%{
#include <stdio.h>
#include <stdlib.h>
double soma = 0;
// int acorda = 0; solução "primária"
%}
d [0-9]
%x ACORDA
%%
!|(?i:agora)               {BEGIN ACORDA;}
<ACORDA>[+-]?{d}+(\.{d}+)? {soma += atof(yytext);}
<ACORDA>=                  {printf("%f\n",soma); soma = 0.0;}
<ACORDA>!|(?i:agora)       {BEGIN INITIAL;}
<*>.|\n                    {;}

%%
// [+-]?{d}+(\.{d}+)? {if(acorda) soma += atof(yytext);} solução "primaria"
int yywrap(){
    return 1;
}

int main(){
    yylex();
    //printf("%f\n",soma);
    return 1;
}
