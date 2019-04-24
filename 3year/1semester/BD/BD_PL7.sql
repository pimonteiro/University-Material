use Caderneta;

-- 1
SELECT c.PagCaderneta
	FROM Jogador j, Cromo c Where Equipa IN (Select id from Equipa Where Designacao="Rio Ave Futebol Clube" or Designacao="Sporting CLube de Braga")
    AND Posicao IN (SELECT id from Posicao WHERE Designacao="Defesa")
    AND c.jogador=j.Nr;


-- 2
Select c.Nr as Numero, j.Nome as Nome, p.Designacao as Posicao 
	From Jogador j, Cromo c, Posicao p
    Where j.Posicao NOT IN(
		Select id
        FROM Posicao
		WHERE Designacao="Médio" or Designacao="Defesa")
	AND Equipa  IN(
		SELECT id
        FROM Equipa
        WHERE Treinador="Jorge Jesus" or Treinador="Nuno Espirito Santo")
	AND j.Nr=c.Nr
    AND p.id=j.posicao
    ORDER BY c.Nr ASC;


-- 3
CREATE VIEW FaltaCromos AS
	SELECT c.Nr, j.Nome, e.Designacao
    FROM Cromo  c, Jogador j, Equipa e
		WHERE c.Adquirido="N"
		AND j.Equipa=e.id
		AND c.Jogador=j.NR;

-- 4        
DELIMITER //
CREATE PROCEDURE obter_cromos_equipa(IN Clube VARCHAR(45))
	BEGIN
		SELECT c.Nr as Numero, c.PagCaderneta as Pagina, j.Nome as Nome, c.Adquirido as Adquirido
        FROM Cromo c, Jogador j, Equipa e
			WHERE c.Jogador = j.Nr
            AND j.Equipa = e.id
            AND e.Designacao=Clube
            ORDER BY c.PagCaderneta ASC, c.Nr, ASC;
    END //
DELIMITER //


CALL obter_cromos_equipa("Sportin Clube de Braga");

-- 5
DELIMITER //
CREATE PROCEDURE caderneta_completa()
	BEGIN
		SELECT c.Nr, tc.Descricao, j.Nome, c.Adquirido, e.Designacao
        FROM Cromo c INNER JOIN TipoCromo tc on c.Tipo=tc.Nr
			LEFT OUTER JOIN Jogador j ON c.Jogador=j.Nr
            LEFT OUTER JOIN Equipa e ON j.Equipa = e.id
            ORDER BY c.Nr;
	END //
DELIMITER //

CALL caderneta_completa();


-- 6
DELIMITER //
CREATE FUNCTION repetido(numero INT)
RETURNS VARCHAR(45)
deterministic
BEGIN
	DECLARE res VARCHAR(45);
    DECLARE rep CHAR(1);
    SET rep = (SELECT adquirido from Cromo WHERE nr=numero);
	
    IF rep= 'S' THEN SET res='Repetido';
    ELSE SET res='Novo';
    END IF;
    
    return (res);
END //
DELIMITER //

-- 7



-- 8

CREATE TABLE IF NOT EXISTS audCromos(
	data_adquirido DATE,
    NrCromo INT
);

DELIMITER //
CREATE TRIGGER atualiza_cromos AFTER UPDATE ON Cromo
FOR EACH ROW
	BEGIN
		IF OLD.Adquirido='N' and NEW.Adquirido='S' THEN
			INSERT INTO audCromos values(NOW(), OLD.Nr);
		END IF;
    END //
DELIMITER //

UPDATE Cromo set Adquirido='S' where nr=87;
Select * from audCromos;