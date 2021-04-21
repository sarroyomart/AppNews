package com.example.appnews.AdvancedFilter

import android.content.Intent
import android.nfc.tech.TagTechnology
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.appnews.GlobalClass
import com.example.appnews.MainActivity
import com.example.appnews.R



class FilterActivity: AppCompatActivity() {

    lateinit var business: Button
    lateinit var entertainment: Button
    lateinit var general: Button
    lateinit var health: Button
    lateinit var science: Button
    lateinit var sports: Button
    lateinit var technology: Button
    lateinit var searchText: EditText
    lateinit var search: Button
    lateinit var returnB: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_filter)

        business = findViewById(R.id.businessB)
        entertainment = findViewById(R.id.entertainmentB)
        general = findViewById(R.id.generalB)
        health = findViewById(R.id.healthB)
        science = findViewById(R.id.scienceB)
        sports = findViewById(R.id.sportsB)
        technology = findViewById(R.id.technologyB)
        searchText = findViewById(R.id.searchText)
        search = findViewById(R.id.searchB)
        returnB = findViewById(R.id.returnB)

        if (GlobalClass.url.contains("category")){
            var aux = GlobalClass.url.split("&category")
            GlobalClass.url= aux[0]
        }

        if(GlobalClass.url.contains("keywords")){
            var aux = GlobalClass.url.split("&keywords")
            GlobalClass.url= aux[0]
        }

        business.setOnClickListener{v: View ->

            GlobalClass.url = GlobalClass.url+"&category=business"

            changeActivity()
        }
        entertainment.setOnClickListener{v: View ->

            GlobalClass.url = GlobalClass.url+"&category=entertainment"

            changeActivity()
        }

        general.setOnClickListener{v: View ->

            GlobalClass.url = GlobalClass.url+"&category=general"

            changeActivity()
        }

        health.setOnClickListener{v: View ->

            GlobalClass.url = GlobalClass.url+"&category=health"

            changeActivity()
        }
        science.setOnClickListener{v: View ->

            GlobalClass.url = GlobalClass.url+"&category=science"

            changeActivity()
        }
        sports.setOnClickListener{v: View ->

            GlobalClass.url = GlobalClass.url+"&category=sports"

            changeActivity()
        }
        technology.setOnClickListener{v: View ->

            GlobalClass.url = GlobalClass.url+"&category=technology"

            changeActivity()
        }

        search.setOnClickListener{v: View ->

            GlobalClass.url = GlobalClass.url+"&keywords="+searchText.text

            changeActivity()
        }
        returnB.setOnClickListener{v: View ->

            changeActivity()
        }



    }

    fun changeActivity(){
        startActivity(Intent(this, MainActivity::class.java))
    }
}