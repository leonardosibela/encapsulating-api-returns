package com.sibela.encapsulatingapireturns

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal suspend fun <T> execute(block: suspend () -> T): Result<BackendException, T> {
    return withContext(Dispatchers.IO) {
        Result.build {
            block()
        }
    }
}