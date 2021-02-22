SEPA = {
	oneOf("sepa-lastschriftmandat","sepa-lastschriftmandats", "einzugsermaechtigung", "sepa-überweisung/ zahlschein"),
	oneOf("ermaechtige", "erteilung", "erteilte"),
	oneOf("einzuziehen", "einziehen", "einzug"),
	not("Zahlungsformular für sie vorbereitet", "quittung", "gesamtsumme")
}