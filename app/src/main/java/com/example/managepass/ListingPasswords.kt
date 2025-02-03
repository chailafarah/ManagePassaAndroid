package com.example.managepass

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
        val container = findViewById<LinearLayout>(R.id.container)
        // recuperer les info d'utilisateur
        database
            .collection("passwords")
            .whereEqualTo("uid", user.uid)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                    // Extraire les données du document
                    val title = document.getString("nom utilisateur") ?: "No Title"
                    val subtitle = document.getString("email") ?: "No email"

                    // Créer une vue pour chaque élément
                    val view = LayoutInflater.from(this).inflate(R.layout.item, container, false)

                    // Afficher les données dans les TextView
                    view.findViewById<TextView>(R.id.nom_user_text_view).text = title
                    view.findViewById<TextView>(R.id.email_text_view).text = subtitle

                    // Récupérer l'icône de copie et ajouter un écouteur de clic
                    val copyIconView = view.findViewById<ImageView>(R.id.copy_icon_view)
                    copyIconView.setOnClickListener {
                        // Copier l'e-mail dans le presse-papiers
                        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Email", subtitle)
                        clipboard.setPrimaryClip(clip)

                        // Afficher un message à l'utilisateur
                        Toast.makeText(this, "E-mail copié : $subtitle", Toast.LENGTH_LONG).show()
                    }

                    // Ajouter la vue au conteneur
                    container.addView(view)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

    }
}