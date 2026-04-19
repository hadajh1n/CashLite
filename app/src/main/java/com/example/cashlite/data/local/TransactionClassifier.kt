package com.example.cashlite.data.local

import com.example.cashlite.data.dataclass.ParseBankTransaction

object TransactionClassifier {

    private val supermarketKeywords = listOf(
        "krasnoe", "magni", "пятерочка", "pyaterochka", "lenta",
        "spar", "fixprice", "дикси", "dixy", "верный", "auchan",
        "ашан", "перекресток", "perekrestok", "optovichok", "zajdi", "m 1", "evropa",
        "ch57008"
    )

    private val foodKeywords = listOf(
        "burger", "kfc", "mcdonald", "вкусно и точка", "restaurant",
        "кафе", "kafe", "coffee", "starbucks", "шоколадница", "coffee like",
        "додо", "dodo", "pizza", "роллы", "sushi", "bar", "pub", "kofe",
        "farfor", "rizzachile", "picca chili", "Babl Ti Dva", "nastoyashhaya pekarnya"
    )

    private val clothesKeywords = listOf(
        "kari", "zara", "hm", "h&m", "bershka", "pull&bear",
        "reserved", "gloria jeans", "ostin", "uniqlo", "sunlight"
    )

    private val carKeywords = listOf(
        "gazprom", "газпром", "lukoil", "лукойл",
        "rosneft", "роснефть", "tatneft", "shell",
        "заправка", "fuel"
    )

    private val busKeywords = listOf(
        "ric_orel", "transport", "metro", "метро",
        "bus", "автобус", "trolley", "tram",
        "проезд", "подорожник", "transp", "tpp", "sbertroika"
    )

    private val bicycleKeywords = listOf(
        "sportmaster", "спортмастер", "decathlon",
        "fitness", "gym", "велосипед", "bike"
    )

    private val housingKeywords = listOf(
        "жкх", "kvartplata", "коммунал", "electricity",
        "water", "газ", "тепло", "ук", "housing"
    )

    private val marketplaceKeywords = listOf(
        "wildberries", "ozon",
    )

    private val taxiKeywords = listOf(
        "taxi", "Яндекс Go", "taximaxim"
    )

    private val educationKeywords = listOf(
        "канц", "office", "book", "labirint",
        "школа", "университет", "курсы", "udemy"
    )

    private val flagKeywords = listOf(
        "avia", "airlines", "aeroflot", "hotel",
        "booking", "trip", "travel", "отель",
        "путешествие", "тур"
    )

    private val electronicsKeywords = listOf(
        "eldorado", "dns", "багира", "ситилинк"
    )

    private val phoneKeywords = listOf(
        "t2", "tele2", "megafon", "мегафон", "mts",
        "мтс", "lgs", "beeline", "билайн", "yota",
        "apple", "google", "subscription", "netflix",
        "spotify", "youtube", "vk"
    )

    private val pharmacyKeywords = listOf(
        "аптека", "apteka", "rigla", "36.6",
        "горздрав", "здравсити", "melzdrav"
    )

    private val babyKeywords = listOf(
        "детский мир", "detskiy mir", "kids",
        "baby", "mothercare"
    )

    private val catKeywords = listOf(
        "zoo", "pet", "зоомагазин", "4 лапы",
        "four paws", "ветеринар"
    )

    private val walletKeywords = listOf(
        "зарплата", "salary", "payroll", "income",
    )

    private val cashbackKeywords = listOf(
        "cashback", "кэшбэк"
    )

    private val graphKeywords = listOf(
        "дивиденды", "dividend", "investment",
        "broker", "тинькофф инвестиции", "vtb invest"
    )

    private val awardKeywords = listOf(
        "bonus", "reward", "награда", "бонус"
    )

    private val transferKeywords = listOf(
        "перевод", "внешний перевод", "transfer",
        "card to card", "по номеру карты", "edadeal"
    )

    fun classify(parsed: ParseBankTransaction): String {
        val note = parsed.rawNote.lowercase()
        val isExpense = parsed.amount < 0

        return when {

            containsAny(note, supermarketKeywords) -> CategoryKeys.EXPENSE_SUPERMARKET
            containsAny(note, foodKeywords) -> CategoryKeys.EXPENSE_FOOD
            containsAny(note, clothesKeywords) -> CategoryKeys.EXPENSE_CLOTHES
            containsAny(note, carKeywords) -> CategoryKeys.EXPENSE_CAR
            containsAny(note, busKeywords) -> CategoryKeys.EXPENSE_BUS
            containsAny(note, bicycleKeywords) -> CategoryKeys.EXPENSE_BICYCLE
            containsAny(note, housingKeywords) -> CategoryKeys.EXPENSE_HOUSING
            containsAny(note, marketplaceKeywords) -> CategoryKeys.EXPENSE_MARKETPLACE
            containsAny(note, taxiKeywords) -> CategoryKeys.EXPENSE_TAXI
            containsAny(note, educationKeywords) -> CategoryKeys.EXPENSE_EDUCATION
            containsAny(note, flagKeywords) -> CategoryKeys.EXPENSE_FLAG
            containsAny(note, electronicsKeywords) -> CategoryKeys.EXPENSE_ELECTRONICS
            containsAny(note, phoneKeywords) -> CategoryKeys.EXPENSE_PHONE
            containsAny(note, pharmacyKeywords) -> CategoryKeys.EXPENSE_PHARMACY
            containsAny(note, babyKeywords) -> CategoryKeys.EXPENSE_BABY
            containsAny(note, catKeywords) -> CategoryKeys.EXPENSE_CAT

            containsAny(note, walletKeywords) -> CategoryKeys.INCOME_WALLET
            containsAny(note, cashbackKeywords) -> CategoryKeys.INCOME_CASHBACK
            containsAny(note, graphKeywords) -> CategoryKeys.INCOME_GRAPH
            containsAny(note, awardKeywords) -> CategoryKeys.INCOME_AWARD

            containsAny(note, transferKeywords) -> {
                if (isExpense) CategoryKeys.TRANSFER_EXPENSE
                else CategoryKeys.TRANSFER_INCOME
            }

            else -> {
                if (isExpense) CategoryKeys.UNKNOWN_EXPENSE
                else CategoryKeys.UNKNOWN_INCOME
            }
        }
    }

    private fun containsAny(text: String, keywords: List<String>): Boolean {
        return keywords.any { text.contains(it) }
    }
}