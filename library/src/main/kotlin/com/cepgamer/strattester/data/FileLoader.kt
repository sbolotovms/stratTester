package com.cepgamer.strattester.data

import java.io.File

class FileLoader(private val filename: String) {
    fun load(): String {
        val file = File(filename)
        return file.readText()
    }
}