package com.sibela.encapsulatingapireturns

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.IO).launch {
            realWithSuccess()
            realWithFail()

            successErrorTest()
            failErrorTest()
        }
    }

    private suspend fun successErrorTest() {
        val result: Result<BackendException, String> = execute {
            "Leonardo"
        }

        result.success {
            Log.d("API_TEST", "successErrorTest - OK")
        }.whenError {
            Log.d("API_TEST", "successErrorTest - NOK")
        }
    }

    private suspend fun failErrorTest() {
        val result: Result<BackendException, String> = execute {
            throw BackendException("Body", Throwable(), BackendException.Kind.HTTP, 404)
        }

        result.success {
            Log.d("API_TEST", "failErrorTest - NOK")
        }.whenError {
            Log.d("API_TEST", "failErrorTest - OK")
        }
    }

    private suspend fun realWithSuccess() {
        val value = "Leonardo"
        val result: Result<BackendException, String> = execute {
            value
        }

        if (result is Result.Success<String> && result.value == value) {
            Log.d("API_TEST", "realWithSuccess - OK")
        } else {
            Log.d("API_TEST", "realWithSuccess - FAIL")
        }
    }

    private suspend fun realWithFail() {
        val throwable = Throwable()
        val body = "Body"
        val kind = BackendException.Kind.HTTP
        val statusCode = 404
        val result: Result<BackendException, String> = execute {
            throw BackendException(body, throwable, kind, statusCode)
        }

        if (
            result is Result.Error<BackendException>
            && result.value.kind == kind
            && result.value.statusCode == statusCode
            && result.value.message == body
            && result.value.cause == throwable
        ) {
            Log.d("API_TEST", "realWithFail - OK")
        } else {
            Log.d("API_TEST", "realWithFail - FAIL")
        }
    }
}