package com.example.appnews

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
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


        var model: UserModel = UserModel(personEmail, personId1, 0)

        val cuc = CypherPol.encrypt(personId1)
        Log.d("sinencryptao", personId1)
        Log.d("encrypptao", cuc.toString())
        Log.d("desencryptao", CypherPol.decrypt(cuc.toString()).toString())


        //app.room.getUserDao().insertAllData(model)

        lifecycleScope.launch {
            //DatabaseClass.getDatabase(applicationContext).getUserDao().insertAllData(model)
            val x = DatabaseClass.getDatabase(applicationContext).getUserDao().getAll()
            Log.d("INSERTAO", x.toString())
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