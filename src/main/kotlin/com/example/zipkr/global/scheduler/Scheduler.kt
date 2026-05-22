package com.example.zipkr.global.scheduler

import com.example.zipkr.domain.roadname.batch.JusoDownloader
import com.example.zipkr.domain.roadname.service.AddressBatchService
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
@EnableScheduling
class AddressScheduler(
    private val jusoDownloader: JusoDownloader,
    private val addressBatchService: AddressBatchService,
) {
    @Scheduled(cron = "0 0 2 * * *")  // 매일 새벽 2시
    fun run() {
        val yesterday = LocalDate.now().minusDays(1)

        try {
            val zipPath = jusoDownloader.download(yesterday)
            val filePath = jusoDownloader.unzip(zipPath)
            addressBatchService.processFile(filePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}