package com.rakib.bkashkotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rakib.bkashkotlin.model.Checkout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonUrlCheckout.setOnClickListener {
            if (checkout_amount?.text.toString().isNotEmpty()) {
                var checkout = Checkout()
                checkout.amount = checkout_amount.text.toString()

                if (one_one.isChecked) {
                    checkout.version = "one"
                } else {
                    checkout.version = "two"
                }

                if (intent_sale.isChecked) {
                    checkout.intent = "sale"
                } else {
                    checkout.intent = "authorization"
                }

                val intent = Intent(applicationContext,CheckoutActivity::class.java).apply {
                    putExtra("values",checkout)
                }

                startActivity(intent)

            } else {
                Toast.makeText(this, "Please enter the field values", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            val intent = intent
            val response = intent.getStringExtra("response")
            response_text.text = response
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
