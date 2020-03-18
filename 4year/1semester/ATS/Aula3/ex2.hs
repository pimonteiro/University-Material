import Test.QuickCheck

data Aluno = Aluno Nome Numero Tipo [Nota]
            deriving Show

data Tipo   = Normal
            | Militar
            | Trabalhador
            deriving Show

type Nome = String
type Numero = String
type Nota = Int


genNome :: Gen Nome
genNome = frequency [(120,return "Filipe"),(85,return "Rodrigues"),
                        (12,return "Marcia"),(4,return "Bruno")]

--1
genTipo :: Gen Tipo
genTipo = frequency [(80,return Normal),(15,return Militar),(5,return Trabalhador)]

--2
genNumero :: Gen Numero
genNumero = do
        l <- vectorOf 1 (elements ['A'..'Z'])
        n <- choose(1,95000)
        let list = show n
        return (l ++ list)

genNota :: Gen Nota
genNota = do
    numero <- choose(0,20)
    return numero        