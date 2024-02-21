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

    fun extractAllQuestions(input: String, categoryList: List<String>): MutableList<Category> {
        val categories = mutableListOf<Category>()

        categoryList.forEach { category ->
            val questions = extractQuestions(input, category)
            categories.add(Category(category, questions.map { Question(it, category) }))
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