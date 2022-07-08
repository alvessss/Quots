package com.alvessss.quots

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.alvessss.quots.xmlDataClass.DataFetch
import java.io.File
import java.io.FileInputStream


const val TAG: String = "MainActivity"
const val XML_SOURCE_FILE: String = "/xmlSourceFile.xml"
const val XML_SOURCE_LINK: String = "https://raw.githubusercontent.com/alvessss/QuotesSource/master/quotesSource.xml"

class MainActivity : AppCompatActivity()
{
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i(TAG, "permission granted")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestPermissionLauncher.launch(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        }
    }

    override fun onStart()
    {
        super.onStart()
        val imageView: ImageView = findViewById(R.id.backgroundImage)
        val phraseView: TextView = findViewById(R.id.phrase)
        val appsDir: String = this.filesDir.absolutePath
        Thread {
            // do your stuff
            WebFileDownloader.getFromUrl(XML_SOURCE_LINK, appsDir + XML_SOURCE_FILE)
            val xmlData: DataFetch = MyXmlPuller.getFrom(File(appsDir + XML_SOURCE_FILE).readText())
            Log.v(TAG, "Frames founded > ${xmlData.quotes.size}")
            // test
            val imagePath: String = "/testImage"
            WebFileDownloader.getFromUrl(xmlData.quotes.first().image.toString(), appsDir + imagePath)
            runOnUiThread {
                // do onPostExecute stuff
                imageView.getFromPath(appsDir + imagePath)
                phraseView.text = xmlData.quotes.first().phrase
                File(appsDir + imagePath).delete()
                File(appsDir + XML_SOURCE_FILE).delete()
            }
        }.start()

    }
}

fun ImageView.getFromPath(path: String)
{
    val options = Options()
    options.inSampleSize = 8
    val bitmapPicture = BitmapFactory.decodeFile(path)
    this.setImageBitmap(bitmapPicture)
}