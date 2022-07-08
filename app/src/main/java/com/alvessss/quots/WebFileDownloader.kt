package com.alvessss.quots

import java.io.File
import java.io.FileOutputStream
import java.net.URL

class WebFileDownloader {
    companion object {
        fun getFromUrl(url: String, fileName: String): Boolean {
            val fd: File = File(fileName)
            if (fd.createNewFile() or fd.exists()) {
                download(url, fileName)
                return fd.length() > 0
            }

            return false
        }

        private fun download(link: String, path: String) {
            URL(link).openStream().use { input ->
                FileOutputStream(File(path)).use { output ->
                    input.copyTo(output)
                }
            }
        }
    }
}