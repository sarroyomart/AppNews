package com.example.appnews

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity : AppCompatActivity() {

    lateinit var signin: SignInButton

    companion object{
        private const val  RC_SIGN_IN = 120
    }

    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var mAuth: FirebaseAuth

    init {
        System.loadLibrary("api-keys")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        signin = findViewById<SignInButton>(R.id.sign_in_button)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("595419854935-qs35t20h414smvvs9a9hr7gh8es84vvt.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        mAuth = FirebaseAuth.getInstance()

        signin.setOnClickListener{
            val signInIntent =googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.w("OnActivityResult", requestCode.toString())


        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception

            if (exception != null) {
                Log.w("SignInActivity", exception.message.toString())
                Log.w("SignInActivity", exception)
            }
            if(task.isSuccessful){
                Log.w("OnActivityResult", "succesful")
                try {
                    val account = task.getResult(ApiException::class.java)

                    if (account != null) {
                        firebaseAuthWithGoogle(account.idToken!!)
                    }

                } catch (e: ApiException) {
                    Log.w("SignInActivity", exception.toString())
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.w("SignInActivity", "firebaseauth")
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {

                    GlobalClass.url = getKeys()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.w("SignInActivity", "signInWithCredential:failure", task.exception)
                }
            }
    }
    private external fun getKeys():String
}