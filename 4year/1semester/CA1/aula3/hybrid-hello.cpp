#include <cstdio>
#include <omp.h>
#include <mpi.h>

int main(int argc, char **argv) {
    int threading,nprocs,procno;
    MPI_Init_thread(&argc,&argv,MPI_THREAD_MULTIPLE,&threading);
    MPI_Comm comm = MPI_COMM_WORLD;
    MPI_Comm_rank(comm,&procno);
    MPI_Comm_size(comm,&nprocs);

    if (procno==0) {
        switch (threading) {
            case MPI_THREAD_MULTIPLE : printf("Glorious multithreaded MPI\n"); break;
            case MPI_THREAD_SERIALIZED : printf("No simultaneous MPI from threads\n");
            case MPI_THREAD_FUNNELED : printf("MPI from main thread\n"); break;
            case MPI_THREAD_SINGLE : printf("no threading supported\n"); break;
        }
    }
    MPI_Finalize();
}