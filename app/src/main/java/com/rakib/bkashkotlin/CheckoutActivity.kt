package com.rakib.bkashkotlin

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.rakib.bkashkotlin.model.Checkout
import com.rakib.bkashkotlin.model.PaymentRequest
import com.rakib.bkashkotlin.utility.JavaScriptInterface
import kotlinx.android.synthetic.main.activity_checkout.*

class CheckoutActivity : AppCompatActivity() {

    private var request: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val checkout: Checkout = intent.getSerializableExtra("values") as Checkout

        val paymentRequest = PaymentRequest()
        paymentRequest.amount = checkout.amount
        paymentRequest.intent = checkout.intent

        val gson = Gson()

        request = gson.toJson(paymentRequest)

        val webSettings = checkout_webview.settings
        webSettings.javaScriptEnabled = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        checkout_webview?.apply {
            isClickable = true
            settings.domStorageEnabled = true
            settings.setAppCacheEnabled(true)
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            clearCache(true)
            settings.allowFileAccessFromFileURLs = true
            settings.allowUniversalAccessFromFileURLs = true
        }

        checkout_webview.addJavascriptInterface(JavaScriptInterface(this), "AndroidNative")

        if (checkout.amount.equals("one"))
            checkout_webview.loadUrl("file:///android_asset/www/checkout_110.html")
        else {
            checkout_webview.loadUrl("file:///android_asset/www/checkout_120.html")
        }

        checkout_webview.webViewClient = CheckoutWebViewClient()
    }

    private inner class CheckoutWebViewClient : WebViewClient() {

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            handler.proceed()
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            Log.d("External URL: ", url)
            if (url == "https://www.bkash.com/terms-and-conditions") {
                val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(myIntent)
                return false
            }
            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            progress_bar.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            val paymentRequest = "{paymentRequest:$request}"
            checkout_webview.loadUrl("javascript:callReconfigure($paymentRequest )")
            checkout_webview.loadUrl("javascript:clickPayButton()")
            checkout_webview.loadUrl("javascript:switchToMain()")
            progress_bar.visibility = View.GONE
        }
    }
}
