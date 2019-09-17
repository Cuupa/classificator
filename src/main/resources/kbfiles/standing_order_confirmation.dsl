STANDING_ORDER_CONFIRMATION = {
	all("dauerauftrag", "entgegengenommen"),
	oneOf("IBAN"),
	oneOf("eur, "euro", "€"),
	not("Kontoauszug")
}