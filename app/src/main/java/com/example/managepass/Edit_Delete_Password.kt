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
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class Edit_Delete_Password : AppCompatActivity() {
    private val TAG = "EditPasswords"
    private val client = OkHttpClient()
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
        if (password != null) {
            decryptPassword(password) { decryptedPassword ->
                runOnUiThread {
                    findViewById<EditText>(R.id.edit_input_password).setText(decryptedPassword)
                }
            }
        }
        // Remplir les champs EditText avec les données
        findViewById<EditText>(R.id.edit_username).setText(nomUtilisateur)
        findViewById<EditText>(R.id.edit_adresse_email).setText(adresseEmail)
        findViewById<EditText>(R.id.edit_url_site_input).setText(urlSite)
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
            var password = findViewById<EditText>(R.id.edit_input_password).text.toString()
            var database = Firebase.firestore

            encryptPassword(password) { encryptedPassword ->
                runOnUiThread {
                    val tgData = mapOf(
                        "mot_de_passe" to encryptedPassword,
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

    private fun decryptPassword(password: String, callback: (String) -> Unit) {
        val url = "https://ca01-46-193-69-79.ngrok-free.app/api/decrypt-aes-gcm"  // Replace with your actual API URL
        val json = JSONObject().apply {
            put("plainText", password)
            put("password", "chaimaa")
        }

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            json.toString()
        )

        val request = Request.Builder()
            .url(url)
            .addHeader("ngrok-skip-browser-warning", "true") // Add the required header
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { responseBody ->
                    val jsonResponse = JSONObject(responseBody)
                    val encryptedPassword = jsonResponse.getString("encryptedData")
                    callback(encryptedPassword)
                }
            }
        })
    }
    private fun encryptPassword(password: String, callback: (String) -> Unit) {
        val url = "https://71e7-46-193-69-79.ngrok-free.app/api/encrypt-aes-gcm"  // Replace with your actual API URL
        val json = JSONObject().apply {
            put("plainText", password)
            put("password", "chaimaa")
        }

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            json.toString()
        )

        val request = Request.Builder()
            .url(url)
            .addHeader("ngrok-skip-browser-warning", "true") // Add the required header
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { responseBody ->
                    val jsonResponse = JSONObject(responseBody)
                    val encryptedPassword = jsonResponse.getString("encryptedData")
                    callback(encryptedPassword)
                }
            }
        })
    }
}