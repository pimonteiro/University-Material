%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Programacao em logica estendida

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% SICStus PROLOG: Declaracoes iniciais

:- set_prolog_flag( discontiguous_warnings,off ).
:- set_prolog_flag( single_var_warnings,off ).
:- set_prolog_flag( unknown,fail ).

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do meta-predicado si: Questao,Resposta -> {V,F}
%                            Resposta = { verdadeiro,falso,desconhecido }

si( Questao,verdadeiro ) :-
    Questao.
si( Questao,falso ) :-
    -Questao.
si( Questao,desconhecido ) :-
    nao( Questao ),
    nao( -Questao ).

%--------------------------------- - - - - - - - - - -  -  -  -  -   -
% Extensao do meta-predicado nao: Questao -> {V,F}

nao( Questao ) :-
    Questao, !, fail.
nao( Questao ).



% Extensao do predicado para caracterizar números pares
par(0).
par(X) :-
	N is X-2,
	N >= 0,
	par(N).
-par(X) :-
    nao(par(X)).

% Extensao do predicado para caracterizar números impares
impar(1).
impar(X) :-
	N is X-2,
	N >= 1
	impar(N).
-impar(X) :-
	nao(impar(X)).

% Extensao do predicado capaz de caracterizar dos números naturais
naturais(0).
naturais(X) :-
	N is X-1,
	N >= 0,
	naturais(N).
-naturais(X) :-
	nao(naturais(X)).

% Extensao do predicado capaz de caracterizar o conjunto dos números inteiros
inteiros(X) :-
	naturais(abs(X)).
-inteiros(X) :-
	nao(naturais(abs(X))).

% Extensao do predicado capaz de caracteizar as cores do arco-íris
cor(vermelho).
cor(laranja).
cor(amarelo).
cor(verde).
cor(azul).
cor(anil).
cor(violeta).

arcoiris(cor(X)) :-
	cor(X).
-arcoiris(cor(X)) :-
	nao(cor(X)).

% Extensao do predicado com capacidade para id cores dos equip.
cor(preto).
cor(branco).

equipamento(cor(X),cor(Y)).

% Extensao do predicado atravessar a estrada
atravessar(0).
-atravessar(X) :-
	nao(atravessar(X)).	

% Extensao do predicado terminal
arco(b,a).
arco(b,c).
arco(c,a).
arco(c,d).
arco(f,g).

nodo(a).
nodo(b).
nodo(c).
nodo(d).
nodo(e).
nodo(f).
nodo(g).

terminal(N) :-
	nodo(N),
	nao(arco(N,X)).
-terminal(N) :-
	arco(N,X).


% Ficha7
mamifero(X) :-
	nao(voa(X)),
	exce