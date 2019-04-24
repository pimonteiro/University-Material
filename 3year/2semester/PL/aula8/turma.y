%{
#include <stdio.h>
%}

%token ID
%token STRING
%token NUM
%%
Turma   : ID '{' Alunos '}'
        ;

Alunos  : Aluno ';'
        | Alunos '\n' Aluno ';'
        ;

Aluno   : CodA STRING '(' Notas ')'
        ;

CodA    : ID
        ;

Notas   : Nota
        | Notas ',' Nota
        ;

Nota    : NUM
        ;
%%
#include "lex.yy.c"

int yyerror(char* s){
    printf("Error: %s\n", s);
}

int main(){
    yyparse();
    return 0;
}
