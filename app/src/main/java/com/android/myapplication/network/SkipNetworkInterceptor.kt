package com.android.myapplication.network

import com.google.gson.Gson
import okhttp3.*
import timber.log.Timber

/**
 * A list of fake results to return.
 */
private val FAKE_RESULTS = listOf(
    "Apple",
    "Beet",
    "Grape",
    "Orange",
    "Pear"
)

/**
 * This class will return fake [Response] objects to Retrofit, without actually using the network.
 */
class NetworkInterceptor() : Interceptor {
    private var lastResult: String = ""
    val gson = Gson()

    private var attempts = 0

    /**
     * Return true iff this request should error.
     */
    private fun wantRandomError() = attempts++ % 5 == 0

    /**
     * Stop the request from actually going out to the network.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val wantRandomError = wantRandomError()
        return if (wantRandomError) {
            makeErrorResult(chain.request())
        } else {
            makeOkResult(chain, chain.request())
        }
    }


    /**
     * Generate an error result.
     *
     * ```
     * HTTP/1.1 500 Bad server day
     * Content-type: application/json
     *
     * {"cause": "not sure"}
     * ```
     */
    private fun makeErrorResult(request: Request): Response {
        return Response.Builder()
            .code(500)
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .message("Bad server day")
            .body(
                ResponseBody.create(
                    MediaType.get("application/json"),
                    gson.toJson(mapOf("cause" to "not sure"))
                )
            )
            .build()
    }

    /**
     * Generate a success response.
     *
     * ```
     * HTTP/1.1 200 OK
     * Content-type: application/json
     *
     * "$random_string"
     * ```
     */
    private fun makeOkResult(chain: Interceptor.Chain, request: Request): Response {
        val url = chain.request().url().toString()
        if (url.contains("banner"))
            return chain.proceed(request)
        var nextResult = lastResult
        while (nextResult == lastResult) {
            nextResult = FAKE_RESULTS.random()
        }
        lastResult = nextResult

        return Response.Builder()
            .code(200)
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .message("OK")
            .body(
                ResponseBody.create(
                    MediaType.get("application/json"),
                    gson.toJson(nextResult)
                )
            )
            .build()
    }
}