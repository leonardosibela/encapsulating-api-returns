package com.sibela.encapsulatingapireturns

internal suspend fun <E, T> Result<E, T>.success(scope: suspend () -> Unit): Result<E, T> {
    scope.takeIf { this is Result.Success }?.invoke()
    return this
}


internal suspend fun <E, T> Result<E, T>.whenError(scope: suspend () -> Unit) {
    scope.takeIf { this is Result.Error }?.invoke()
}