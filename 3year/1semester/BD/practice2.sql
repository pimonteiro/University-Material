-- 8 - O sistema tem de ser capaz de determinar qual a consulta de um dado utente que cuja receita tinha mais medicamentos;

use CentroDeSaudeAlpiarca;

DELIMITER //
CREATE PROCEDURE ConsultaComMaisMeds(IN utenteID INT)
BEGIN
SELECT T.idConsulta, T.Receita, COUNT(*) as N_Medicamentos
	FROM (
		SELECT C.idConsulta, C.Receita, Receita_Medicamento.Medicamento
			FROM Receita_Medicamento INNER JOIN (
				SELECT idConsulta, Receita
					FROM Consulta
					WHERE Utente = utenteID and Receita IS NOT NULL) as C
				ON Receita_Medicamento.Receita = C.Receita 
		) as T
	GROUP BY T.Receita, T.idConsulta
    ORDER BY N_Medicamentos DESC
    LIMIT 1;
END //
DELIMITER //

CALL ConsultaComMaisMeds2(6);
SELECT * from Receita_Medicamento WHERE Receita = 1;
SELECT * FROM Medicamento WHERE idMedicamento = 604;
SELECT * FROM Consulta WHERE Receita = 1;

SELECT DISTINCT(U.Utente), SUM(U.N) as Num FROM (
		(SELECT DISTINCT(Utente) as Utente, COUNT(*) as N FROM Emergencia GROUP BY Utente)
		UNION ALL 
		(SELECT DISTINCT(Utente.NrUtente) as Utente, COUNT(*) as N FROM Consulta
			INNER JOIN Utente ON Consulta.Utente = Utente.NrUtente
				WHERE Utente.Medico = Consulta.Medico
			GROUP BY Utente.NrUtente)
	) AS U 
        GROUP BY Utente
        ORDER BY Num DESC
        LIMIT 1;