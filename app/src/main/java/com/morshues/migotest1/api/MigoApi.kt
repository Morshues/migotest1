package com.morshues.migotest1.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class MigoApi(isPublic: Boolean = true) {

    private lateinit var rootUrl: String

    init {
        setPublic(isPublic)
    }

    /**
     * To switch different root url by set up private or not
     */
    fun setPublic(isPublic: Boolean) {
        rootUrl = if (isPublic) ROOT_URL_PUBLIC else ROOT_URL_PRIVATE
    }

    private suspend fun httpGet(urlStr: String?): HTTPResult = withContext(Dispatchers.IO) {
        val url = URL(urlStr)
        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection

        val result: String
        try {
            if (conn.responseCode != 200) {
                val errorMsg = "${conn.responseCode}: ${conn.responseMessage}"
                return@withContext HTTPResult.Failure(errorMsg)
            }

            val inputStream = conn.inputStream
            result = convertInputStreamToString(inputStream)
        } catch (e: IOException) {
            return@withContext HTTPResult.Failure("Data IO Error")
        }

        HTTPResult.Success(result)
    }

    private fun convertInputStreamToString(inputStream: InputStream): String {
        val stringBuilder = StringBuilder()

        val bufferedReader: BufferedReader? = BufferedReader(InputStreamReader(inputStream))
        var line:String? = bufferedReader?.readLine()
        while (line != null) {
            stringBuilder.append(line)
            line = bufferedReader?.readLine()
        }

        inputStream.close()
        return stringBuilder.toString()
    }

    /**
     * use http get to fetch data from status api with root url
     */
    suspend fun getStatus() = httpGet(rootUrl+"status")

    companion object {
        private const val ROOT_URL_PUBLIC = "https://code-test.migoinc-dev.com/"
        private const val ROOT_URL_PRIVATE = "http://192.168.2.2/"
    }
}