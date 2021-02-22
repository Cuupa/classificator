INSURANCE_POLICY = {
	oneOf("versicherungsschein"),
	not("Nachtrag zum Versicherungsschein"),
	not("entnehmen Sie bitte Ihrem Versicherungsschein")
}