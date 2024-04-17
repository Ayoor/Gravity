package tech.ayodele.gravity

import retrofit2.Call
import retrofit2.http.GET

interface ApiServices {
    @GET("categories.php")
    fun fetchData(): Call<CategoriesResponse>
}