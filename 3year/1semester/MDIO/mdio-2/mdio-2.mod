/*********************************************
 * OPL 12.8.0.0 Model
 * Author: Filipe Monteiro
 * Creation Date: Dec 4, 2018 at 5:38:24 PM
 *********************************************/
 
 // N� de nodos
 int n = ...;
 // N� de arcos
 int n_arcos = ...;
 // N� de recusos m�ximo a poder alocar
 int b = ...;
 // Constante de retarda��o
 int delta = ...;
 // Nodo a proteger o m�ximo poss�vel
 int p = ...;
 // Nodo de igni��o
 int inicio = ...;

 {int} Nodos = asSet(1..n);

 tuple Arco{
    int i;
    int j;
 }

 {Arco} Arcos with i in Nodos, j in Nodos = ...;
 // Tempo que demorou a chegar ao nodo i
 int c_total[Arcos] = ...;

 
 dvar boolean y[1..n];
 dvar int x[Nodos];

 maximize  x[p];
 
 subject to {
	forall (<i,j> in Arcos) x[j] <= x[i] + (y[i]*delta) + c_total[<i,j>];
	
	sum (i in Nodos) y[i] <= b;
	
	forall (i in Nodos) x[i]>= 0;
	
	x[inicio] == 0;
	
 }
 
 execute {
  
 	for(var i in Nodos)
 		write(x[i]," ");
}  