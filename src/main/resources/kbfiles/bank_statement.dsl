BANK_STATEMENT = {
	oneOf("kontoauszug", "auszug"),
	oneOf("eur, "euro", "€"),
	not("mahnung", "beitragsrechnung", "dauerauftrag"),
	not("auf dem Kontoauszug bestätigen")
}