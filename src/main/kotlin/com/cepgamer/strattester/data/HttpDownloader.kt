package com.cepgamer.strattester.data

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

abstract class HttpDownloader(private val url: URL) {
    fun download(): String {
        val connection = url.openConnection() as HttpURLConnection
        setUpConnection(connection)

        val result = connection.responseCode
        val response = StringBuilder()
        BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
            reader.lines().forEach {
                response.append(it + "\n")
            }
        }

        return response.toString()
    }

    abstract fun setUpConnection(connection: HttpURLConnection)
}