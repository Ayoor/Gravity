package tech.ayodele.gravity.model

data class CommunityItems(
    val topic: String,
    val description: String,
    var commentCount: Int,
    val link: String,
)
