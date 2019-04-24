#include <iostream>
#include <fstream>
#include <sstream>
#include <time.h>
#include <stdlib.h>
#include <string>
using namespace std;

std::string hospital[6] {"Hospital de Braga", "Hospital de Guimarães", "Centro de Saúde S.Nicolau", "Hospital da Luz", "Hospital Nossa Senhora da Oliveira", "Centro de Saúde de Urgezes"};
std::string tipo[11] {"Ortopedia","Cardio","Maternidade","Enfermaria","Pediatria","Psiquiatria","Psicologia","Genecologia","Urologia","Clinica Geral", "Otorrinologia"};
std::string nomes[11] {"Filipe","Bruno","Eduardo","Marcia", "Ines", "Mario","Maria","Alexandre","Bráulio","Rafael","Catarina"};

std::string desc[7] {"Protese","Gravidez","Pacemaker","Vacina","Problema Respiratorio","Colonoscopia","Endoscopia"};

/*
int main(int argc, char* argv[]){
    ostringstream os;
    srand(time(NULL));

    for(int i = 1; i < 50; i++){
        int hi = rand() % 6;
        int ti = rand() % 11;
        int ni = rand() % 11;

        os << "prestador(" << i << ", '" << nomes[ni] << "', '" << tipo[ti] << "', '" << hospital[hi] << "')." << endl;
    }



    std::ofstream outfile;
    outfile.open("tmp.txt");

    if (!outfile.is_open())
            perror("ofstream.open");

    outfile << os.str();
    outfile.close();
}

*/
int main(int argc, char* argv[]){
    ostringstream os;
    srand(time(NULL));

    for(int i = 1; i < 50; i++){
        int ui = rand() % 15 + 1;
        int pi = rand() % 40 + 1;
        int di = rand() % 6;

        int d = rand() % 31 + 1;
        int m = rand() % 12 + 1;
        int a = rand() % 20;

        int v = rand() % 100 + 5;
        os << "cuidado(" << i << ", data(" << d << ", " << m << ", " << a << "), " << ui << ", " << pi << ", '" << desc[di] << "', " << v << ")." << endl;
    }



    std::ofstream outfile;
    outfile.open("tmp2.txt");

    if (!outfile.is_open())
            perror("ofstream.open");

    outfile << os.str();
    outfile.close();
}