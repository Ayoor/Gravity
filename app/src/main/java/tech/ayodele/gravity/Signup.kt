package tech.ayodele.gravity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import com.google.gson.Gson
import tech.ayodele.gravity.databinding.ActivitySignupBinding
import tech.ayodele.gravity.model.UserDetails
import java.security.Key
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Base64
import java.util.Date
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class Signup : AppCompatActivity() {
    // variable declarations
    private lateinit var binding: ActivitySignupBinding
    private lateinit var prefs: SharedPreferences
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


//        bind ui elements

        binding.signupBtn.setOnClickListener {
            val email: String = binding.emailET.text.toString()
            val password = binding.passwordET.text.toString()
            val name = binding.nameET.text.toString()
            val weight = binding.weightET.text.toString()
            val height = binding.heightET.text.toString()

            if (isInternetAvailable(this)) {
                // verify all required fields
                if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                    val hashedPassword = encryptPassword(password)
                    signupUser(name, hashedPassword, email, height.toInt(), weight.toDouble())
                } else {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }


            } else {
                Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT)
                    .show()
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
        weight: Double
    ) {

        //initalise firebase and database reference
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("Users")

        //check for the provided email exits
        databaseReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        val id = databaseReference.push().key ?: ""
                        val userData = UserDetails(id=id, name = userName, email = email, password = password, height = height, weight = weight)
                        databaseReference.child(id).setValue(userData)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this@Signup,
                                    "Sign Up Successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                updateWeight(id, height, weight)
                                val userdata = UserDetails(
                                    id = id,
                                    name = userName,
                                    height = height,
                                    weight = weight,
                                    email = email
                                )
                                saveUserData(userdata)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateWeight(userID: String, height: Int, weight: Double) {
        val decimalFormat = DecimalFormat("#.##")
        val userWeightF = decimalFormat.format(weight).toDouble()
        val database = FirebaseDatabase.getInstance()
        val weightRef = database.getReference("Weight Data")

        // Check if the user ID exists in the database
        weightRef.child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // User ID does not exist, save the weight data
                    val dateTime = formattedDate()
                    val weightData = WeightData(userID, dateTime, userWeightF, height)
                    weightRef.child(userID).setValue(weightData)

                        .addOnFailureListener { e ->
                            // Failed to save weight data
                            Toast.makeText(
                                this@Signup,
                                "Failed to update weight data: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle onCancelled event
                Toast.makeText(
                    this@Signup,
                    "Database error: ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formattedDate(): String {
        val date = LocalDate.now()
        // Convert LocalDate to LocalDateTime
        val localDateTime = LocalDateTime.of(date, LocalTime.now())

        // Convert LocalDateTime to Date
        val dateObj = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())

        // Define date and time format
        val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        // Format the date and time
        val formattedDate = dateFormat.format(dateObj)
        val formattedTime = timeFormat.format(dateObj)

        // Return the formatted string
        return "$formattedDate, $formattedTime"
    }

    fun saveUserData(user: UserDetails) {
        prefs = getSharedPreferences("saveData", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("existingUser", true)
        val gson = Gson()
        val json = gson.toJson(user)
        editor.putString("userdata", json)
        editor.apply()
    }

    @SuppressLint("ServiceCast")
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }

}




