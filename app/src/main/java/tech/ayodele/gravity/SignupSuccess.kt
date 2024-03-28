package tech.ayodele.gravity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import tech.ayodele.gravity.databinding.ActivitySignupBinding
import tech.ayodele.gravity.databinding.ActivitySignupSuccessBinding

    //this is the page that confirms successful sign up to the user
class SignupSuccess : AppCompatActivity() {
    private lateinit var binding: ActivitySignupSuccessBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_success)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        set up binding
        binding = ActivitySignupSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

// get started button
        binding.getStarted.setOnClickListener{
            val intent = Intent(this, Signin::class.java)
            startActivity(intent)
            finish()
        }
    }
}