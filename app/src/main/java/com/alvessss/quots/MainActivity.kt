package com.alvessss.quots

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.alvessss.quots.xmlDataClass.DataFetch
import com.alvessss.quots.xmlDataClass.Frame
import java.io.File


const val TAG: String = "MainActivity"
const val IMAGE_SOURCE_FILE: String = "com.alvessss.quots.image.xml"
const val XML_SOURCE_FILE: String = "com.alvessss.quots.xmlSourceFile.xml"
const val XML_SOURCE_LINK: String = "https://raw.githubusercontent.com/alvessss/QuotesSource/master/quotesSource.xml"

const val NO_INTERNET: String = "Cannot download the necessary resources to run the app. Please check your Internet connection :)"

class MainActivity : AppCompatActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i(TAG, "permission granted")
            }
        }

    private lateinit var xmlSourceFile: File
    private lateinit var xmlDataFetch: DataFetch
    private var currentFrame: Int = 0

    private lateinit var xmlFilePath: String
    private lateinit var appsDirPath: String
    private lateinit var imageFilePath: String

    private lateinit var backgroundImageView: ImageView
    private lateinit var phraseTextView: TextView

    // for onTouchListener
    var initialX: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestPermissionLauncher.launch(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()
        // get our views
        backgroundImageView = findViewById(R.id.backgroundImage)
        phraseTextView = findViewById(R.id.phrase)

        // file name of our xml got from github
        appsDirPath = this.filesDir.absolutePath
        xmlFilePath = "$appsDirPath/$XML_SOURCE_FILE"
        imageFilePath = "$appsDirPath/$IMAGE_SOURCE_FILE"

        // try to get the xml to start
        xmlSourceFile = File(xmlFilePath)
        Thread {
            try {
                WebFileDownloader.getFromUrl(XML_SOURCE_LINK, (xmlSourceFile as File).absolutePath)
                xmlDataFetch = MyXmlPuller.getFrom((xmlSourceFile as File).readText())
                assert(xmlDataFetch.quotes.isNotEmpty())
            } catch (ex: Exception) {
                showMessage(NO_INTERNET, this)
                closeApp(this)
            }
        }.start()

        // set scrollable action
        phraseTextView.setOnTouchListener(OnTouchListener { _, event ->
            // Log.d(TAG, "onTouch entered")
            if (event.action == MotionEvent.ACTION_DOWN) {
                initialX = event.x
            }

            if (event.action == MotionEvent.ACTION_UP) {
                // Log.d(TAG, "ACTION_UP")
                val finalX: Float = event.x
                if (initialX > finalX) {
                    Log.d(TAG, "right to left")
                    nextFrame()
                }
            }

            true
        })
    }

    private fun nextFrame() {
        Thread {
            if (currentFrame < xmlDataFetch.quotes.size) {
                val frame: Frame = xmlDataFetch.quotes[currentFrame]
                WebFileDownloader.getFromUrl(frame.image.toString(), imageFilePath)
                runOnUiThread {
                    phraseTextView.text = frame.phrase
                    backgroundImageView.getFromPath(imageFilePath)
                    currentFrame++
                    File(imageFilePath).delete()
                }
            } else {
                currentFrame = 0
                nextFrame()
            }
        }.start()
    }
}

private fun ImageView.getFromPath(path: String) {
    val options = Options()
    options.inSampleSize = 8
    val bitmapPicture = BitmapFactory.decodeFile(path)
    this.setImageBitmap(bitmapPicture)
}

private fun showMessage(msg: String, context: AppCompatActivity) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
}

@SuppressLint("ObsoleteSdkInt")
private fun closeApp(context: AppCompatActivity) {
    if (Build.VERSION.SDK_INT < 21) {
        context.finishAffinity()
    } else {
        context.finishAndRemoveTask()
    }
}