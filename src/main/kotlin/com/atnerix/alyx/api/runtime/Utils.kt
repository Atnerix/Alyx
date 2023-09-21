package com.atnerix.alyx.api.runtime

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

fun Any?.isNull(): Boolean = this == null

fun Any?.isNotNull(): Boolean = !this.isNull()

@OptIn(ExperimentalSerializationApi::class)
fun <T> Json.readRuntimeJson(serial: DeserializationStrategy<T>, fileName: String): T =
    this.decodeFromStream(serial, ResourceHelper.loadIOFromClasspath("${fileName}.json"))
