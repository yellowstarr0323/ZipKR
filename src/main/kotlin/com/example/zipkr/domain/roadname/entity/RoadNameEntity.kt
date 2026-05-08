package com.example.zipkr.domain.roadname.entity

import com.example.zipkr.domain.common.BaseUUIDEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
class RoadNameEntity (

    @Column(columnDefinition = "VARCHAR(40)")
    val cityProvinceName: String,

    @Column(columnDefinition = "VARCHAR(40)")
    val countyDistricts: String,

    @Column(columnDefinition = "VARCHAR(40)")
    val eupMyeonDong: String,

    @Column(columnDefinition = "VARCHAR(40)")
    val Li: String? = null,

    @Column(columnDefinition = "INT")
    val mainJibunNumber: Int,

    @Column(columnDefinition = "INT")
    val subJibunNumber: Int,

    @Column(columnDefinition = "VARCHAR(80)")
    val roadName: String,

    @Column(columnDefinition = "INT")
    val mainBuildingNumber: Int? = null,

    @Column(columnDefinition = "INT")
    val subBuildingNumber: Int? = null,

    @Column(columnDefinition = "VARCHAR(400)")
    val korFullText: String,

    @Column(columnDefinition = "VARCHAR(400)")
    val korInitialFullText: String,

    @Column(columnDefinition = "INT")
    val postalCode: Int,

    @Column(columnDefinition = "VARCHAR(40)")
    val buildingName: String?,

    @Column(columnDefinition = "VARCHAR(26)", nullable = false)
    val managementNumber: String,


) : BaseUUIDEntity()