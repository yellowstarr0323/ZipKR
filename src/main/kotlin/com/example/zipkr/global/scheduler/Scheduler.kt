package com.example.zipkr.global.scheduler

import com.example.zipkr.domain.roadname.batch.JusoDownloader
import com.example.zipkr.domain.roadname.service.AddressBatchService
import org.slf4j.LoggerFactory
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
    private val log = LoggerFactory.getLogger(AddressScheduler::class.java)

    @Scheduled(cron = "0 0 2 * * *")  // 매일 새벽 2시
    fun run() {
        val yesterday = LocalDate.now().minusDays(1)
        val start = System.currentTimeMillis()

        try {
            val zipPath = jusoDownloader.download(yesterday)
            val filePath = jusoDownloader.unzip(zipPath)
            addressBatchService.processFile(filePath)
            log.info("배치 작업 완료: {}초", (System.currentTimeMillis() - start) / 1000)
        } catch (e: Exception) {
            log.error("배치 작업 실패: {}초 경과 후 오류 발생", (System.currentTimeMillis() - start) / 1000, e)
        }
    }
}