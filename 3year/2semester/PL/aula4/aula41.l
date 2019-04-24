%{
#include <stdio.h>
#include <stdlib.h>
double soma = 0;
char* ele;
char* eu;
%}
%x EU
%x ELE
%%

^(?i:eu)\ *=\  	{BEGIN EU;}
<EU>[^.]+		{eu = strdup(yytext);}
<EU>\.[ ]*\n    {BEGIN INITIAL;}


^(?i:ele)\ *=\ 	{BEGIN ELE;}
<ELE>[^.]+	    {ele = strdup(yytext);}
<ELE>\.[ ]*\n   {BEGIN INITIAL;}


^(?i:eu):  {printf("(%s) ", eu);}
^(?i:ele): {printf("(%s) ", ele);}
%%

int yywrap(){
    return 1;
}

int main(){
    yylex();
    //printf("%f\n",soma);
    return 1;
}
