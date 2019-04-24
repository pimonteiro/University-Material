%option noyywrap

%{
#include <stdio.h>
%}
SEP [ \t\n.,;:!?]

%%
\\tb {printf("tambem");}
\\mm {printf("mesmo");}
\\cqd {printf("como queriamos demonstrar");}
\\mb {printf("muito bem");}
\\agr {printf("agora");}

[a-zA-Z]+\//{SEP} { yytext[yyleng - 1] = '\0'; printf("%smente", yytext);}
~/[a-zA-Z]+ { printf("in");}
%%

int main(){
    yylex();
    return 0;
}
