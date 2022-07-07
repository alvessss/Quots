package com.alvessss.quots

import org.junit.Test
import org.junit.Assert.*
import java.io.File

class TestWebFileDownloader {
    @Test
    fun `getFromUrl() file length must be greater than 0 `() {
        /*
            TODO: Disable logs in WebFileDownloader.getFromUrl before do tests
         */

        val googleFile = File("./test-files/google")
        WebFileDownloader.getFromUrl("https://www.google.com", "./test-files/google")
        assert(googleFile.exists())
        assert(googleFile.length() > 0)

        val imageFile = File("./test-files/image")
        WebFileDownloader.getFromUrl("https://www.google.com/favicon.ico", "./test-files/image")
        assert(imageFile.exists())
        assert(imageFile.length() > 0)

        googleFile.delete()
        imageFile.delete()
    }
}