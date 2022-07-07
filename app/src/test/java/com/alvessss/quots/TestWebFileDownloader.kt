package com.alvessss.quots

import org.junit.Test

import org.junit.Assert.*
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestWebFileDownloader {
    @Test
    fun `file length must be greater than 0`() {
        val googleFile = File("./test-files/google")
        if (googleFile.exists()) {
            googleFile.delete()
        }

        WebFileDownloader.getFromUrl("https://www.google.com", "./test-files/google")
        assert(googleFile.exists())
        assert(googleFile.length() > 0)

        val imageFile = File("./test-files/image")
        if (imageFile.exists()) {
            imageFile.delete()
        }

        WebFileDownloader.getFromUrl("https://www.google.com/favicon.ico", "./test-files/image")
        assert(imageFile.exists())
        assert(imageFile.length() > 0)
    }
}