package com.alvessss.quots

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

const val TAG: String = "MainActivity"
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

    private lateinit var phraseTextView: TextView
    private lateinit var frameDisplay: FrameDisplay

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

        frameDisplay = FrameDisplay(this)
        frameDisplay.loadResources()

        phraseTextView = findViewById(R.id.phrase)

        phraseTextView.setOnTouchListener(OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                initialX = event.x
            }
            if (event.action == MotionEvent.ACTION_UP) {
                val finalX: Float = event.x
                if (initialX > finalX) {
                    Log.d(TAG, "right to left")
                    frameDisplay.next()
                }
            }
            true
        })
    }
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