/*********************************************
 * OPL 12.8.0.0 Model
 * Author: Filipe Monteiro
 * Creation Date: Dec 5, 2018 at 3:17:36 PM
 *********************************************/

 // Nº de nodos
 int n = ...;
 
 //Nº de arcos
 int n_arcos = ...;

 //Set de 
{int} Nodos = asSet(1..n);

tuple Arco{
    int i;
    int j;
}

{Arco} Arcos with i in Nodos, j in Nodos = ...;
 // Tempo que demorou a chegar ao nodo i
 int c_total[Arcos] = ...;

 // Variável de Decisão
 dvar int x[Nodos];
 
 // Minimizar o tempo do fogo chegar a todos os nodos
 //maximize max(i in R) x[i];
 maximize sum (i in 1..n) x[i];
 
 subject to {
  

  forall (<i,j> in Arcos) tempo:  x[j] <= x[i] + c_total[<i,j>];
  inicial: x[1] == 0;
  
 }