package ru.otus

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

abstract class BaseHttpClient {

    val gson = Gson()

    private val client = OkHttpClient.Builder().build()
    private val JSON = "application/json; charset=utf-8".toMediaType()

    inline fun <reified T> Gson.fromJson(json: String?): T = fromJson(json, T::class.java)

    fun <T> doPostRequest(url: String, body: T): Response? {
        val request = Request.Builder()
            .post(gson.toJson(body).toRequestBody(JSON))
            .url(url)
            .build()
        val response = client.newCall(request).execute()
        return if (response.isError()) null else response
    }

    fun doPutRequest(url: String): Boolean {
        val request = Request.Builder()
            .put(gson.toJson("").toRequestBody(JSON))
            .url(url)
            .build()
        val response = client.newCall(request).execute()
        return response.isError()
    }

    fun doGetRequest(url: String): Response {
        val request = Request.Builder()
            .get()
            .url(url)
            .build()
        return client.newCall(request).execute()
    }

    fun doDeleteRequest(url: String): Boolean {
        val request = Request.Builder()
            .delete()
            .url(url)
            .build()
        val response = client.newCall(request).execute()
        return response.isError()
    }

    private fun Response.isError(): Boolean {
        return !this.code.toString().startsWith("200")
    }
}