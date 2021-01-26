BILL = {
	oneOf("rechnung", "jahresrechnung", "Zahlung der", "zahlen Sie den Betrag", "Rechnungs-Nr"),
	oneOf("eur, "euro", "€"),
	not("beitragsrechnung"),
	not("dauerauftrag", "quittungsbeleg"),
	not("versicherungsschein", "versicherungs-nr"),
	not("gehaltsabrechnungen"),
	not("rechnung trägt")
	not("keine Zahlung erhalten")
}