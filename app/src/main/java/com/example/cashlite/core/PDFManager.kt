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

    /** Возвращает true, если хотя бы одна транзакция была добавлена */
    suspend fun importPdf(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        Log.e("LOG_TESTING", "=== НАЧАЛО ИМПОРТА PDF ===")
        Log.e("LOG_TESTING", "URI: $uri")

        val text = readPdfText(uri)
        if (text.isBlank()) {
            Log.e("LOG_TESTING", "ОШИБКА: PDF текст пустой после извлечения!")
            return@withContext false
        }

        val parsedList = parseBankStatement(text)
        Log.e("LOG_TESTING", "Парсинг завершён. Найдено сырых транзакций: ${parsedList.size}")

        if (parsedList.isEmpty()) {
            Log.e("LOG_TESTING", "ОШИБКА: Ни одной транзакции не распарсено!")
            return@withContext false
        }

        val mapper = TransactionImportMapper()
        val entities = mutableListOf<TransactionEntity>()

        for (parsed in parsedList) {
            val entity = mapper.mapToEntity(parsed)
            if (entity != null) {
                entities.add(entity)
                Log.e("LOG_TESTING", "✅ Маппинг успешен: ${parsed.note} | ${parsed.amount}")
            } else {
                Log.e("LOG_TESTING", "ПРОПУЩЕНО: категория не найдена для '${parsed.note}'")
            }
        }

        if (entities.isNotEmpty()) {
            AppRepository.insertImportedTransactions(entities)
            Log.e("LOG_TESTING", "✅ УСПЕШНО ЗАПИСАНО В БАЗУ: ${entities.size} транзакций")
            Log.e("LOG_TESTING", "=== ИМПОРТ ЗАВЕРШЁН УСПЕШНО ===")
            return@withContext true
        }

        Log.e("LOG_TESTING", "ОШИБКА: нет сущностей для записи")
        return@withContext false
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
            Log.e("LOG_TESTING", "PDFTextStripper.getText → получено ${text.length} символов")
            text
        } catch (e: Throwable) {
            Log.e("LOG_TESTING", "ОШИБКА чтения PDF: ${e.javaClass.simpleName} → ${e.message}")
            e.printStackTrace()
            ""
        } finally {
            document?.close()
            inputStream?.close()
        }
    }

    private fun parseBankStatement(text: String): List<ParseBankTransaction> {
        val result = mutableListOf<ParseBankTransaction>()

        // Более гибкий regex специально под твою выписку
        val regex = Regex(
            """(\d{2}\.\d{2}\.\d{4})\s+\d{2}\.\d{2}\.\d{4}\s+([+-]?\s*[\d\s]+[.,]\d{2})\s+₽\s+[+-]?\s*[\d\s]+[.,]\d{2}\s+₽\s+(.+)"""
        )

        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        val skipKeywords = listOf(
            "итого", "баланс", "остаток", "входящий", "исходящий",
            "поступление", "списание", "комиссия", "курс", "всего",
            "операции", "выписка", "период", "клиент", "движение",
            "средств", "дата и время", "сумма в валюте", "номер карты"
        )

        text.lines().forEach { originalLine ->
            val line = originalLine.trim()
            if (line.isBlank()) return@forEach

            // Пропускаем шапки и итоги
            if (skipKeywords.any { line.lowercase().contains(it) }) {
                Log.e("LOG_TESTING", "ПРОПУЩЕНА (skipKeyword): $line")
                return@forEach
            }

            val match = regex.find(line) ?: return@forEach

            val dateStr = match.groupValues[1]
            val amountRaw = match.groupValues[2]
            val description = match.groupValues[3].trim()
                .replace(" ", "")
                .replace(",", ".")

            Log.e("LOG_TESTING", "✅ КОРРЕКТНЫЙ МАТЧ → дата=$dateStr | сумма=$amountRaw | описание='$description' | строка: '$line'")

            val date = try {
                dateFormat.parse(dateStr)?.time ?: return@forEach
            } catch (e: Exception) {
                Log.e("LOG_TESTING", "Ошибка парсинга даты: $dateStr")
                return@forEach
            }

            val amount = amountRaw.toDoubleOrNull() ?: run {
                Log.e("LOG_TESTING", "Ошибка конвертации суммы: $amountRaw")
                return@forEach
            }

            result.add(ParseBankTransaction(date = date, amount = amount, note = description))
        }

        return result
    }
}