import Test.QuickCheck

mulL :: Num a => [a] -> a
mulL [] = 0 --Deveria ser |||  mulL [] = 1
mulL (h : t) = h * mulL t

--property 1
prop_mulL_A :: (Eq a,Num a) => [a] -> Bool
prop_mulL_A xs = mulL xs == mulL (reverse xs)

--property 2
prop_mulL_B :: (Eq a,Num a) => [a] -> Bool
prop_mulL_B [x] = mulL [x] == x
prop_mulL_B l = discard l

prop_mulL_C :: (Eq a,Num a) => [a] -> Property
prop_mulL_C l = length l == 1 ==> mulL l == head l

--property 3
prop_mulL_D :: (Eq a,Num a) => [a] -> Bool
prop_mulL_D xs = mulL xs == product xs


find :: (a -> Bool) -> [a] -> Maybe a
find f [] = Nothing
find f (x:xs) = case find f xs of
            Just k -> Just k
            Nothing -> if f x then Just x else Nothing

prop_find_A :: [Int] -> Property
prop_find_A l = all (>0) l ==> find (==0) l == Nothing

prop_find_B :: [Int] -> Property
prop_find_B l = length (filter odd l) > 0 ==>
                    find (odd) l == Just (head (filter odd l))