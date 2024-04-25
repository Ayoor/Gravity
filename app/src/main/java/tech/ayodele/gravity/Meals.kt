package tech.ayodele.gravity

object Meals {
    fun getMealDetails(): List<MealData> {
        return listOf(
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
    }
}