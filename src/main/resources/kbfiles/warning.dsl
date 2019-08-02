WARNING = {
	oneOf("mahnung", "zahlungserinnerung", "angemahnte Betrag"),
	oneOf("keine Zahlung erhalten", "keine Zahlung eingegangen", "noch nicht eingegangen", "noch nicht bei uns eingegangen", "noch nicht erhalten"),
	oneOf("eur", "euro", "€")
}