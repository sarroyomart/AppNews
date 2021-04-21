package com.example.appnews

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.hardware.biometrics.BiometricPrompt
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.appnews.AdvancedFilter.FilterActivity
import com.example.appnews.Cypher.CypherPol
import com.example.appnews.Database.DatabaseClass
import com.example.appnews.Database.UserModel
import com.example.appnews.MainPageFragments.FavsFragment
import com.example.appnews.MainPageFragments.HomeFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.crypto.KeyGenerator

class MainActivity : AppCompatActivity(){

    var personName =""
    var personEmail=""
    var personId1=""
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var referance: DatabaseReference
    lateinit var googleSignInClient: GoogleSignInClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        firebaseAuth = FirebaseAuth.getInstance()
        val bottomNav: BottomNavigationView = findViewById(R.id.bottomnav)


        bottomNav.setOnNavigationItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.itemhome -> {
                    val fragment = HomeFragment.newInstance()
                    openFragment(fragment)
                    true
                }
                R.id.itemfavs -> {
                    val fragment = FavsFragment.newInstance()
                    openFragment(fragment)
                    true
                }

                else-> false

            }
        }

        database = FirebaseDatabase.getInstance()

        referance =database.getReference()

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            personName = acct.displayName.toString()
            personEmail = acct.email.toString()
            personId1 = acct.id.toString()

            GlobalClass.email = personEmail

        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("595419854935-qs35t20h414smvvs9a9hr7gh8es84vvt.apps.googleusercontent.com")
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        var key = CypherPol.generateKey()
        val encryptedId = CypherPol.encrypt(key, personId1)





        lifecycleScope.launch {
            //DatabaseClass.getDatabase(applicationContext).getUserDao().nukeTable()

            val usr = DatabaseClass.getDatabase(applicationContext).getUserDao().getUserId(personEmail)
            if (usr.size==0){
                var model: UserModel = UserModel(personEmail, encryptedId, 0, "GLOBAL", "es", key)
                DatabaseClass.getDatabase(applicationContext).getUserDao().insertAllData(model)
            }

            GlobalClass.personId=DatabaseClass.getDatabase(applicationContext).getUserDao().getId(GlobalClass.email)
            GlobalClass.key=DatabaseClass.getDatabase(applicationContext).getUserDao().getKey(GlobalClass.email)

            val country = DatabaseClass.getDatabase(applicationContext).getUserDao().getCountry(GlobalClass.email)
            val language = DatabaseClass.getDatabase(applicationContext).getUserDao().getLanguage(GlobalClass.email)

            if (country != "GLOBAL" &&!GlobalClass.url.contains("country")){
                    GlobalClass.url= GlobalClass.url.replace("&apiKey", "&country=$country&apiKey")
            }

            GlobalClass.url = GlobalClass.url.replace("language=en", "language="+language)
            Log.d("urllink", GlobalClass.url)


            openFragment(HomeFragment.newInstance())

        }


    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater:MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.mainpagemenu, menu)

        menu?.findItem(R.id.item1)?.title = "Hi $personName"




        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itemsearch->startActivity(Intent(this, FilterActivity::class.java))
            R.id.item1 -> Toast.makeText(this, "Item 1 selected", Toast.LENGTH_LONG).show()
            R.id.itemSettings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.item3 -> logOut()
        }
        return super.onOptionsItemSelected(item)
    }
    fun logOut(){
        startActivity(Intent(this, LoginActivity::class.java))
        lifecycleScope.launch {
            DatabaseClass.getDatabase(applicationContext).getUserDao().updateFingerprint(0, GlobalClass.email)
        }

        googleSignInClient.signOut()
        firebaseAuth.signOut()
    }
}