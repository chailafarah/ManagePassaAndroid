package com.example.managepass

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Edit_Delete_Password : AppCompatActivity() {
    private val TAG = "EditPasswords"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_delete_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Récupérer les données passées via l'Intent
        val nomUtilisateur = intent.getStringExtra("nom_utilisateur")
        val adresseEmail = intent.getStringExtra("adresse_email")
        val urlSite = intent.getStringExtra("url_site")
        val password = intent.getStringExtra("mot_de_passe")
        val id = intent.getStringExtra("id")
        // Remplir les champs EditText avec les données
        findViewById<EditText>(R.id.edit_username).setText(nomUtilisateur)
        findViewById<EditText>(R.id.edit_adresse_email).setText(adresseEmail)
        findViewById<EditText>(R.id.edit_url_site_input).setText(urlSite)
        findViewById<EditText>(R.id.edit_input_password).setText(password)
        val user = Firebase.auth.currentUser
        // Récupérer le bouton  enregistrer pour modifier des passwords
        val buttonupdate = findViewById<Button>(R.id.btn_save);
        val buttonDelete = findViewById<Button>(R.id.btn_delete)
        // Ajouter un écouteur d'événements sur le bouton
        buttonupdate.setOnClickListener {
            // Récupération des valeurs des champs
            val siteurl = findViewById<EditText>(R.id.edit_url_site_input).text.toString()
            val nomutilisateur = findViewById<EditText>(R.id.edit_username).text.toString()
            val email = findViewById<EditText>(R.id.edit_adresse_email).text.toString()
            val password = findViewById<EditText>(R.id.edit_input_password).text.toString()
            var database = Firebase.firestore
            val tgData = mapOf(
                "mot_de_passe" to password,
                "nom_utilisateur" to nomutilisateur,
                "adresse_email" to email,
                "url_site" to siteurl,
                "uid" to user?.uid
            )
            //recuperer les informationn du listing email password nom d'utilistaeur url du site qui exist sur firabase
            if (id != null) {
                database
                    .collection("passwords").document(id)
                    .update(tgData)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot updated with ID: ${id}")
                        Toast.makeText(
                            this,
                            "Les informations du site $urlSite ont été modifiées avec succès.",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Redirection vers ListingPasswordActivity
                        val intent = Intent(this, ListingPasswords::class.java)
                        startActivity(intent)
                        finish() // Ferme l'activité actuelle
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error updating document", e)
                    }
            }
        }
        buttonDelete.setOnClickListener {
            if (id != null) {
                val database = Firebase.firestore
                database.collection("passwords").document(id)
                    .delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!")
                        Toast.makeText(
                            this,
                            "Les informations du site $urlSite ont été supprimées avec succès.",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Redirection vers la liste des mots de passe
                        val intent = Intent(this, ListingPasswords::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error deleting document", e)
                        Toast.makeText(
                            this,
                            "Erreur lors de la suppression des informations du site $urlSite.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(this, "Impossible de récupérer l'ID du document.", Toast.LENGTH_SHORT).show()
            }
        }

        //lors du click sur le button close fermer l'activity du form add
        val closeIcon: ImageView = findViewById(R.id.close_icon)
        closeIcon.setOnClickListener {
            finish() // Ferme l'Activity actuelle
            startActivity(Intent(this, ListingPasswords::class.java))
        }

    }
}