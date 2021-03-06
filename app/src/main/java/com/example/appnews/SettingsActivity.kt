package com.example.appnews

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appnews.Database.DatabaseClass
import kotlinx.coroutines.launch

class SettingsActivity: AppCompatActivity() {

    private lateinit var dropDownCountries: Spinner
    private lateinit var dropDownLanguages: Spinner
    private lateinit var buttonReturn: Button
    private lateinit var buttonAccept: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        dropDownCountries = findViewById(R.id.spinnerCountries)
        dropDownLanguages = findViewById(R.id.spinnerLanguages)
        buttonAccept = findViewById(R.id.buttonAccept)
        buttonReturn = findViewById(R.id.buttonReturn)


        val countries = arrayOf("GLOBAL", "ES", "EN", "FR", "PT")

        var adapterCountries: ArrayAdapter<String> = ArrayAdapter(this, R.layout.dropdown_item, countries)

        dropDownCountries.adapter=adapterCountries


        val languages = arrayOf("es", "en", "fr", "pt")

        var adapterLanguages: ArrayAdapter<String> = ArrayAdapter(this, R.layout.dropdown_item, languages)

        dropDownLanguages.adapter=adapterLanguages

        buttonReturn.setOnClickListener { v: View ->
            startActivity(Intent(this, MainActivity::class.java))
        }

        buttonAccept.setOnClickListener { v: View ->
            lifecycleScope.launch {

                val country = DatabaseClass.getDatabase(applicationContext).getUserDao().getCountry(GlobalClass.email)
                val language = DatabaseClass.getDatabase(applicationContext).getUserDao().getLanguage(GlobalClass.email)

                GlobalClass.prevCountry=country
                GlobalClass.prevLanguage=language

                DatabaseClass.getDatabase(applicationContext).getUserDao().updateCountry(dropDownCountries.selectedItem.toString(), GlobalClass.email)

                DatabaseClass.getDatabase(applicationContext).getUserDao().updateLanguage(dropDownLanguages.selectedItem.toString(), GlobalClass.email)

                if (dropDownCountries.selectedItem.toString() != "GLOBAL" &&!GlobalClass.url.contains("country")){
                    GlobalClass.url= GlobalClass.url.replace("&apiKey", "&country="+dropDownCountries.selectedItem.toString()+"&apiKey")
                }
                if(dropDownCountries.selectedItem.toString() == "GLOBAL" &&GlobalClass.url.contains("country")){
                    GlobalClass.url = GlobalClass.url.replace("&country="+country, "")
                }

                GlobalClass.url = GlobalClass.url.replace("language="+language, "language="+dropDownLanguages.selectedItem.toString())
            }

            startActivity(Intent(this, MainActivity::class.java))
        }






    }
}