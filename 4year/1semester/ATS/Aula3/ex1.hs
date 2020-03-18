import Test.QuickCheck

data Carro = Carro Tipo Marca Matricula NIF CPKm Autonomia
                deriving Show

data Tipo   = Combustao
            | Eletrico
            | Hibrido
            deriving Show


type Marca = String
type Matricula = String
type NIF = String -- NIF Proprietario
type CPKm = Float -- Consumo por Km
type Autonomia = Int


genMarca :: Gen String
genMarca = frequency [(120,return "Renault"),(85,return "Mercedes"),
                        (12,return "Porsche"),(4,return "Ferrari")]

--1
genTipo :: Gen Tipo
genTipo = frequency [(70,return Combustao),(5,return Eletrico),(25,return Hibrido)]

--2
genCPKm :: Gen CPKm
genCPKm = do
        numero <- choose(0.1,2)
        return numero

--3
genAutonomia :: Gen Autonomia
genAutonomia = do
        numero <- choose(50,600)
        return numero


--4
genMatricula :: Gen Matricula
genMatricula = do
        p1 <- vectorOf 2 (elements ['0'..'9'])
        p2 <- vectorOf 2 (elements ['0'..'9'])
        p3 <- vectorOf 2 (elements ['A'..'Z'])
        let tt = p3 ++ "-" ++ p1 ++ "-" ++ p2
        return tt

--5
genCarro :: [NIF] -> Gen Carro
genCarro nif = do
        tipo <- genTipo
        marca <- genMarca
        matricula <- genMatricula
        cpk <- genCPKm
        auto <- genAutonomia
        nn <- elements nif
        return (Carro tipo marca matricula nn cpk auto)