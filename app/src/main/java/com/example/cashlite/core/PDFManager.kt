package com.example.cashlite.core

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.cashlite.data.dataclass.ParseBankTransaction
import com.example.cashlite.data.mapper.TransactionImportMapper
import com.example.cashlite.data.repository.AppRepository
import com.example.cashlite.data.room.transaction.TransactionEntity
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale

class PDFManager(private val context: Context) {

    suspend fun importPdf(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        Log.e("LOG_TESTING", "=== НАЧАЛО ИМПОРТА PDF ===")
        Log.e("LOG_TESTING", "URI: $uri")

        val text = readPdfText(uri)
        if (text.isBlank()) {
            Log.e("LOG_TESTING", "ОШИБКА: PDF текст пустой!")
            return@withContext false
        }

        val parsedList = parseBankStatement(text)
        Log.e("LOG_TESTING", "Найдено транзакций: ${parsedList.size}")

        if (parsedList.isEmpty()) return@withContext false

        val mapper = TransactionImportMapper()
        val entities = mutableListOf<TransactionEntity>()

        for (parsed in parsedList) {
            val entity = mapper.mapToEntity(parsed)
            if (entity != null) {
                entities.add(entity)
                Log.e("LOG_TESTING", "✅ ${parsed.note} | ${parsed.amount}")
            } else {
                Log.e("LOG_TESTING", "❌ Нет категории: ${parsed.note}")
            }
        }

        if (entities.isNotEmpty()) {
            AppRepository.insertImportedTransactions(entities)
            Log.e("LOG_TESTING", "✅ Сохранено: ${entities.size}")
            return@withContext true
        }

        Log.e("LOG_TESTING", "❌ Нет данных для записи")
        false
    }

    private fun readPdfText(uri: Uri): String {
        var inputStream: InputStream? = null
        var document: PDDocument? = null

        return try {
            inputStream = context.contentResolver.openInputStream(uri)
            document = PDDocument.load(inputStream)

            val stripper = PDFTextStripper().apply {
                sortByPosition = true
            }

            val text = stripper.getText(document)
            Log.e("LOG_TESTING", "PDF → ${text.length} символов")
            text

        } catch (e: Throwable) {
            Log.e("LOG_TESTING", "ОШИБКА PDF: ${e.message}")
            ""
        } finally {
            document?.close()
            inputStream?.close()
        }
    }

    private fun mergeLines(text: String): List<String> {
        val mergedLines = mutableListOf<String>()
        var currentLine = StringBuilder()

        val dateRegex = Regex("""^\d{2}\.\d{2}\.\d{4}""")

        text.lines().forEach { raw ->
            val line = raw.trim()
            if (line.isBlank()) return@forEach

            if (dateRegex.containsMatchIn(line)) {
                if (currentLine.isNotEmpty()) {
                    mergedLines.add(currentLine.toString())
                }
                currentLine = StringBuilder(line)
            } else {
                currentLine.append(" ").append(line)
            }
        }

        if (currentLine.isNotEmpty()) {
            mergedLines.add(currentLine.toString())
        }

        return mergedLines
    }

    private fun parseBankStatement(text: String): List<ParseBankTransaction> {
        val result = mutableListOf<ParseBankTransaction>()

        val regex = Regex(
            """(\d{2}\.\d{2}\.\d{4})\s+\d{2}\.\d{2}\.\d{4}\s+([+-]?\s*[\d\s]+[.,]\d{2})\s+₽\s+[+-]?\s*[\d\s]+[.,]\d{2}\s+₽\s+(.+)"""
        )

        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        val skipKeywords = listOf(
            "итого", "баланс", "остаток", "входящий", "исходящий",
            "поступление", "списание", "комиссия", "курс", "всего",
            "операции", "выписка", "период", "клиент", "движение",
            "средств", "дата и время", "сумма в валюте", "номер карты", "внутренний перевод",
            "внутрибанковский перевод", "закрытие вклада", "пополнение", "пополнения", "расходы",
            "с карты другого банка", "с карты на карту"
        )

        val lines = mergeLines(text)

        lines.forEach { originalLine ->

            val line = originalLine
                .replace(Regex("\\s+"), " ")
                .trim()

            if (line.isBlank()) return@forEach

            if (skipKeywords.any { line.lowercase().contains(it) }) {
                Log.e("LOG_TESTING", "⛔ skip: $line")
                return@forEach
            }

            Log.e("LOG_TESTING", "📄 LINE: $line")

            val match = regex.find(line) ?: return@forEach

            val dateStr = match.groupValues[1]
            val amountRaw = match.groupValues[2]
                .replace(" ", "")
                .replace(",", ".")
            val description = match.groupValues[3].trim()

            Log.e("LOG_TESTING", "✅ MATCH → $description | $amountRaw")

            val date = try {
                dateFormat.parse(dateStr)?.time ?: return@forEach
            } catch (e: Exception) {
                Log.e("LOG_TESTING", "❌ date parse: $dateStr")
                return@forEach
            }

            val amount = amountRaw.toDoubleOrNull() ?: return@forEach

            result.add(
                ParseBankTransaction(
                    date = date,
                    amount = amount,
                    note = description
                )
            )
        }

        return result
    }
}