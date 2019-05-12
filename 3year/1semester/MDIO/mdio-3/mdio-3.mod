/*********************************************
 * OPL 12.8.0.0 Model
 * Author: Filipe Monteiro
 * Creation Date: Dec 7, 2018 at 1:03:03 PM
 *********************************************/

 // Nº de nodos
 int n = ...;
 // Nº de recursos disponíveis
 int b = ...;
 // Intervalo de tempo 
 float t = ...;
 // Probabilidade de i arder
 float prb[1..n]=...;
 //
 int delta = ...;
 
 {int} Nodos = asSet(1..n);

 tuple Arco{
    int i;
    int j; 
 }

 {Arco} Arcos with i in Nodos, j in Nodos = ...;
 // Tempo que demorou a chegar ao nodo i
 int c_total[Arcos] = ...;
 
 
 // Usar recurso no nodo i ou não
 dvar boolean y[1..n];
 // Se nodo i ardeu antes do tempo
 dvar boolean z[1..n];
 // Tempo de chegada ao nodo i
 dvar int x[1..n];
 
 maximize sum(i in 1..n) prb[i]* (sum (j in 1..n) z[i]);
 
 subject to{
 	   
 	forall(<i,j> in Arcos) x[j] <= x[i] + (delta*y[i]) + c_total[<i,j>];
 	forall(i in Nodos) x[i] <= t;   
	x[1] == 0;   
	sum (i in Nodos) y[i] <= b;
	forall(i in Nodos) x[i] >= 0;
 }