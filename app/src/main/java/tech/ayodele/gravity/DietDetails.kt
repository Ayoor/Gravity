package tech.ayodele.gravity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import tech.ayodele.gravity.databinding.ActivityDietDetailsBinding

class DietDetails : AppCompatActivity() {
    private lateinit var binding: ActivityDietDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_diet_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //set up binding
        binding = ActivityDietDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val meal = intent.getStringExtra("meal")
        val description = intent.getStringExtra("description")
        val imageResId = intent.getIntExtra("imageResId", R.drawable.burger)
        val mealKcal = intent.getStringExtra("mealkcalories")
        val mealProtein = intent.getStringExtra("mealprotein")
        val mealFat = intent.getStringExtra("mealfat")
        val mealCarbs = intent.getStringExtra("mealcarbs")

        // Set the retrieved values to the appropriate views in your layout
        val mealTextView: TextView = findViewById(R.id.mealName)
        val descriptionTextView: TextView = findViewById(R.id.mealDescription)
        val imageView: ImageView = findViewById(R.id.imageView)

        mealTextView.text = meal
        descriptionTextView.text = description
        imageView.setImageResource(imageResId)
        binding.mealCarb.text = mealCarbs
        binding.mealProtein.text = mealProtein
        binding.mealFat.text = mealFat
        binding.mealKcal.text = mealKcal
    }
}