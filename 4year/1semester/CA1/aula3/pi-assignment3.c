#include <omp.h>
#include <mpi.h>
#include <stdio.h>

static long num_steps = 100000;
double step, pi;

int main(int argc, char** argv){
    int i;
    double x, sum = 0.0;

    step = 1.0/(double) num_steps;

    MPI_Init_thread(&argc,&argv,MPI_THREAD_MULTIPLE,&threading);
    MPI_Comm comm = MPI_COMM_WORLD;

    double start = omp_get_wtime();
    #pragma omp parallel{
        int procno, nprocs;
        MPI_Comm_rank(comm,&procno);
        MPI_Comm_size(comm,&nprocs);

        #pragma omp for private(x) reduction(+:sum)
            for(i = 0; i < (num_steps); i++){
                x = (i+0.5)*step;
                sum = sum + 4.0/(1.0 + x*x);
            }
    }
    

    pi = step * sum;
    double end = omp_get_wtime();
    printf("Pi = %f\nTook %f seconds", pi, end-start);
    return 0;

}