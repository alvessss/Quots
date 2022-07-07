package com.alvessss.quots

import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.net.URL

const val TAG = "WebFileDownloader"

class WebFileDownloader
{
    companion object
    {
        fun getFromUrl(url: String, fileName: String): Boolean
        {
            val fd: File = File(fileName)
            if (fd.createNewFile() or fd.exists())
            {
                download(url, fileName)
                //Log.i(TAG, "File: ${fd.name} Length: ${fd.length()}")
                return fd.length() > 0
            }

            //Log.w(TAG, "File does not exists or cannot be created")
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