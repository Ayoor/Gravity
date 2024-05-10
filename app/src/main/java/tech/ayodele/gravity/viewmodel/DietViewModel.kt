package tech.ayodele.gravity.view

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tech.ayodele.gravity.MealData
import tech.ayodele.gravity.Meals

class DietViewModel(private val context: Context) : ViewModel() {
    private val _recommendedMeals = MutableLiveData<List<MealData>>()
    val recommendedMeals: LiveData<List<MealData>> = _recommendedMeals

    init {
        loadRecommendedMeals()
    }

    private fun loadRecommendedMeals() {
        val meals = Meals.getMealDetails(context)
        _recommendedMeals.value = meals
    }

    fun getUserType(context: Context): String {
        return Meals.getUserType(context)
    }
}
