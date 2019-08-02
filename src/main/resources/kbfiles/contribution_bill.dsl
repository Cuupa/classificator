CONTRIBUTION_BILL = {
	oneOf("beitragsrechnung"),
	oneOf("eur, "euro", "€"),
	not("mahnung")
}