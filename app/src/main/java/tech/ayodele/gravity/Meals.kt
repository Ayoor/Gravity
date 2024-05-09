package tech.ayodele.gravity

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

object Meals {
    private lateinit var preferences: SharedPreferences

    fun getMealDetails(context: Context): List<MealData> {
        val userType = getUserType(context)
        var menulist: List<MealData> = listOf()

        when (userType) {
            "Unavailable" ->   menulist =listOf(
                MealData(
                    "Quick Shrimp Puttanesca",
                    "Because refrigerated fresh pasta cooks much faster than dried pasta, this Italian-inspired pasta dish will be on the table lickety-split! Puttanesca, traditionally made with tomatoes, olives, capers, anchovies and garlic, gets shrimp for extra protein and artichoke hearts to boost the vegetable servings (and the fiber!). If you can't find frozen artichoke hearts, sub in drained canned artichoke hearts.",
                    "Nut-Free, Dairy-Free, Healthy Aging, Low Added Sugar, Soy-Free, High-Fiber, High-Protein, Low-Calorie",
                    NutritionFacts(
                        "390 kcal",
                        "8g",
                        "43g",
                        "37g"
                    ), R.drawable.quick_shrimp
                ),
                MealData(
                    " Sweet Potato & Brussels Sprouts Hash with Chicken Sausage",
                    "Apple-flavored chicken sausage adds flavor and protein in this quick dinner hash that uses a bag of shaved Brussels sprouts as its hearty, high-fiber base. Steaming the cubed sweet potatoes in the microwave cuts way down on total cook time.",
                    "Nut-Free, Dairy-Free, Soy-Free, High-Fiber, High-Protein, Egg-Free, Gluten-Free",
                    NutritionFacts(
                        "345 kcal",
                        "12g",
                        "42g",
                        "19g"
                    ),
                    R.drawable.sweet_potato_brussels_sprout
                ),
                MealData(
                    "Sausage, Pepper & Potato Packets",
                    "Make a complete meal in one foil packet with this easy recipe full of seasoned sausage, peppers and potatoes. This easy dinner is great for grilling at home or prepping ahead and taking along on a camping trip to cook over the coals.",
                    "Nut-Free, Healthy Aging, Healthy Immunity, Soy-Free, Vegetarian, Gluten-Free, Low-Calorie",
                    NutritionFacts(
                        "345 kcal",
                        "13g",
                        "41g",
                        "17g"
                    ),R.drawable.sausage_veggie_packets
                ),
                MealData(
                    "Eggs in Tomato Sauce with Chickpeas & Spinach",
                    "Simmer eggs in a rich tomatoey cream sauce studded with chickpeas and silky spinach for a super-fast vegetarian dinner. Serve with a piece of crusty bread to soak up the sauce. Be sure to use heavy cream; a lower-fat option might curdle when mixed with acidic tomatoes.",
                    "Nut-Free, Healthy Aging, Healthy Immunity, Soy-Free, Vegetarian, Gluten-Free, Low-Calorie",
                    NutritionFacts(
                        "323 kcal",
                        "18g",
                        "26g",
                        "14g"
                    ),R.drawable.eggs_tomato_chickpeas_spinach
                ),
                MealData(
                    "Chicken & Sweet Potato Grill Packets with Peppers & Onions",
                    "Cook your whole meal in a packet on the grill with this easy veggie-loaded recipe. The Mexican-inspired seasoning makes the chicken and veggies taste great served with warm tortillas and your favorite taco toppings for a healthy dinner.",
                    "Diabetes-Appropriate, Nut-Free, Dairy-Free, Healthy Aging Healthy Immunity, Low-Sodium , High-Blood Pressure ,Low-Fat ,Soy-Free ,Heart-Healthy, Egg-Free ,Gluten-Free, Low-Calorie",
                    NutritionFacts(
                        "241 kcal",
                        "3g",
                        "34g",
                        "21g"
                    ),R.drawable.chicken_sweet_potato_packets
                ),
                MealData(
                    "Baked Fish Tacos with Avocado",
                    "Instead of deep-frying, the fish fillets in this quick-and-easy 5-ingredient recipe are coated with a flavorful seasoning blend and baked. Several varieties of flaky white fish can be used for these tacos. When you go to the market to purchase fish, the best strategy is to be flexible and choose the variety that looks freshest that day.",
                    "Diabetes-appropriate, nut-free, dairy-free, healthy aging, low-sodium, soy-free, heart-healthy, egg-free, gluten-free, low-calorie",
                    NutritionFacts(
                        "296 kcal",
                        "13g",
                        "29g",
                        "19g"
                    ),R.drawable.baked_fish_tacos
                ),
                MealData(
                    "Stuffed Eggplant with Couscous & Almonds",
                    "Smoky almonds, meaty eggplant and whole-grain couscous with herbs make this meal plenty satisfying. Harissa gives the creamy sauce a little kick.",
                    "Diabetes-appropriate, dairy-free, low-sodium, low-added sugar, soy-free, high-fiber, heart-healthy, vegetarian, low-calorie",
                    NutritionFacts(
                        "457 kcal",
                        "33g",
                        "35g",
                        "9g"
                    ),R.drawable.stuffed_eggplant
                ),
                MealData(
                    "Pressure-Cooker Grain Bowl",
                    "Whip up this healthy vegetarian grain bowl with ease in your Instant Pot. Pressure-cooking the sweet potato is faster and yields the perfect texture. A drizzle of homemade spicy dressing takes this easy dinner to the next level.",
                    "Dairy-free, healthy pregnancy, healthy aging, healthy immunity, low-sodium, low-added sugar, high blood pressure, soy-free, high-fiber, heart-healthy, vegan, vegetarian, egg-free, gluten-free, low-calorie",
                    NutritionFacts(
                        "514 kcal",
                        "22g",
                        "67g",
                        "15g"
                    ),R.drawable.pressure_cooker_grain_bowl
                ),
                MealData(
                    "Baked Spinach & Feta Pasta",
                    "Feta softens in the oven before it's combined with spinach and pasta, with the pasta cooking right in the baking dish. Enjoy this one-pan pasta with feta dish on its own as a vegetarian main or serve with sautéed chicken breast for a boost of protein.",
                    "Nut-Free, Soy-Free, Vegetarian, Egg-Free",
                    NutritionFacts(
                        "394 kcal",
                        "19g",
                        "45g",
                        "14g"
                    ),R.drawable.baked_spinach_fetapasta),
                MealData(
                    "Black Bean-Quinoa Bowl",
                    "This black bean and quinoa bowl has many of the usual hallmarks of a taco salad, minus the fried bowl. We've loaded it up with pico de gallo, fresh cilantro and avocado plus an easy hummus dressing to drizzle on top.",
                    "Nut-free, dairy-free, healthy pregnancy, healthy aging, healthy immunity, soy-free, high-fiber, vegan, vegetarian, high-protein, egg-free, gluten-free, low-calorie",
                    NutritionFacts(
                        "500 kcal",
                        "16g",
                        "74g",
                        "20g"
                    ),R.drawable.black_bean_quinoa
                )
            )
            "Active User" -> menulist = listOf(
                MealData(
                    "High-Protein Oatmeal with Berries and Nuts",
                    "Provides sustained energy for workouts.",
                    "High-Protein, Low-Calorie, High-Fiber",
                    NutritionFacts(
                        "350 kcal",
                        "12g",
                        "45g",
                        "20g"
                    ), R.drawable.high_protein_oatmeal
                ),
                MealData(
                    "Salmon with Roasted Vegetables and Quinoa",
                    "Offers lean protein and healthy carbohydrates.",
                    "High-Protein, Low-Calorie, High-Fiber",
                    NutritionFacts(
                        "400 kcal",
                        "15g",
                        "35g",
                        "25g"
                    ), R.drawable.salmon_roasted_veggies
                ),
                MealData(
                    "Chicken Breast Stir-Fry with Brown Rice",
                    "Packed with protein and essential nutrients.",
                    "High-Protein, Moderate-Calorie, High-Fiber",
                    NutritionFacts(
                        "380 kcal",
                        "18g",
                        "40g",
                        "22g"
                    ), R.drawable.chicken_stir_fry
                ),
                MealData(
                    "Lentil Soup with Whole-Wheat Bread",
                    "A hearty and vegetarian option with protein and fiber.",
                    "High-Protein, Low-Calorie, High-Fiber",
                    NutritionFacts(
                        "300 kcal",
                        "10g",
                        "50g",
                        "15g"
                    ), R.drawable.lentil_soup
                ),
                MealData(
                    "Greek Yogurt with Fruit and Granola",
                    "A convenient and protein-rich post-workout snack.",
                    "High-Protein, Moderate-Calorie, High-Fiber",
                    NutritionFacts(
                        "250 kcal",
                        "15g",
                        "30g",
                        "10g"
                    ), R.drawable.greek_yogurt
                ),
                MealData(
                    "Turkey Sandwich on Whole-Wheat Bread with Avocado",
                    "A filling and satisfying lunch option.",
                    "High-Protein, Moderate-Calorie, High-Fiber",
                    NutritionFacts(
                        "400 kcal",
                        "20g",
                        "35g",
                        "18g"
                    ), R.drawable.turkey_sandwich
                ),
                MealData(
                    "Black Bean Burgers on Whole-Wheat Buns with Sweet Potato Fries",
                    "A healthy and flavorful vegetarian alternative.",
                    "High-Protein, Moderate-Calorie, High-Fiber",
                    NutritionFacts(
                        "450 kcal",
                        "20g",
                        "40g",
                        "20g"
                    ), R.drawable.black_bean_burger
                ),
                MealData(
                    "Baked Chicken Breast with Roasted Brussels Sprouts and Quinoa",
                    "A well-balanced meal with lean protein and complex carbohydrates.",
                    "High-Protein, Moderate-Calorie, High-Fiber",
                    NutritionFacts(
                        "380 kcal",
                        "18g",
                        "40g",
                        "22g"
                    ), R.drawable.baked_chicken
                ),
                MealData(
                    "Tuna Salad with Whole-Wheat Crackers and a Side Salad",
                    "A quick and protein-rich lunch option.",
                    "High-Protein, Moderate-Calorie, High-Fiber",
                    NutritionFacts(
                        "350 kcal",
                        "20g",
                        "30g",
                        "18g"
                    ), R.drawable.tuna_salad
                ),
                MealData(
                    "Shrimp Scampi with Whole-Wheat Pasta and Steamed Broccoli",
                    "A delicious and nutritious seafood option.",
                    "High-Protein, Moderate-Calorie, High-Fiber",
                    NutritionFacts(
                        "400 kcal",
                        "25g",
                        "35g",
                        "20g"
                    ), R.drawable.shrimp_pasta
                )
            )
            "Passive User" -> menulist = listOf(
                MealData(
                    "Whole-Wheat Pancakes with Fruit and Maple Syrup",
                    "A balanced breakfast option with complex carbohydrates and some protein.",
                    "Moderate-Calorie, High-Fiber",
                    NutritionFacts(
                        "350 kcal",
                        "10g",
                        "50g",
                        "15g"
                    ), R.drawable.whole_wheat_pancakes
                ),
                MealData(
                    "Chicken Caesar Salad with Whole-Wheat Croutons",
                    "A filling salad with protein and healthy fats.",
                    "Moderate-Calorie, High-Protein",
                    NutritionFacts(
                        "400 kcal",
                        "20g",
                        "30g",
                        "18g"
                    ), R.drawable.caesar_salad
                ),
                MealData(
                    "Vegetable Soup with a Whole-Wheat Roll",
                    "A light and nutritious lunch option.",
                    "Low-Calorie, High-Fiber",
                    NutritionFacts(
                        "250 kcal",
                        "8g",
                        "40g",
                        "10g"
                    ), R.drawable.vegetable_soup
                ),
                MealData(
                    "Turkey Chili with a Side of Brown Rice",
                    "A hearty and protein-rich meal.",
                    "Moderate-Calorie, High-Protein",
                    NutritionFacts(
                        "350 kcal",
                        "15g",
                        "40g",
                        "20g"
                    ), R.drawable.turkey_chili
                ),
                MealData(
                    "Greek Yogurt Parfait with Granola and Fruit",
                    "A layered yogurt dish with protein, carbohydrates, and fiber.",
                    "Moderate-Calorie, High-Protein, High-Fiber",
                    NutritionFacts(
                        "300 kcal",
                        "12g",
                        "35g",
                        "15g"
                    ), R.drawable.greek_yogurt_parfait
                ),
                MealData(
                    "Tuna Melt on Whole-Wheat Bread with a Side Salad",
                    "A classic and satisfying lunch option.",
                    "Moderate-Calorie, High-Protein",
                    NutritionFacts(
                        "400 kcal",
                        "18g",
                        "35g",
                        "20g"
                    ), R.drawable.tuna_melt
                ),
                MealData(
                    "Chicken Stir-Fry with Brown Rice",
                    "A customizable and flavorful dish with lean protein and essential nutrients.",
                    "Moderate-Calorie, High-Protein, High-Fiber",
                    NutritionFacts(
                        "380 kcal",
                        "20g",
                        "40g",
                        "18g"
                    ), R.drawable.chicken_stir_fry
                ),
                MealData(
                    "Lentil Pasta with Marinara Sauce and a Side Salad",
                    "A vegetarian option with protein and fiber.",
                    "Moderate-Calorie, High-Fiber",
                    NutritionFacts(
                        "350 kcal",
                        "12g",
                        "45g",
                        "15g"
                    ), R.drawable.lentil_pasta
                ),
                MealData(
                    "Salmon with Roasted Asparagus and Quinoa",
                    "A simple and nutritious meal with omega-3 fatty acids.",
                    "Moderate-Calorie, High-Protein, High-Fiber",
                    NutritionFacts(
                        "400 kcal",
                        "20g",
                        "30g",
                        "25g"
                    ), R.drawable.salmon_asparagus
                ),
                MealData(
                    "Bean Burrito Bowl with Brown Rice and Avocado",
                    "A plant-based option with protein, fiber, and healthy fats.",
                    "Moderate-Calorie, High-Protein, High-Fiber",
                    NutritionFacts(
                        "380 kcal",
                        "18g",
                        "35g",
                        "20g"
                    ), R.drawable.bean_burrito_bowl
                )
            )
            "Sedentary User" -> menulist = listOf(
                MealData(
                    "Fruit and Vegetable Smoothie with Protein Powder",
                    "A convenient and nutritious breakfast or snack option.",
                    "Low-Calorie, High-Fiber, High-Protein",
                    NutritionFacts(
                        "250 kcal",
                        "8g",
                        "40g",
                        "10g"
                    ), R.drawable.fruit_veggie_smoothie
                ),
                MealData(
                    "Egg Scramble with Vegetables and Whole-Wheat Toast",
                    "A filling breakfast with protein and healthy fats.",
                    "Moderate-Calorie, High-Protein",
                    NutritionFacts(
                        "300 kcal",
                        "15g",
                        "25g",
                        "12g"
                    ), R.drawable.egg_scramble
                ),
                MealData(
                    "Chicken or Lentil Soup with a Side Salad",
                    "A light and protein-rich lunch option.",
                    "Low-Calorie, High-Protein",
                    NutritionFacts(
                        "280 kcal",
                        "12g",
                        "35g",
                        "10g"
                    ), R.drawable.lentil_soup
                ),
                MealData(
                    "Tuna Salad Sandwich on Whole-Wheat Bread with Lettuce and Tomato",
                    "A classic and easy-to-digest lunch option.",
                    "Moderate-Calorie, High-Protein",
                    NutritionFacts(
                        "320 kcal",
                        "14g",
                        "30g",
                        "15g"
                    ), R.drawable.tuna_sandwich
                ),
                MealData(
                    "Greek Yogurt with Berries and Chia Seeds",
                    "A protein-rich snack with fiber and healthy fats.",
                    "Low-Calorie, High-Protein, High-Fiber",
                    NutritionFacts(
                        "200 kcal",
                        "10g",
                        "25g",
                        "8g"
                    ), R.drawable.greek_yogurt
                ),
                MealData(
                    "Baked Chicken Breast with Steamed Vegetables",
                    "A simple and low-calorie meal.",
                    "Low-Calorie, High-Protein",
                    NutritionFacts(
                        "300 kcal",
                        "20g",
                        "15g",
                        "10g"
                    ), R.drawable.baked_chicken
                ),
                MealData(
                    "Salmon with Roasted Broccoli",
                    "A heart-healthy option with omega-3 fatty acids.",
                    "Low-Calorie, High-Protein, High-Fiber",
                    NutritionFacts(
                        "320 kcal",
                        "18g",
                        "25g",
                        "20g"
                    ), R.drawable.salmon_broccoli
                ),
                MealData(
                    "Turkey Chili with a Side of Vegetables",
                    "A protein-rich and low-calorie meal.",
                    "Moderate-Calorie, High-Protein",
                    NutritionFacts(
                        "340 kcal",
                        "16g",
                        "30g",
                        "18g"
                    ), R.drawable.turkey_chili
                ),
                MealData(
                    "Lentil and Vegetable Stew",
                    "A vegetarian option with protein and fiber.",
                    "Low-Calorie, High-Protein, High-Fiber",
                    NutritionFacts(
                        "280 kcal",
                        "14g",
                        "35g",
                        "12g"
                    ), R.drawable.lentil_soup
                ),
                MealData(
                    "Baked Cod with Roasted Sweet Potato and Green Beans",
                    "A balanced meal with lean protein, complex carbohydrates, and vegetables.",
                    "Low-Calorie, High-Protein, High-Fiber",
                    NutritionFacts(
                        "350 kcal",
                        "20g",
                        "30g",
                        "25g"
                    ), R.drawable.baked_cod
                )
            )
        }
        return menulist
    }

     fun getUserType(context: Context): String {
        preferences = context.getSharedPreferences("saveData", Context.MODE_PRIVATE)
        val userDataJson = preferences.getString("userdata", null)
        val gson = Gson()
        val userData = gson.fromJson(userDataJson, UserDetails::class.java)
        return userData.userType.toString()
    }
}