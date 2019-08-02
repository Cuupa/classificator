SEPA = {
	oneOf("sepa-lastschriftmandat", "einzugsermaechtigung", "sepa-überweisung/ zahlschein"),
	oneOf("ermaechtige"),
	oneOf("einzuziehen", "einziehen"),
	not("Zahlungsformular für sie vorbereitet")
}