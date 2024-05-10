package tech.ayodele.gravity.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize // for sending the user details as object across activities
data class UserDetails(
    val userType: String? = null,
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val height: Int? = null,
    val weight: Double? = null,
    val gpName: String? = null,
    val gpHospital: String? = null

): Parcelable


