package com.smartcity.springapplication.storage

import java.io.InputStream

interface FileStorage {
    fun upload(fileName: String?, path: String, file: InputStream?): String
    fun download(fileName: String?, path: String): ByteArray
    fun delete(fileName: String?, path: String)
}