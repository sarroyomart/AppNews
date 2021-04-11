package com.example.appnews

import android.app.Application

class GlobalClass: Application() {

    companion object{
        var personId =""
        lateinit var key:ByteArray
        var ready = false
        var ready2 = false
        var email=""

        var url = ""
        var prevCountry=""
        var prevLanguage="en"
    }



}