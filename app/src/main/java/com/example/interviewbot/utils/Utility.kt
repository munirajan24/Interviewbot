package com.example.interviewbot.utils

import com.example.interviewbot.view.model.Category
import com.example.interviewbot.view.model.Question

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

    fun extractAllQuestions(input: String, categoryList: List<String>, count: Int, randomize: Boolean): MutableList<Category> {
        val categories = mutableListOf<Category>()
        val questionsPerCategory = count / categoryList.size

        categoryList.forEach { category ->
            val questions = extractQuestions(input, category)
            val selectedQuestions = if (randomize) questions.shuffled() else questions
            val selectedQuestionsForCategory = selectedQuestions.take(questionsPerCategory)
            categories.add(Category(category, selectedQuestionsForCategory.map { Question(it, category) }))
        }

        return categories
    }


}

enum class QuestionType {
    JAVA,
    KOTLIN,
    ANDROID,
    SPRING_BOOT,
    MICROSERVICE
}