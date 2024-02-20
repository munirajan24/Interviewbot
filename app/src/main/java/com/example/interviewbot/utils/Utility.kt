package com.example.interviewbot.utils

object Utility {

    fun extractQuestions(input: String, type: String): List<String> {
        val lines = input.split("\n")
        val questions = ArrayList<String>()

        var isType = false
        for (line in lines) {
            if (line.trim().startsWith(type)) {
                isType = true
                continue
            }

            if (isType && line.trim().isNotEmpty()) {
                questions.add(line.trim())
            } else if (isType && line.trim().isEmpty()) {
                break
            }
        }

        return questions
    }

}

data class Question(
    val text: String,
    val type: String
)

enum class QuestionType {
    JAVA,
    KOTLIN,
    ANDROID,
    SPRING_BOOT,
    MICROSERVICE
}