package com.example.simplewebviewapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private var filePathCallback: ValueCallback<Array<Uri>>? = null
    private val REQUEST_STORAGE_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView1)
        setupWebView()
        loadWebViewContent()
        requestStoragePermission()
    }

    private fun setupWebView() {
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.allowFileAccess = true

        webView.webViewClient = CustomWebViewClient()
        webView.webChromeClient = CustomWebChromeClient()
    }

    private fun loadWebViewContent() {
        webView.loadUrl("http://192.168.0.105:80/")

        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.settings.setAppCacheEnabled(false)
    }

    private fun requestStoragePermission() {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_STORAGE_PERMISSION)
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private inner class CustomWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (url != null) {
                view?.loadUrl(url)
            }
            return true
        }
    }

    private inner class CustomWebChromeClient : WebChromeClient() {
        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            filePathCallback?.let {
                this@MainActivity.filePathCallback = it
            }
            openFileInput()
            return true
        }
    }

    private fun openFileInput() {
        // Implement the code to open the file input dialog here
        // You can use an Intent to open a file picker
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"  // Set the MIME type according to your needs
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                data?.data?.let {
                    filePathCallback?.onReceiveValue(arrayOf(it))
                    filePathCallback = null
                }
            } else {
                filePathCallback?.onReceiveValue(null)
                filePathCallback = null
            }
        }
    }
}
