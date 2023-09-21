package com.atnerix.alyx.api.runtime

import com.atnerix.alyx.api.exception.AlyxFileNotFoundException
import java.io.InputStream

object ResourceHelper {
    @JvmStatic
    fun loadIOFromClasspath(fileName: String): InputStream {
        val clazz = this::class.java
        var io = clazz.getResourceAsStream(fileName)

        if (io.isNull()) {
            io = clazz.getResourceAsStream(fileName)
        }

        if (io.isNull()) {
            io = ClassLoader.getSystemResourceAsStream(fileName)
        }

        if (io.isNull()) {
            throw AlyxFileNotFoundException("Enable to found file with name: $fileName")
        }

        return io
    }
}