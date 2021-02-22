SICKNOTE = {
	oneOf("Arbeitsunfähigkeitsbescheinigung", "Arbeitsunfähigkeits-"),
	oneOf("ICD 10", "ICD-10"),
	all("Erstbescheinigung", "Folgebescheinigung"),
	all("arbeitsunfähig seit", "voraussichtlich arbeitsunfähig", "festgestellt am")
}