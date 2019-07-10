/*********************************************
 * OPL 12.8.0.0 Model
 * Author: Filipe Monteiro
 * Creation Date: Dec 5, 2018 at 3:17:36 PM
 *********************************************/

 // Nº de nodos
 int n = ...;
 
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
 dvar int y[Nodos][Nodos];
 
 // Minimizar o tempo do fogo chegar a todos os nodos
 minimize sum (<i,j> in Arcos) c_total[<i,j>]*y[i][j];
 
 subject to {
  
  forall (<i,j> in Arcos) sum(<i,k> in Arcos) y[i][k] - sum(<k,j> in Arcos) y[k][j] >= 1; 
  
  forall(i,j in Nodos) y[i][j] >= 0;
 }