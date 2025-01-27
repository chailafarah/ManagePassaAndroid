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
        val passwordInput = findViewById<EditText>(R.id.password_input);
        // Ajouter un écouteur d'événements sur le bouton
        button.setOnClickListener {
            // Ouvrir l'activité SignInActivity
            signIn(email.text.toString(), passwordInput.text.toString())
        }
        // Récupérer le bouton forgot_password_button par son ID
        val button2 = findViewById<TextView>(R.id.forget_text);
        // Ajouter un écouteur d'événements sur le bouton
        button2.setOnClickListener {
            // Ouvrir l'activité SignInActivity
            sendPasswordResetEmail(email.text.toString())
        }

        // Récupérer le bouton inscrivez vous par son ID
        val button3 = findViewById<TextView>(R.id.sign_up_text);
        // Ajouter un écouteur d'événements sur le bouton
        button3.setOnClickListener {
            //rediriger vers l'activité signup
            startActivity(Intent(this, signup::class.java))
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
                        this@SignInActivity, "Authentication ${user!!.displayName} success.",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Redirection vers l'activité ListingPasswords
                    val intent = Intent(this@SignInActivity, ListingPasswords::class.java)
                    startActivity(intent)
                    finish()
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
    // méthode pour envoyer un e-mail de réinitialisation de mot de passe
    private fun sendPasswordResetEmail(email: String) {
        if (email.isEmpty()) {
            Toast.makeText(
                this, "Veuillez entrer une adresse e-mail valide.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Envoyer un e-mail de réinitialisation de mot de passe
        mAuth!!.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email de réinitialisation envoyé à $email")
                    Toast.makeText(
                        this,
                        "Un e-mail de réinitialisation a été envoyé à $email.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.w(TAG, "Échec de l'envoi de l'e-mail de réinitialisation", task.exception)
                    Toast.makeText(
                        this,
                        "Échec de l'envoi de l'e-mail de réinitialisation. Vérifiez votre adresse e-mail.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


}