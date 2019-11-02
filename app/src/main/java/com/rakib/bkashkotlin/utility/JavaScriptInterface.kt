package com.rakib.bkashkotlin.utility

import android.content.Context
import android.content.Intent
import android.webkit.JavascriptInterface
import com.rakib.bkashkotlin.MainActivity

class JavaScriptInterface(var context: Context) {

    @JavascriptInterface
    fun switchActivity(response : String){
        val intent = Intent(context,MainActivity::class.java)
        intent.putExtra("response", response)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intent)
    }
}