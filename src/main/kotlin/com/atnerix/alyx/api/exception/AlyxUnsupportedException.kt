package com.atnerix.alyx.api.exception

import java.io.Serial

class AlyxUnsupportedException: AlyxRuntimeException {
    constructor(): super()
    constructor(message: String): super(message)
    constructor(message: String, cause: Throwable): super(message, cause)
    constructor(cause: Throwable): super(cause)
    constructor(message: String, cause: Throwable, enableSuppression: Boolean, writableStackTrace: Boolean):
            super(message, cause, enableSuppression, writableStackTrace)

    companion object {
        @Serial
        private const val serialVersionUID = -31643L
    }
}