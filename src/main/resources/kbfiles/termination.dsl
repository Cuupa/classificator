TERMINATION = {
	oneOf("kuendigung", "hiermit kuendige ich"),
	not("information zur kuendigung"),
	not("kuendigung oder"),
	not("vorzeitiger kuendigung"),
	not("Kündigung kann frühestens"),
	not("Überschussbeteiligung"),
	not("eine Kündigung zugegangen ist.")
}