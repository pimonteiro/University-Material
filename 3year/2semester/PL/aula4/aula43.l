%{
#include <stdio.h>
#include <stdlib.h>
%}
%x TAG
%%

\<!.*\>         {;}
\<\/.*/>         {;}
\<[^>]+\>       { yytext[yyleng - 1] = '\0'; printf("%s\n", yytext+1);}
<*>.|\n         {;}

%%
int yywrap(){
    return 1;
}

int main(){
    yylex();
    return 1;
}
