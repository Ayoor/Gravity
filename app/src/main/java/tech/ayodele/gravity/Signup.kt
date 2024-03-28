package tech.ayodele.gravity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import tech.ayodele.gravity.databinding.ActivitySignupBinding
import android.os.Build
import androidx.annotation.RequiresApi
import java.security.Key
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.ExperimentalEncodingApi

class Signup : AppCompatActivity() {
    // variable declarations
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference // for database connection

    private val key: String = "mysecretkey12345" // You can use any tech.ayodele.gravity.getKey
    private val secretKeySpec: Key = SecretKeySpec(key.toByteArray(), "AES")

    @RequiresApi(Build.VERSION_CODES.O) // required for password encryption
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        set up view binding

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initalise firebase and database reference
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("Users")

//        bind ui elements

        binding.signupBtn.setOnClickListener {
            val email: String = binding.emailET.text.toString()
            val password = binding.passwordET.text.toString()
            val name = binding.nameET.text.toString()
            val weight = binding.weightET.text.toString()
            val height = binding.heightET.text.toString()

            // verify all required fields
            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                val hashedPassword = encryptPassword(password)
                signupUser(name, hashedPassword, email, height.toInt(), weight.toInt())
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }


        }
        //redirect to signin page
        binding.redirectET.setOnClickListener {
            startActivity(Intent(this, Signin::class.java))
            finish()
        }
    }


    // Function to encrypt password

    @SuppressLint("GetInstance")
    @RequiresApi(Build.VERSION_CODES.O)
    fun encryptPassword(input: String): String {
        val trimmedInput = input.trim()
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
        val encryptedBytes = cipher.doFinal(trimmedInput.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }


    //Signup function

    private fun signupUser(
        userName: String,
        password: String,
        email: String,
        height: Int,
        weight: Int
    ) {
        //check for the provided email exits
        databaseReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //if the user exists
                    if (!dataSnapshot.exists()) {
                        val id =
                            databaseReference.push().key // Generate a unique tech.ayodele.gravity.getKey for the new user
                        val userData = UserDetails(id, userName, email, password, height, weight)
                        databaseReference.child(id!!).setValue(userData) // save "userdata"
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this@Signup,
                                    "Sign Up Successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this@Signup, SignupSuccess::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    this@Signup,
                                    "Sign Up Failed: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        Toast.makeText(this@Signup, "Email already taken", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@Signup, error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }


}

