BANK_STATEMENT = {
	oneOf("kontoauszug", "auszug"),
	oneOf("eur, "euro", "€"),
	not("mahnung", "beitragsrechnung", "quittungsbeleg", "Qui ttungsbeleg"),
	not("auf dem Kontoauszug bestätigen")
}