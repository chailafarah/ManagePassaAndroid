package com.example.managepass

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ListingPasswords : AppCompatActivity() {
    private val TAG = "ListingPasswords"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listing_passwords)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Récupérer le id du button ajouter
        val buttonajouter = findViewById<Button>(R.id.button_ajouter);
        // Ajouter un écouteur d'événements sur le bouton
        buttonajouter.setOnClickListener {
            // Ouvrir l'activité SignInActivity
            startActivity(Intent(this, AddPasswordActivity::class.java))
        }
        val database = Firebase.firestore
        //recureper les données de l'utilisateur nom et prenom
        val user = Firebase.auth.currentUser
        // set welcome_text (textview) to "Welcome $user.displayName"
        findViewById<TextView>(R.id.welcome_text).text = "Bonjour ${user!!.displayName}"
        // recuperer les info d'utilisateur
        database
            .collection("passwords")
            .whereEqualTo("uid", user.uid)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }
}