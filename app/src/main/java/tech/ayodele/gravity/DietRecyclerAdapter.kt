package tech.ayodele.gravity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DietRecyclerAdapter(
    private val meals: List<MealData>,
    private val context: Context
) : RecyclerView.Adapter<DietRecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.diet_items, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mealData = meals[position]
        holder.bind(mealData)
    }

    override fun getItemCount(): Int {
        return meals.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mealTextView: TextView = itemView.findViewById(R.id.Meal)
//        private val descriptionTextView: TextView = itemView.findViewById(R.id.description)
        private val imageView: ImageView = itemView.findViewById(R.id.mealImg)

        fun bind(mealData: MealData) {
            mealTextView.text = mealData.meal
//            descriptionTextView.text = mealData.description
            imageView.setImageResource(mealData.img)

            // Set click listener to the item
            itemView.setOnClickListener {
                // Create an Intent to start the new activity
                val intent = Intent(context, DietDetails::class.java)
                // Pass the details as extras
                intent.putExtra("meal", mealData.meal)
                intent.putExtra("description", mealData.description)
                intent.putExtra("imageResId", mealData.img)
                intent.putExtra("mealkcalories", mealData.nutritionFacts.calories)
                intent.putExtra("mealfat", mealData.nutritionFacts.fat)
                intent.putExtra("mealcarbs", mealData.nutritionFacts.carbs)
                intent.putExtra("mealprotein", mealData.nutritionFacts.protein)
                // Start the new activity
                context.startActivity(intent)
            }
        }
    }
}
