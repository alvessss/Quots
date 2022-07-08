package com.alvessss.quots

import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alvessss.quots.xmlDataClass.DataFetch
import java.io.File

class FrameDisplay(activity: AppCompatActivity) {
    private val TAG: String ="FrameDisplay"

    private val XML_URL: String = "https://raw.githubusercontent.com/alvessss/QuotesSource/master/quotesSource.xml"
    private val XML_FILE_NAME: String = "FrameDisplayXmlDataFetch.xml"
    private val IMG_PREFIX: String = "FrameDisplayBackgroundImage_"
    private val IMG_EXT: String = ".jpg"

    private var numberOfFrames: Int = 0
    private var currentFrameIndex: Int = 0

    private lateinit var appDir: String
    private lateinit var dataFetch: DataFetch
    private lateinit var downloadedImages: ArrayList<String>

    private val activity: AppCompatActivity = activity
    private val phrase: TextView = activity.findViewById(R.id.phrase)
    private val image: ImageView = activity.findViewById(R.id.backgroundImage)

    fun loadResources() {
        Thread {
            appDir = activity.filesDir.absolutePath
            val xmlSourceFile = File("$appDir/$XML_FILE_NAME")
            WebFileDownloader.getFromUrl(XML_URL, xmlSourceFile.absolutePath)
            dataFetch = MyXmlPuller.getFrom(xmlSourceFile.readText().replace("&", "&amp;"))
            numberOfFrames = dataFetch.quotes.size
            currentFrameIndex = 0
            Log.i(TAG, "$numberOfFrames frames loaded")
            downloadImages()
        }.start()
    }

    fun start() {
        next()
    }

    fun next() {
        if (currentFrameIndex < numberOfFrames) {
            image.getFromPath(downloadedImages.get(currentFrameIndex))
            phrase.text = dataFetch.quotes.get(currentFrameIndex).phrase
            currentFrameIndex++
        } else {
            currentFrameIndex = 0
            next()
        }
    }

    fun release() {
        for (path in downloadedImages) {
            File(path).delete()
        }
    }

    private fun downloadImages() {
        if (dataFetch.quotes.isEmpty()) {
            return
        }

        Thread {
            var i: Int = 0
            var path: String
            downloadedImages = ArrayList()
            for (frame in dataFetch.quotes) {
                path = "$appDir/$IMG_PREFIX${i++}$IMG_EXT"
                WebFileDownloader.getFromUrl(frame.image!!, path)
                downloadedImages.add(path)
            }
        }.start()
    }

    private fun ImageView.getFromPath(path: String) {
        val options = BitmapFactory.Options()
        options.inSampleSize = 8
        val bitmapPicture = BitmapFactory.decodeFile(path)
        this.setImageBitmap(bitmapPicture)
    }
}