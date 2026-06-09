package com.example.zipkr.domain.roadname.service

import com.example.zipkr.domain.roadname.entity.RoadNameEntity
import com.example.zipkr.domain.roadname.repository.RoadNameJpaRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.nio.charset.Charset
import java.util.UUID

@Service
class AddressBatchServiceImpl(
    private val jpaRepository: RoadNameJpaRepository
) : AddressBatchService {

    private val log = LoggerFactory.getLogger(AddressBatchServiceImpl::class.java)

    companion object {
        private const val BATCH_SIZE = 1000
        private const val DELETE_CODE = "63"

        private const val COL_MANAGEMENT_NUMBER = 0
        private const val COL_CITY_PROVINCE = 2
        private const val COL_COUNTY_DISTRICT = 3
        private const val COL_EUP_MYEON_DONG = 4
        private const val COL_LI = 5
        private const val COL_MAIN_JIBUN = 7
        private const val COL_SUB_JIBUN = 8
        private const val COL_ROAD_NAME = 10
        private const val COL_MAIN_BUILDING_NUM = 12
        private const val COL_SUB_BUILDING_NUM = 13
        private const val COL_POSTAL_CODE = 16
        private const val COL_CHANGE_CODE = 20
        private const val COL_BUILDING_NAME = 21
        private const val COL_BUILDING_NAME_DETAIL = 22

        private val CHOSUNG_TABLE = listOf(
            "ㄱ","ㄲ","ㄴ","ㄷ","ㄸ","ㄹ","ㅁ","ㅂ","ㅃ",
            "ㅅ","ㅆ","ㅇ","ㅈ","ㅉ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"
        )
    }

    override fun processFile(filePath: String) {
        val start = System.currentTimeMillis()
        val upsertBatch = mutableListOf<RoadNameEntity>()
        val deleteBatch = mutableListOf<String>()

        File(filePath).forEachLine(charset = Charset.forName("cp949")) { line ->
            val fields = line.split("|")

            when (fields[COL_CHANGE_CODE].trim()) {
                DELETE_CODE -> {
                    deleteBatch.add(fields[COL_MANAGEMENT_NUMBER].trim())
                    if (deleteBatch.size == BATCH_SIZE) {
                        jpaRepository.deleteAllByManagementNumberIn(deleteBatch)
                        deleteBatch.clear()
                    }
                }
                else -> {
                    upsertBatch.add(toEntity(fields))
                    if (upsertBatch.size == BATCH_SIZE) {
                        upsert(upsertBatch)
                        upsertBatch.clear()
                    }
                }
            }
        }

        if (upsertBatch.isNotEmpty()) upsert(upsertBatch)
        if (deleteBatch.isNotEmpty()) jpaRepository.deleteAllByManagementNumberIn(deleteBatch)
        File(filePath).delete()
        log.info("파일 처리 완료 [{}]: {}초", filePath, (System.currentTimeMillis() - start) / 1000)
    }

    private fun toEntity(fields: List<String>): RoadNameEntity {
        val korFullText = buildKorFullText(fields)
        return RoadNameEntity(
            _id                = UUID.randomUUID(),
            managementNumber   = fields[COL_MANAGEMENT_NUMBER].trim(),
            cityProvinceName   = fields[COL_CITY_PROVINCE].trim(),
            countyDistricts    = fields[COL_COUNTY_DISTRICT].trim(),
            eupMyeonDong       = fields[COL_EUP_MYEON_DONG].trim(),
            li                 = fields[COL_LI].trim().ifBlank { null },
            mainJibunNumber    = fields[COL_MAIN_JIBUN].trim().toIntOrNull() ?: 0,
            subJibunNumber     = fields[COL_SUB_JIBUN].trim().toIntOrNull() ?: 0,
            roadName           = fields[COL_ROAD_NAME].trim().ifBlank { null },
            mainBuildingNumber = fields[COL_MAIN_BUILDING_NUM].trim().toIntOrNull(),
            subBuildingNumber  = fields[COL_SUB_BUILDING_NUM].trim().toIntOrNull(),
            postalCode         = fields[COL_POSTAL_CODE].trim().toInt(),
            buildingName       = fields.getOrNull(COL_BUILDING_NAME_DETAIL)?.trim()?.ifBlank { null }
                                    ?: fields.getOrNull(COL_BUILDING_NAME)?.trim()?.ifBlank { null },
            korFullText        = korFullText,
            korInitialFullText = extractChosung(korFullText),
        )
    }

    private fun upsert(entities: List<RoadNameEntity>) {
        jpaRepository.deleteAllByManagementNumberIn(entities.map { it.managementNumber })
        jpaRepository.saveAll(entities)
    }

    private fun buildKorFullText(fields: List<String>): String {
        val cityProvince  = fields[COL_CITY_PROVINCE].trim()
        val countyDistrict = fields[COL_COUNTY_DISTRICT].trim()
        val eupMyeonDong  = fields[COL_EUP_MYEON_DONG].trim()
        val li            = fields[COL_LI].trim()
        val roadName      = fields[COL_ROAD_NAME].trim()
        val mainBuilding  = fields[COL_MAIN_BUILDING_NUM].trim()
        val subBuilding   = fields[COL_SUB_BUILDING_NUM].trim()
        val mainJibun     = fields[COL_MAIN_JIBUN].trim()
        val subJibun      = fields[COL_SUB_JIBUN].trim()

        val buildingName = fields.getOrNull(COL_BUILDING_NAME_DETAIL)?.trim()?.ifBlank { null }
            ?: fields.getOrNull(COL_BUILDING_NAME)?.trim()?.ifBlank { null }

        return buildList {
            add(cityProvince)
            if (countyDistrict.isNotBlank()) add(countyDistrict)
            add(eupMyeonDong)
            if (li.isNotBlank()) add(li)
            if (roadName.isNotBlank()) {
                add(roadName)
                add(if (subBuilding != "0" && subBuilding.isNotBlank()) "$mainBuilding-$subBuilding" else mainBuilding)
            } else {
                add(if (subJibun != "0" && subJibun.isNotBlank()) "$mainJibun-$subJibun" else mainJibun)
            }
            if (buildingName != null) add(buildingName)
        }.joinToString(" ")
    }

    private fun extractChosung(text: String): String =
        text.map { ch ->
            val code = ch.code
            if (code in 0xAC00..0xD7A3) CHOSUNG_TABLE[(code - 0xAC00) / (21 * 28)]
            else ch.toString()
        }.joinToString("")
}
