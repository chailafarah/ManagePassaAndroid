package com.example.managepass

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth // Instance de FirebaseAuth
    private val TAG = "SignInActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = Firebase.auth;
        // [END initialize_auth]
        setContentView(R.layout.activity_sign_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Récupérer le bouton sign_in_button par son ID
        val button = findViewById<Button>(R.id.sign_in_button);
        val email = findViewById<EditText>(R.id.sign_in_email_input);
        val password = findViewById<EditText>(R.id.password_input);
        // Ajouter un écouteur d'événements sur le bouton
        button.setOnClickListener {
            // Ouvrir l'activité SignInActivity
            signIn(email.text.toString(), password.text.toString())
        }
    }
    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = mAuth!!.currentUser
                    Toast.makeText(
                        this@SignInActivity, "Authentication success.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@SignInActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        // [END sign_in_with_email]
    }
}