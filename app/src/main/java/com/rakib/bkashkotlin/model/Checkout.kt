package com.rakib.bkashkotlin.model

import java.io.Serializable

data class Checkout(
    var amount: String? = "",
    var intent : String? = "",
    var version : String? = ""
) : Serializable