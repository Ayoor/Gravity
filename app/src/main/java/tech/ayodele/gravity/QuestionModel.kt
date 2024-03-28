package tech.ayodele.gravity

data class QuestionModel(val id: Int,
                         val title: String= "",
                         val question: String= "",
                         val options: List<String> = emptyList()
)


