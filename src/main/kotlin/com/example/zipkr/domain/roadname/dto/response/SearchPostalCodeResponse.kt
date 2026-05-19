package com.example.zipkr.domain.roadname.dto.response

import com.example.zipkr.domain.roadname.entity.RoadNameEntity

data class SearchPostalCodeResponse(
    val postalCode: Int,
    val roadNameAddress: String,
    val jibunAddress: String
) {
    companion object {
        fun from(entity: RoadNameEntity) = SearchPostalCodeResponse(
            postalCode = entity.postalCode,
            roadNameAddress = buildRoadNameAddress(entity),
            jibunAddress = buildJibunAddress(entity)
        )

        private fun buildRoadNameAddress(entity: RoadNameEntity): String {
            val road = entity.roadName ?: return buildJibunAddress(entity)
            val buildingNum = entity.mainBuildingNumber ?: return buildJibunAddress(entity)
            val sub = if ((entity.subBuildingNumber ?: 0) != 0) "-${entity.subBuildingNumber}" else ""
            val buildingName = if (entity.buildingName?.isNotBlank() == true) "(${entity.buildingName})" else ""
            return "${entity.cityProvinceName} ${entity.countyDistricts} $road $buildingNum$sub$buildingName"
        }

        private fun buildJibunAddress(entity: RoadNameEntity): String {
            val li = if (entity.li?.isNotBlank() == true) " ${entity.li}" else ""
            val sub = if (entity.subJibunNumber != 0) "-${entity.subJibunNumber}" else ""
            val buildingName = if (entity.buildingName?.isNotBlank() == true) "(${entity.buildingName})" else ""
            return "${entity.cityProvinceName} ${entity.countyDistricts} ${entity.eupMyeonDong}$li ${entity.mainJibunNumber}$sub$buildingName"
        }
    }
}