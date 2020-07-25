BANK_STATEMENT = {
	oneOf("kontoauszug", "auszug"),
	oneOf("eur, "euro", "€"),
	not("SEPA-Lastschriftmandats", "SEPA-Lastschriftmandat", "Lastschriftverfahren")
	not("mahnung", "beitragsrechnung", "quittungsbeleg", "Qui ttungsbeleg"),
	not("auf dem Kontoauszug bestätigen")
}