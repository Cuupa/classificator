BILL = {
	oneOf("rechnung", "jahresrechnung"),
	oneOf("eur, "euro", "€"),
	not("mahnung", "beitragsrechnung")
	not("dauerauftrag", "kontoauszug", "auszug", "kontostand", "quittungsbeleg"),
	not("versicherungsschein", "versicherungs-nr")
}