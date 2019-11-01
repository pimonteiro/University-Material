#include <omp.h>
#include <mpi.h>

int main(int argc, char** argv){
    int threading,nprocs,procno,len;
     char name[MPI_MAX_PROCESSOR_NAME];

    MPI_Init_thread(&argc,&argv,MPI_THREAD_MULTIPLE,&threading);
    MPI_Comm comm = MPI_COMM_WORLD;

    #pragma omp parallel private(nprocs,procno,len,name)
    {
        MPI_Comm_rank(comm,&procno);
        MPI_Comm_size(comm,&nprocs);
        MPI_Get_processor_name(name, &len);

         int id_thread = omp_get_thread_num();
         int n_threads = omp_get_num_threads();
         printf("Hello from the Thread %d of %d in rank %d of %d on %s\n", id_thread, n_threads, procno, nprocs, name);
    }
    MPI_Finalize();
    return 0;
}