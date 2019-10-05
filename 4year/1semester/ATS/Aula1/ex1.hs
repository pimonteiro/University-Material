import Parser
import Data.Char
import Prelude hiding ((<*>),(<$>))


number = f <$> satisfy isDigit
     <|> g <$> satisfy isDigit <*> number
        where f a = [a]
              g a b = a:b

isChar :: Char -> Bool -- VERIFICAR
isChar x = x >= 'a' && x <= 'Z'

ident = f <$> satisfy isAlpha
    <|> g <$> satisfy isAlpha <*> ident
        where f a = [a]
              g a b = a:b

--spaces =  f <$> satisfy (\x -> x `elem` [' ', '\t','\n'])
--      <|> g <$> satisfy (\x -> x `elem` [' ', '\t','\n']) <*> spaces
--        where f a = 
----------------------------------------------------------------
----------------------------------------------------------------

data Exp = AddExp Exp Exp
         | MulExp Exp Exp
         | SubExp Exp Exp
         | GThen Exp Exp
         | LThen Exp Exp
         | OneExp Exp
         | Var String
         | Const Int

instance Show Exp where
    show = showExp

showExp (AddExp e1 e2) = showExp e1 ++ "+" ++ showExp e2
showExp (SubExp e1 e2) = showExp e1 ++ "-" ++ showExp e2
showExp (MulExp e1 e2) = showExp e1 ++ "*" ++ showExp e2
showExp (GThen e1 e2) = showExp e1 ++ ">" ++ showExp e2
showExp (OneExp e) = "(" ++ showExp e ++ ")"
showExp (Const i) = show i
showExp (Var a) = a

pexp :: Parser Char Exp
pexp =  f <$> pterm
    <|> g <$> pterm <*> symbol '+' <*> pexp
    <|> h <$> pterm <*> symbol '-' <*> pexp
        where f a     = a
              g a b c = AddExp a c
              h a b c = SubExp a c

pterm :: Parser Char Exp
pterm =  f <$> pfactor
     <|> g <$> pfactor <*> symbol '*' <*> pterm
        where f a = a
              g a b c = MulExp a c

pfactor :: Parser Char Exp
pfactor =  f <$> number
       <|> g <$> ident
       <|> h <$> symbol '(' <*> pexp <*> symbol ')'
        where f a = Const (read a)
              g a = Var a
              h a b c = OneExp b

-- SUSBSITUIR TODOS OS symbol POR symbol'