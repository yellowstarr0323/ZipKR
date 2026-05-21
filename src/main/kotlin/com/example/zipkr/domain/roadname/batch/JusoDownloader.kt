package com.example.zipkr.domain.roadname.batch

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.zip.ZipFile

@Component
class JusoDownloader(
    @Value("\${juso.api.base-url}") private val baseUrl: String
) {

    companion object {
        private const val DOWNLOAD_DIR = "downloads"

        private val BASE_DATE = LocalDate.of(2026, 5, 18)
        private const val BASE_INT_NUM = 1448L
        private const val BASE_INT_FILE_NO = 147840L
        private const val INT_FILE_NO_INCREMENT_PER_DAY = 15L

        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd")
        private val YEAR_FORMATTER = DateTimeFormatter.ofPattern("yyyy")
    }

    fun download(date: LocalDate): String {
        val fileName = "${date.format(DATE_FORMATTER)}_dailyjusukrdata.zip"
        val savePath = "$DOWNLOAD_DIR/$fileName"

        File(DOWNLOAD_DIR).mkdirs()
        URL(buildDownloadUrl(date, fileName)).openStream().use { input ->
            File(savePath).outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return savePath
    }

    fun unzip(zipPath: String): List<String> {
        val destDir = zipPath.substringBeforeLast("/")

        return ZipFile(zipPath).use { zip ->
            zip.entries().asSequence()
                .filter { it.name.contains("MST") }
                .map { entry ->
                    val outFile = File("$destDir/${entry.name}")
                    zip.getInputStream(entry).use { input ->
                        outFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    outFile.absolutePath
                }
                .toList()
        }.also { File(zipPath).delete() }
    }

    private fun buildDownloadUrl(date: LocalDate, fileName: String): String {
        val daysDiff = ChronoUnit.DAYS.between(BASE_DATE, date)
        val intNum = BASE_INT_NUM + daysDiff
        val intFileNo = BASE_INT_FILE_NO + (daysDiff * INT_FILE_NO_INCREMENT_PER_DAY)

        return "$baseUrl" +
            "?regYmd=${date.format(YEAR_FORMATTER)}" +
            "&reqType=JUSUKRDAY" +
            "&stdde=${date.format(DATE_FORMATTER)}" +
            "&fileName=$fileName" +
            "&realFileName=$fileName" +
            "&intFileNo=$intFileNo" +
            "&intNum=$intNum"
    }
}