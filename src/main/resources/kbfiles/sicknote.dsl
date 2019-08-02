SICKNOTE = {
	oneOf("Arbeitsunfähigkeitsbescheinigung"),
	oneOf("ICD 10"),
	all("Erstbescheinigung", "Folgebescheinigung"),
	all("arbeitsunfähig seit", "voraussichtlich arbeitsunfähig bis", "festgestellt am")
}