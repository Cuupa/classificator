package com.cuupa.classificator.engine.services.dataExtraction

object PhoneNumberTestData {

    val validPhoneNumbers = listOf(GermanPhoneNumber.validNumbers).flatten()
    val invalidPhoneNumbers = listOf(GermanPhoneNumber.invalidNumbers).flatten()

    object GermanPhoneNumber {
        private val validNumbersInternational = listOf(
            "+49301234567",
            // DIN 5008
            "+4930 12345-67",
            // E.123
            "(+4930) 12345 67",
            // RFC 3966 ... without tel:
            "+49-30-1234567"
        )

        private val validNumbersLocal = listOf(
            "0301234567",
            // DIN 5008
            "030 12345-67",
            // E.123
            "(030) 12345 67"
        )

        val validNumbers = listOf(
            validNumbersInternational,
            validNumbersLocal
        ).flatten()

        private val invalidNumbersInternational = listOf(
            "49301234567",
            "4930 12345-67",
            "(4930) 12345 67",
            "49-30-1234567",
            "049301234567",
            "04930 12345-67",
            "(04930) 12345 67",
            "049-30-1234567"
        )

        private val invalidNumbersLocal = listOf(
            "0301234567",
            // DIN 5008
            "030 12345-67",
            // E.123
            "(030) 12345 67"
        )

        val invalidNumbers = listOf(
            invalidNumbersInternational,
            invalidNumbersLocal
        ).flatten()
    }
}
