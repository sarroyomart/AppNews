package com.example.appnews

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuView
import androidx.fragment.app.Fragment
import com.example.appnews.Database.DatabaseModel
import com.example.appnews.MainPageFragments.FavsFragment
import com.example.appnews.MainPageFragments.HomeFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//Añadido los menus
class MainActivity : AppCompatActivity(){

    var personName =""
    var personEmail=""
    var personId1=""
    var la = LoginActivity
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var referance: DatabaseReference
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        firebaseAuth = FirebaseAuth.getInstance()
        val bottomNav: BottomNavigationView = findViewById(R.id.bottomnav)
        openFragment(HomeFragment.newInstance())

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
        Log.d("InfoDB1", database.toString())
        referance =database.getReference()
        Log.d("InfoDB2", referance.toString())
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            personName = acct.displayName.toString()
            val personGivenName = acct.givenName
            val personFamilyName = acct.familyName
            personEmail = acct.email.toString()
            personId1 = acct.id.toString()
            val personPhoto: Uri? = acct.photoUrl
            GlobalClass.personId=personId1

            Log.d("Info", personName + personEmail)


        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("595419854935-qs35t20h414smvvs9a9hr7gh8es84vvt.apps.googleusercontent.com")
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //sendData()



    }

    private fun sendData(){
        var name = personName
        var email = personEmail
        if(name.isNotEmpty()&&email.isNotEmpty()){
            var model= DatabaseModel(name, email)
            var id = referance.push().key
            referance.child(personId1).setValue(model)
        }
    }
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater:MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.mainpagemenu, menu)

        menu?.findItem(R.id.item1)?.setTitle("Hi " + personName)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item1 -> Toast.makeText(this, "Item 1 selected", Toast.LENGTH_LONG).show()
            R.id.item2 -> Toast.makeText(this, "Item 2 selected", Toast.LENGTH_LONG).show()
            R.id.item3 -> logOut()
        }
        return super.onOptionsItemSelected(item)
    }
    fun logOut(){
        startActivity(Intent(this, LoginActivity::class.java))
        googleSignInClient.signOut()
        firebaseAuth.signOut()
    }
}