package com.example.zipkr.domain.roadname.repository

import com.example.zipkr.domain.roadname.entity.RoadNameEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

interface RoadNameJpaRepository : JpaRepository<RoadNameEntity, UUID> {

    @Query(
        nativeQuery = true,
        value = "SELECT * FROM road_name_entity WHERE MATCH(kor_full_text) AGAINST (:keyword IN BOOLEAN MODE) LIMIT :limit OFFSET :offset"
    )
    fun searchByText(
        @Param("keyword") keyword: String,
        @Param("limit") limit: Int,
        @Param("offset") offset: Int,
    ): List<RoadNameEntity>

    @Query(
        nativeQuery = true,
        value = "SELECT * FROM road_name_entity WHERE MATCH(kor_initial_full_text) AGAINST (:keyword IN BOOLEAN MODE) LIMIT :limit OFFSET :offset"
    )
    fun searchByInitialConsonant(
        @Param("keyword") keyword: String,
        @Param("limit") limit: Int,
        @Param("offset") offset: Int,
    ): List<RoadNameEntity>

    @Modifying
    @Transactional
    @Query("DELETE FROM RoadNameEntity r WHERE r.managementNumber IN :managementNumbers")
    fun deleteAllByManagementNumberIn(@Param("managementNumbers") managementNumbers: List<String>)
}
