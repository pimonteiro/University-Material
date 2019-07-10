%{
    #include<stdio.h>
    #define _GNU_SOURCE

    int tabid[26];
    int nextAddr = 0;
    int labelCount = 0;
%}

%union { char id; int num; char* str; }

%token  INTEGER CODE END READ PRINT VAR IF ELSE
%token <id> ID
%token <num> NUM

%type <str> Code Instrucoes Instrucao Type Atrib Leitura Escrita Expr Termo Fator Declaracoes Declaracao Variaveis Cond Condicao

%%

Program : Variaveis Code                    { printf("%s\nSTART\n%s\nSTOP\n", $1, $2); }
    ;

Variaveis : VAR Declaracoes                 { $$ = $2; }
    ;

Declaracoes : Declaracoes ';' Declaracao    { asprintf(&$$, "%s%s", $1, $3); }
    | Declaracao                            { $$ = $1; }
    ;

Declaracao : ID ':' Type                    {   if(tabid[$1 - 'a'] == -1){
                                                    tabid[$1 - 'a'] = nextAddr++; 
                                                    asprintf(&$$, "PUSHI 0\n");
                                                } 
                                                
                                                else{
                                                    printf("Variavel %c já foi declarada.\n", $1);
                                                    exit(1);
                                                }
                                            }
    ;

Type : INTEGER
    ;

Code : CODE Instrucoes END                  { $$ = $2; }
    ;

Instrucoes : Instrucoes ';' Instrucao       { asprintf(&$$, "%s%s", $1, $3); }
    | Instrucao                             { $$ = $1; }
    ;

Instrucao : Atrib                           { $$ = $1; }
    | Leitura                               { $$ = $1; }
    | Escrita                               { $$ = $1; }
    | Condicao                              { $$ = $1; }
    ;

Atrib : ID '=' Expr                         { if(tabid[$1 - 'a'] != -1){
                                                asprintf(&$$, "%sSTOREG %d\n", $3, tabid[$1 - 'a']); 
                                              } else {
                                                printf("Error: Variável (%c) não declarada.\n", $1);
                                                exit(1);
                                              }
                                            }
    ;

Leitura : READ '(' ID ')'                   {if(tabid[$3 - 'a'] != -1){
                                                asprintf(&$$, "READ\nATOI\nSTOREG %d\n", tabid[$3 - 'a']);
                                            } else {
                                                printf("Error: Variável (%c) não declarada.\n", $3);
                                                exit(1);
                                            }
                                            }
    ;

Escrita : PRINT '(' Expr ')'                  {asprintf(&$$, "%sWRITEI\n", $3);}
    ;

Condicao : IF '(' Cond ')' '{' Instrucoes '}' ELSE '{' Instrucoes '}'   
                                    {asprintf(&$$, "%sJZ SENAO%d\n%sJUMP FIMSE%d\nSENAO%d: NOOP\n%sFIMSE%d: NOOP\n",
                                    $3, labelCount, $6, labelCount,labelCount, $10, labelCount++);}
         ;

Cond : Expr '<' Expr            {asprintf(&$$, "%s%sLT\n", $1, $3);}


Expr : Termo                {$$=$1;}
    | Expr '+' Termo        { asprintf(&$$, "%s%sADD\n", $1, $3);}
    | Expr '-' Termo        { asprintf(&$$, "%s%sSUB\n", $1, $3);}

    ;

Termo : Fator               {$$=$1;}
    | Termo '*' Fator       { asprintf(&$$, "%s%sMUL\n", $1, $3);}
    | Termo '/' Fator       { asprintf(&$$, "%s%sDIV\n", $1, $3);}

    ;

Fator : NUM                 {asprintf(&$$, "PUSHI %d\n", $1);}
    | ID                    {if(tabid[$1 - 'a'] != -1) {
                                asprintf(&$$, "PUSHG %d\n", tabid[$1 - 'a']);
                             } else {
                                printf("Error: Variável (%c) não declarada\n", $1);
                                exit(1);
                             }
                             }
    | '(' Expr ')'           { $$=$2;}
    ;

%%
#include "lex.yy.c"
void yyerror(char* s){ 
    printf("Error: %s\n", s); 
}

int main(){
    for(int i = 0; i < 26; i++)
        tabid[i] = -1;

    yyparse();
    return 0;
}
