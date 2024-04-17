package tech.ayodele.gravity

import android.os.Parcel
import android.os.Parcelable

data class CategoriesResponse(
    val categories: List<Category>
)

data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)



