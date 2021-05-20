object IBANTestData {

    val validIbans = listOf(
        GermanIbans.validIbans,
        AustrianIbans.validIbans,
        NorwegianIbans.validIbans,
        StLuciaIBans.validIbans
    ).flatten()
    val invalidIbans = listOf(
        GermanIbans.invalidIbans, AustrianIbans.invalidIbans,
        NorwegianIbans.invalidIbans,
        StLuciaIBans.invalidIbans, IllegalIbans.ibans
    ).flatten()

    object GermanIbans {
        val validIbans = listOf("DE19 1234 1234 1234 1234 12", "DE37123412341234123412")
        val invalidIbans = listOf("19 1234 1234 1234 1234 12", "37123412341234123412", "DE3712341234A234123412")
    }

    object AustrianIbans {
        val validIbans = listOf("AT19 1234 1234 1234 1234", "AT371234123412341234")
        val invalidIbans = listOf("19 1234 1234 1234 1234", "371234123412341234", "AT3712341234A2341234")
    }

    /**
     * shortest IBAN
     */
    object NorwegianIbans {
        val validIbans = listOf("NO19 1234 1234 123", "AT3712341234123")
        val invalidIbans = listOf("19 1234 1234 123", "3712341234123", "NO3712341234A23")
    }

    /**
     * longes IBAN
     */
    object StLuciaIBans {
        val validIbans = listOf("LC19 1234 1234 1234 1234 1234 1234 1234", "LC371234123412341234125632368009")
        val invalidIbans = listOf(
            "19 1234 1234 1234 1234 1234 1234 1234",
            "371234123412341234125632368009",
            "LC37123412341A2341234125632368009"
        )
    }

    /**
     * illegal IBAN
     */
    object IllegalIbans {
        val ibans = listOf(
            "ÄÜ19 1234 1234 1234 1234 1234 1234 1234",
            "ÄÜ371234123412341234125632368009",
            "Aß19 1234 1234 1234 1234 1234 1234 1234",
            "?#19 1234 1234 1234 1234 1234 1234 1234"
        )
    }

}