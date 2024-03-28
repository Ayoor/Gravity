package tech.ayodele.gravity

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize // for sending the user details as object across activities
data class UserDetails(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val gpName: String? = null,
    val gpHospital: String? = null
): Parcelable


