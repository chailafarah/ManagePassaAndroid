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
import coil.load
import com.google.firebase.auth.ktx.auth
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
                    val title = document.getString("nom_utilisateur") ?: "No Title"
                    val subtitle = document.getString("adresse_email") ?: "No email"
                    val siteUrl = document.getString("url_site") ?: ""  // On récupère l'URL, mais on ne l'affiche pas
                    // Créer une vue pour chaque élément
                    val view = LayoutInflater.from(this).inflate(R.layout.item, container, false)

                    // Afficher les données dans les TextView
                    view.findViewById<TextView>(R.id.nom_user_text_view).text = title
                    view.findViewById<TextView>(R.id.email_text_view).text = subtitle
                    // Charger l'icône du site (favicon)
                    val domain = siteUrl.removePrefix("https://").removePrefix("http://")
                    val faviconView = view.findViewById<ImageView>(R.id.home_image_view)
                    if (siteUrl.isNotEmpty()) {
                        // Extraire uniquement le domaine pour l'URL du favicon
                        val domain = siteUrl.removePrefix("https://").removePrefix("http://")
                        val faviconUrl = "https://www.google.com/s2/favicons?sz=64&domain=$domain"

                        // Charger l'icône du site avec Coil
                        faviconView.load(faviconUrl) {
                            placeholder(R.drawable.user__1_)  // Icône par défaut pendant le chargement
                            Log.d("Favicon", "Favicon URL: $faviconUrl")
                            error(R.drawable.default_image) // Icône par défaut si l'URL ne fonctionne pas
                        }
                    }
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