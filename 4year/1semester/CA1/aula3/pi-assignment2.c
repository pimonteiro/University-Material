#include <omp.h>
#include <stdio.h>

static long num_steps = 100000;
double step, pi;

int main(int argc, char** argv){
    int i;
    double x, sum = 0.0;

    step = 1.0/(double) num_steps;

    double start = omp_get_wtime();
    #pragma omp parallel for private(x) shared(sum)
        for(i = 0; i < (num_steps); i++){
            x = (i+0.5)*step;
            
            #pragma omp atomic
                sum = sum + 4.0/(1.0 + x*x);
        }
    pi = step * sum;
    double end = omp_get_wtime();
    printf("Pi = %f\nTook %f seconds", pi, end-start);
    return 0;

}