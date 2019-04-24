use Mercearia;


-- 1.c)--

-- 1
select * from Cliente; -- IT WORKS --
-- 2
SELECT * from Cliente
	WHERE Localidade = 'Aguada do Queixo';
-- 3
SELECT Profissao from Cliente;
-- 4
SELECT Designacao, Preco from Produto ORDER BY Designacao ASC;
-- 5
SELECT Numero, Total from Venda
	where Data='2017-10-05';

-- 1.d)--

-- 1
INSERT INTO Cliente Values (8,'Filipe', '1998-02-28', 'Estudante', 'Avenida Rio de Janeiro', 'Guimarães', '4810-009', 251165710, 'pi@pi.pt', 0, NULL);
SELECT * FROM Cliente;

-- 2
UPDATE Cliente
	SET Rua='Avenida de Guimarães';
SELECT Rua FROM Cliente
	where Numero=8;

-- 3
UPDATE Produto
	SET Preco =;

-- 4
INSERT INTO Venda Values (10,'2018-10-29',1,0,8);

INSERT INTO VendaProduto VALUES(10,1,1, (SELECT Preco from Produto where Numero=1),(SELECT Preco from Produto where Numero=1)*1); 
INSERT INTO VendaProduto VALUES(10,2,1, (SELECT Preco from Produto where Numero=2),(SELECT Preco from Produto where Numero=2)*1); 

SELECT * From Venda Where Numero = 10;
SELECT * From VendaProduto where venda = 10;

Update Venda
SET Total = (SELECT SUM(Valor) from VendaProduto Where Venda=10)
	where Numero=10;

-- 5
Delete from VendaProduto Where Venda=10;
Delete from Venda Where Numero=10;