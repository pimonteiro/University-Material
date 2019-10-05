module Parser where
import Prelude hiding ((<*>),(<$>))

infixl 2 <|>
infixl 3 <*>

type Parser s r = [s] -> [(r , [s])]

symbola :: Parser Char Char
symbola ('a':xs) = [('a',xs)]
symbola _        = []

symbol :: Eq a => a -> Parser a a
symbol s []                 = []
symbol s (x:xs) | x == s    = [(s,xs)]
                | otherwise = []

satisfy :: (a -> Bool) -> Parser a a
satisfy p []                 = []
satisfy p (x:xs) | p x       = [(x,xs)]
                 | otherwise = []

token :: Eq s => [s] -> Parser s [s]
token t []  = []
token t inp = if take (length t) inp == t
              then [(t,drop (length t) inp)]
              else []

succeed :: r -> Parser s r
succeed r inp = [(r,inp)] 

(<|>) :: Parser s a -> Parser s a -> Parser s a
(p <|> q) inp = p inp ++ q inp

--
--pS = token "while"
--  <|> token "for"

(<*>) :: Parser s (a -> b) -> Parser s a -> Parser s b
(p <*> r) inp = [ (f v,ys)
                | (f,xs) <- p inp
                , (v,ys) <- r xs
                ]


(<$>) :: (a -> r ) -> Parser s a -> Parser s r
(f <$> p) inp = [ (f v, xs)
                | (v, xs) <- p inp
                ]


--pS' = f <$> (symbol 'a' <*> symbol 'b' <*> symbol 'c')
--    <|> g <$> (symbol 'd')
--    where f ((a,b),c) = [a,b,c]
--          g d         = [d]


pS' = f <$> symbol 'a' <*> symbol 'b' <*> symbol 'c'
    <|> g <$> symbol 'd'
    where f a b c = [a,b,c]
          g d     = [d]


-- spaces

-- symbol' a = (\a b -> a) <$> symbol a <*> spaces