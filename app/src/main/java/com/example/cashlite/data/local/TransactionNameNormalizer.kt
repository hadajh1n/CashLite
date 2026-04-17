package com.example.cashlite.data.local

object TransactionNameNormalizer {

    private val replacements = listOf(

        "krasnoe&beloe" to "Красное & Белое",
        "magnit" to "Магнит",
        "pyaterochka" to "Пятёрочка",
        "пятерочка" to "Пятёрочка",
        "lenta" to "Лента",
        "spar" to "SPAR",
        "fixprice" to "Fix Price",
        "dixy" to "Дикси",
        "дикси" to "Дикси",
        "auchan" to "Ашан",
        "ашан" to "Ашан",
        "perekrestok" to "Перекрёсток",
        "перекресток" to "Перекрёсток",
        "verny" to "Верный",
        "верный" to "Верный",

        "kfc" to "KFC",
        "mcdonald" to "McDonald's",
        "вкусно и точка" to "Вкусно — и точка",
        "burger" to "Burger King",
        "dodo" to "Додо Пицца",
        "pizza" to "Пицца",
        "sushi" to "Суши",
        "coffee like" to "Coffee Like",
        "starbucks" to "Starbucks",
        "шоколадница" to "Шоколадница",

        "zara" to "ZARA",
        "hm" to "H&M",
        "h&m" to "H&M",
        "bershka" to "Bershka",
        "pull&bear" to "Pull & Bear",
        "reserved" to "Reserved",
        "ostin" to "O'STIN",
        "uniqlo" to "UNIQLO",
        "kari" to "Kari",
        "sunlight" to "Sunlight",

        "gazprom" to "Газпром",
        "лукойл" to "Лукойл",
        "lukoil" to "Лукойл",
        "rosneft" to "Роснефть",
        "shell" to "Shell",

        "yandex go" to "Яндекс Go",
        "taxi" to "Такси",
        "taximaxim" to "Максим",

        "tele2" to "Tele2",
        "t2" to "Tele2",
        "megafon" to "МегаФон",
        "mts" to "МТС",
        "beeline" to "Билайн",
        "yota" to "YOTA",
        "netflix" to "Netflix",
        "spotify" to "Spotify",
        "youtube" to "YouTube",

        "rigla" to "Ригла",
        "36.6" to "Аптека 36.6",
        "здравсити" to "Здравсити",

        "wildberries" to "Wildberries",
        "ozon" to "Ozon",

        "detskiy mir" to "Детский мир",
        "детский мир" to "Детский мир",

        "4 лапы" to "Четыре Лапы",
        "four paws" to "Четыре Лапы",

        "salary" to "Зарплата",
        "cashback" to "Кэшбэк",
        "кэшбэк" to "Кэшбэк",
        "dividend" to "Дивиденды",
        "bonus" to "Бонус",

        "transfer" to "Перевод",
        "перевод" to "Перевод"
    )

    fun normalize(raw: String): String {
        val cleaned = raw
            .lowercase()
            .replace(Regex("[^a-zа-я0-9& ]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()

        return replacements.firstOrNull { cleaned.contains(it.first) }?.second
            ?: prettify(cleaned)
    }

    private fun prettify(text: String): String {
        return text
            .split(" ")
            .joinToString(" ") {
                it.replaceFirstChar { c -> c.uppercase() }
            }
    }
}