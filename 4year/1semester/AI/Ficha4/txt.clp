(defrule add-person-name (person ?na ?id) => (assert (name ?na)))
(defrule add-person-age (person ?na ?id) => (assert (age ?id)))
(defrule add-person-text (person ?na ?id) => (printout t "Person " ?na " tem " ?id " anos." crlf))



;(deftemplate person (slot name) (slot age))

(assert (person FIlipe 21))
(assert (person Rodeio 25))
(run)
(facts)