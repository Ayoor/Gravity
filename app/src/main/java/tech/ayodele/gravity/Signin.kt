package tech.ayodele.gravity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import tech.ayodele.gravity.databinding.ActivitySigninBinding
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class Signin : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference // for database connection

    private var key: String = "mysecretkey12345" //You can use any tech.ayodele.gravity.getKey
    private var secretKeySpec = SecretKeySpec(key.toByteArray(), "AES")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialise firebase and database reference


        binding.signinBtn.setOnClickListener {
            val email: String = binding.emailET.text.toString()
            val password = binding.passwordET.text.toString()


            // confirm necessary fiels
            if (email.isNotEmpty() && password.isNotEmpty()) {
                //initalise firebase and database reference
                firebaseDatabase = FirebaseDatabase.getInstance()
                databaseReference = firebaseDatabase.getReference("Users")
                signIn(email, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
//        redirect to Signup
        binding.redirectET.setOnClickListener {
            startActivity(Intent(this, Signup::class.java))
            finish()
        }
    }
//   function to re-encrypt the signin password to compare to the database password

    @SuppressLint("GetInstance")
    @RequiresApi(Build.VERSION_CODES.O)
    fun encryptPassword(input: String): String {
        val trimmedInput = input.trim()
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
        val encryptedBytes = cipher.doFinal(trimmedInput.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

//function to handle the sign in

    @RequiresApi(Build.VERSION_CODES.O)
    private fun signIn(email: String, password: String) {
        val hashedPassword = encryptPassword(password)
        val query = databaseReference.orderByChild("email")
            .equalTo(email) //check if the email exists in the database
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) { // user exists
                    for (userDataSnapshot in dataSnapshot.children) {
                        val userData = userDataSnapshot.getValue(UserDetails::class.java)
                        if (userData?.password == hashedPassword) {
                            // Passwords match, proceed with login
                            val intent = Intent(this@Signin, OnBoardingSurvey::class.java)
                            intent.putExtra("userData", userData)
                            startActivity(intent)
                            finish()
                            return
                        }
                    }
                    // Password does not match
                    Toast.makeText(this@Signin, "Incorrect Password", Toast.LENGTH_SHORT).show()
                } else {
                    // User not found
                    Toast.makeText(this@Signin, "User not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                Toast.makeText(this@Signin, "Database error", Toast.LENGTH_SHORT).show()
            }
        })
    }

}