package com.sibela.encapsulatingapireturns

internal sealed class Result<out E, out V> {
    internal data class Success<out V>(val value: V) : Result<Nothing, V>()
    internal data class Error<out E>(val value: E) : Result<E, Nothing>()

    companion object Factory {
        inline fun <V> build(func: () -> V): Result<BackendException, V> =
            try {
                Success(func.invoke())
            } catch (e: BackendException) {
                Error(e)
            }
    }
}

class BackendException(
    body: String,
    throwable: Throwable,
    val kind: Kind,
    val statusCode: Int,
) : RuntimeException(body, throwable) {

    enum class Kind {
        NETWORK,
        CONVERSION,
        HTTP,
        UNEXPECTED,
        ROUTER,
        TOKEN,
        SESSION,
        SIMULTANEOUS_SESSION,
        VERSION_CONTROL
    }
}

class ErrorDTO {
    lateinit var code: String
    lateinit var message: String
}