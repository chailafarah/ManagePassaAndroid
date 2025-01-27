package com.example.managepass

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class signup : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private val TAG = "signup"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth;
        firestore = Firebase.firestore

        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val button = findViewById<Button>(R.id.sign_up_button)
        val firstNameEditText = findViewById<EditText>(R.id.register_input_prenom)
        val lastNameEditText = findViewById<EditText>(R.id.register_input_nom)
        val emailEditText = findViewById<EditText>(R.id.register_email_input)
        val passwordInput = findViewById<EditText>(R.id.register_password_input);
        button.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordInput.text.toString()
            if (firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser

                            val profileUpdates = userProfileChangeRequest {
                                displayName = "$firstName $lastName"
                            }

                            user!!.updateProfile(profileUpdates)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            baseContext, "Inscription réussie vous pouvez se connecter maintenant.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        // Redirection vers l'activité signin pour se connecter
                                        startActivity(Intent(this, SignInActivity::class.java))
                                    }
                                }
                        }
                    }
            }
        }
        //recuperer le bouton se connecter par son ID
        val button2 = findViewById<TextView>(R.id.sign_in_text)
        //ajouter un ecouteur d'evenements sur le bouton
        button2.setOnClickListener {
            //rediriger vers l'activité SignInActivity
            startActivity(Intent(this, SignInActivity::class.java))
        }
        val eyeIcon = findViewById<ImageView>(R.id.eye_icon)
        // Gérer le clic sur l'icône de l'œil
        eyeIcon.setOnClickListener {
            // Vérifie si le mot de passe est masqué ou non
            if (passwordInput.inputType and InputType.TYPE_TEXT_VARIATION_PASSWORD != 0) {
                // Si le mot de passe est masqué, on le rend visible
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
                eyeIcon.setImageResource(R.drawable.icon_eye_on)  // Changer l'icône de l'œil
            } else {
                // Sinon, on le masque à nouveau
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                eyeIcon.setImageResource(R.drawable.icon_eye_off)  // Revenir à l'icône de l'œil fermé
            }

            // Revenir au focus de l'EditText pour que l'utilisateur puisse continuer à taper
            passwordInput.setSelection(passwordInput.text.length)
        }
    }
}