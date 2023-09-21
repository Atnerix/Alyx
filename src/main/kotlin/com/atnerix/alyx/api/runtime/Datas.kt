package com.atnerix.alyx.api.runtime

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.sql.Date
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class Settings(
    val token: String,
    val activity: String?,
    val activityType: String?
)

@Serializable
data class WarnedArray(
    val muted: MutableList<WarnedEntity>
)

@Serializable
data class WarnedEntity(
    val memberId: String,
    val reason: String = "Reason not provided",
    @Contextual
    val dateAdded: LocalDate = LocalDate.now(),
    @Contextual
    val timeAddedWarn: LocalTime = LocalTime.now(),
    @Contextual
    val dateOut: LocalDate? = null,
    @Contextual
    val timeOut: LocalTime? = null
)