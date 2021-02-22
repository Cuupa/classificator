STANDING_ORDER_TERMINATION = {
	all("dauerauftrag", "wurde gelöscht"),
	oneOf("IBAN"),
	oneOf("eur, "euro", "€"),
	not("Kontoauszug")
}