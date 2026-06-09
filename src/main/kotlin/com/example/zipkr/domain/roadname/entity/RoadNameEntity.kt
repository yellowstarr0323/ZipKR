package com.example.zipkr.domain.roadname.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.PostLoad
import jakarta.persistence.Table
import jakarta.persistence.Transient
import org.springframework.data.domain.Persistable
import java.util.UUID

@Entity
@Table(
    name = "road_name_entity",
    indexes = [Index(name = "idx_management_number", columnList = "management_number")]
)
class RoadNameEntity(

    // JVM에서 메서드를 자동생성해서 _id로 이름을 지었음
    @Id
    @Column(name = "id")
    private val _id: UUID,

    @Column(columnDefinition = "VARCHAR(40)")
    val cityProvinceName: String,

    @Column(columnDefinition = "VARCHAR(40)")
    val countyDistricts: String,

    @Column(columnDefinition = "VARCHAR(40)")
    val eupMyeonDong: String,

    @Column(columnDefinition = "VARCHAR(40)")
    val li: String? = null,

    @Column(columnDefinition = "INT")
    val mainJibunNumber: Int,

    @Column(columnDefinition = "INT")
    val subJibunNumber: Int,

    @Column(columnDefinition = "VARCHAR(80)")
    val roadName: String? = null,

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

    @Column(columnDefinition = "VARCHAR(400)")
    val buildingName: String?,

    @Column(columnDefinition = "VARCHAR(26)", nullable = false)
    val managementNumber: String,

) : Persistable<UUID> {

    @Transient
    private var _isNew: Boolean = true

    override fun getId(): UUID = _id

    override fun isNew(): Boolean = _isNew

    @PostLoad
    fun markNotNew() {
        _isNew = false
    }
}
