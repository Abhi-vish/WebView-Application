package com.example.simplewebviewapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri

class WebViewJavaScriptInterface(private val activity: Activity) {

    fun openFileInput() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"  // You can set a specific MIME type if needed
        activity.startActivityForResult(intent, 1)
    }
}
