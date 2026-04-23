package com.example.cashlite.core.manager

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.cashlite.data.dataclass.parse.ParseBankTransaction
import com.example.cashlite.data.local.TransactionNameNormalizer
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

        val text = readPdfText(uri)
        if (text.isBlank()) return@withContext false

        val parsedList = parseBankStatement(text)

        if (parsedList.isEmpty()) return@withContext false

        val mapper = TransactionImportMapper()
        val entities = mutableListOf<TransactionEntity>()

        for (parsed in parsedList) {
            val entity = mapper.mapToEntity(parsed)
            if (entity != null) {
                entities.add(entity)
            } else {
                Log.e("LOG_TESTING", "Нет категории: ${parsed.displayNote}")
            }
        }

        if (entities.isNotEmpty()) {
            AppRepository.insertImportedTransactions(entities)
            return@withContext true
        }

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
            """(\d{2}\.\d{2}\.\d{4})\s+""" +
                    """\d{2}\.\d{2}\.\d{4}\s+""" +
                    """([+-]?\s*[\d\s]+[.,]\d{2})\s+₽\s+""" +
                    """[+-]?\s*[\d\s]+[.,]\d{2}\s+₽\s+""" +
                    """(.+)"""
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

            if (skipKeywords.any { line.lowercase().contains(it) }) return@forEach

            val match = regex.find(line) ?: return@forEach

            val dateStr = match.groupValues[1]
            val amountRaw = match.groupValues[2]
                .replace(" ", "")
                .replace(",", ".")
            val raw = match.groupValues[3].trim()
            val display = TransactionNameNormalizer.normalize(raw)

            Log.e("LOG_TESTING", "MATCH → $display | $amountRaw")

            val date = try {
                dateFormat.parse(dateStr)?.time ?: return@forEach
            } catch (e: Exception) {
                return@forEach
            }

            val amount = amountRaw.toDoubleOrNull() ?: return@forEach

            result.add(
                ParseBankTransaction(
                    date = date,
                    amount = amount,
                    rawNote = raw,
                    displayNote = display,
                )
            )
        }

        return result
    }
}