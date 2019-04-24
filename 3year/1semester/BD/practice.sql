use Caderneta;



SELECT c.Nr as Numero, j.Nome as Nome, p.Designacao as Posicao, j.Equipa as Equipa
	FROM Cromo c, Jogador j, Posicao p
    WHERE Equipa IN (Select id from Equipa where Treinador='Jorge Jesus' or Treinador='Nuno Espírito Santo')
    AND Posicao NOT IN (Select id from Posicao where Designacao='Médio' or Designacao='Defesa')
    AND j.Nr = c.Jogador
    AND p.id = j.posicao
    ORDER BY c.Nr ASC;
    

-- 3
CREATE VIEW emFalta AS
SELECT c.Nr as Numero, j.Nome as Nome, e.Designacao
	FROM Jogador j, Cromo c, Equipa e
    WHERE c.Adquirido='N' AND c.Jogador=j.Nr AND e.id=j.equipa;
    

-- outros VERIFICAR ESTEEEEE
SELECT c.Nr as NrCromo, e.Designacao as Equipa, p.Designacao as Posicao, j.Nome
	FROM Jogador j, Equipa e, Posicao p, Cromo c
    WHERE c.Jogador=j.Nr
    ORDER BY Equipa ASC;
    
    

SELECT c.Nr as Cromo, j.Nome as Nome, c.Adquirido
	FROM Cromo c INNER JOIN Jogador j
    ON c.Jogador=j.Nr
    WHERE c.Adquirido='N';