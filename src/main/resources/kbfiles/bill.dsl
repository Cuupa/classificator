BILL = {
	oneOf("rechnung", "jahresrechnung", "Zahlung der", "zahlen Sie den Betrag", "Rechnungs-Nr"),
	oneOf("eur, "euro", "€"),
	not("mahnung", "beitragsrechnung"),
	not("dauerauftrag", "auszug", "quittungsbeleg"),
	not("versicherungsschein", "versicherungs-nr"),
	not("gehaltsabrechnungen")
}