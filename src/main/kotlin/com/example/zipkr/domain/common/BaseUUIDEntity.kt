package com.example.zipkr.domain.common

import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.springframework.data.domain.Persistable
import java.util.UUID

@MappedSuperclass
abstract class BaseUUIDEntity(
    @field:Id
    @get:JvmName("getIdentifier")
    open var id: UUID = defaultUUID
) : Persistable<UUID> {

    override fun getId() = id

    override fun isNew() =
        (id == defaultUUID).apply { if (this) id = UUID.randomUUID() }

    companion object {
        private val defaultUUID = UUID(0, 0)
    }
}