package tech.ayodele.gravity

object SurveyData {
    fun getSurveyData(): List<QuestionModel> {
        val allQuestions: MutableList<QuestionModel> = mutableListOf()
        allQuestions.add(
            QuestionModel(
                1,
                "Physical Activity Level",
                "How would you describe your level of physical activity in a typical day?",
                mutableListOf(
                    "Sedentary (little to no physical activity)",
                    "Lightly active (minimal physical activity)",
                    "Moderately active (regular exercise or physical activity)",
                    "Very active (intense exercise or physical labor)"
                )
            )
        )

        allQuestions.add(
            QuestionModel(
                2,
                "Accessibility of Healthy Food Options",
                "How easy is it for you to access healthy and affordable food options where you live?",
                mutableListOf(
                    "Very Easily",
                    "Easily",
                    "Normal",
                    "Difficult",
                    "Very Difficult"
                )
            )
        )

        allQuestions.add(
            QuestionModel(
                3,
                "Discussing Health with Friends and Family",
                "Do you feel comfortable talking to friends and family about your health and well-being?",
                mutableListOf(
                    "Very Comfortable",
                    "Somewhat Comfortable",
                    "Neutral",
                    "Somewhat Uncomfortable",
                    "Very Uncomfortable"
                )
            )
        )

        allQuestions.add(
            QuestionModel(
                4,
                "Financial Challenges",
                "Do you face any financial challenges that make it difficult to access healthy food or gym memberships?",
                mutableListOf("Yes", "No")
            )
        )

        allQuestions.add(
            QuestionModel(
                5,
                "Eating Lifestyle",
                "How would you describe your eating lifestyle?",
                mutableListOf(
                    "Balanced and healthy",
                    "Excessive consumption of processed foods",
                    "Regular consumption of energy-dense foods",
                    "Eating disorders or irregular eating patterns"
                )
            )
        )

        allQuestions.add(
            QuestionModel(
                6,
                "Smoking and Alcohol Habits",
                "Do you smoke or drink alcohol regularly?",
                mutableListOf("Yes", "No")
            )
        )

        allQuestions.add(
            QuestionModel(
                7,
                "Frequency of Physical Activity",
                "How often do you engage in physical activity?",
                mutableListOf(
                    "Regularly (several times a week)",
                    "Occasionally (once or twice a week)",
                    "Rarely (less than once a week)",
                    "Never"
                )
            )

        )

        allQuestions.add(
            QuestionModel(
                8,
                "Recent Lifestyle Changes and Their Impact",
                "Have you experienced any recent lifestyle changes that may have impacted your weight or overall well-being?",
                mutableListOf("Yes", "No")

            )
        )
        return allQuestions
    }

}