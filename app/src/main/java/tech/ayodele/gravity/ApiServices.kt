package tech.ayodele.gravity

import retrofit2.Call
import retrofit2.http.GET

import retrofit2.http.Query

interface ApiServices {
    @GET("/api/recipes/v2")
    fun fetchData(
        @Query("type") type: String = "public",
        @Query("beta") beta: Boolean = true,
        @Query("app_id") appId: String = "35d71082",
        @Query("app_key") appKey: String = "5f4c6269f577dec0468f8a3d1cb72f5e",
        @Query("diet") diet: List<String> = listOf("high-protein", "low-carb", "low-fat"),
        @Query("health") health: List<String> = listOf("alcohol-free", "low-sugar"),
        @Query("dishType") dishType: String = "Main course",
        @Query("calories") calories: String = "100-400",
        @Query("imageSize") imageSize: String = "REGULAR"
    ): Call<RecipeInfo>
}
