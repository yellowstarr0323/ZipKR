package com.example.zipkr.domain.roadname.repository

import com.example.zipkr.domain.roadname.dto.response.SearchPostalCodeResponse
import com.example.zipkr.domain.roadname.entity.QRoadNameEntity.roadNameEntity
import com.example.zipkr.domain.roadname.entity.RoadNameEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class MysqlRoadNameRepository(
    private val queryFactory: JPAQueryFactory
) : RoadNameRepository {

    override fun searchPostalCode(keyword: String): List<SearchPostalCodeResponse> {
        return queryFactory
            .selectFrom(roadNameEntity)
            .where(
                roadNameEntity.korFullText.contains(keyword)
                    .or(roadNameEntity.korInitialFullText.contains(keyword))
            )
            .fetch()
            .map { toResponse(it) }
    }

    private fun toResponse(entity: RoadNameEntity) = SearchPostalCodeResponse(
        postalCode = entity.postalCode,
        roadNameAddress = buildRoadNameAddress(entity),
        jibunAddress = buildJibunAddress(entity)
    )

    private fun buildRoadNameAddress(entity: RoadNameEntity): String {
        val road = entity.roadName ?: return buildJibunAddress(entity)
        val buildingNum = entity.mainBuildingNumber ?: return buildJibunAddress(entity)
        val sub = if ((entity.subBuildingNumber ?: 0) != 0) "-${entity.subBuildingNumber}" else ""
        val buildingName = if(entity.buildingName?.isNotBlank() == true) "(${entity.buildingName})" else ""
        return "${entity.cityProvinceName} ${entity.countyDistricts} $road $buildingNum$sub$buildingName"
    }

    private fun buildJibunAddress(entity: RoadNameEntity): String {
        val li = if (entity.li?.isNotBlank() == true) " ${entity.li}" else ""
        val sub = if (entity.subJibunNumber != 0) "-${entity.subJibunNumber}" else ""
        val buildingName = if(entity.buildingName?.isNotBlank() == true) "(${entity.buildingName})" else ""
        return "${entity.cityProvinceName} ${entity.countyDistricts} ${entity.eupMyeonDong}$li ${entity.mainJibunNumber}$sub$buildingName"
    }
}
