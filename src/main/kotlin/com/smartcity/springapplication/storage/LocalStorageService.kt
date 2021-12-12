package com.smartcity.springapplication.storage

import java.io.IOException
import org.springframework.stereotype.Service
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class LocalStorageService : FileStorage {
    override fun upload(fileName: String?, path: String, file: InputStream?): String =
        try {
            val s = prepareDirectory(path)
            val fullName = String.format("%s/%s", s, fileName)
            Files.copy(file, Paths.get(fullName), StandardCopyOption.REPLACE_EXISTING)
            fullName
        } catch (e: IOException) {
            e.printStackTrace()
            throw FileStorageException("error.file.upload")
        }

    override fun download(fileName: String?, path: String): ByteArray =
        try {
            val s = prepareDirectory(path)
            val fullName = String.format("%s/%s", s, fileName)
            if (!Paths.get(fullName).toFile().exists()) {
                ByteArray(0)
            } else Files.readAllBytes(Paths.get(fullName))
        } catch (e: IOException) {
            e.printStackTrace()
            throw FileStorageException("error.file.download")
        }


    override fun delete(fileName: String?, path: String) {
        try {
            val s = prepareDirectory(path)
            val fullName = String.format("%s/%s", s, fileName)
            if (Paths.get(fullName).toFile().exists()) {
                Files.delete(Paths.get(fullName))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            throw FileStorageException("error.file.delete")
        }
    }

    private fun prepareDirectory(path: String): String {
        return try {
            val dir = String.format("%s/%s", homeDirectory, path)
            val fullPath = Paths.get(dir)
            if (!fullPath.toFile().exists()) {
                Files.createDirectory(fullPath)
            }
            dir
        } catch (e: IOException) {
            e.printStackTrace()
            throw FileStorageException("error.file.create-dir.failed")
        }
    }

    private val homeDirectory: String
        get() = System.getProperty("user.home")
}