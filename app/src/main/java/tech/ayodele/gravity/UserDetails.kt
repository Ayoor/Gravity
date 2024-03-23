package tech.ayodele.gravity

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class UserDetails(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val height: Int? = null,
    val weight: Int? = null
): Parcelable


