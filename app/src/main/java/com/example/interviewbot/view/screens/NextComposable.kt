package com.example.interviewbot.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.interviewbot.utils.PreferencesManager
import com.example.interviewbot.view.model.Question
import com.example.interviewbot.utils.Questions
import com.example.interviewbot.utils.Utility


@Composable
fun NextComposable(selectedCategories: List<Question>) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val data = remember { mutableStateOf(preferencesManager.getData("questionsCount", 10)) }

//    var kotlinQuestions: List<String> = remember { mutableStateListOf() }
//
//    LaunchedEffect(key1 = Unit) {
//        launch(Dispatchers.IO) {
//            kotlinQuestions = Utility.extractQuestions(Questions.getQuestions(), Questions.KOTLIN_QUESTIONS)
//        }
//    }

    Column {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Questions List",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
        LazyColumn {
            selectedCategories.forEach { category ->
                val questions = Utility.extractQuestions(Questions.input, category.type)
                items(selectedCategories.size) {
                    it
                    if (!questions.isEmpty()) {
                        Text(
                            text = category.type,
                            style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                items(questions) { question ->
                    Text(
                        text = question,
                        style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
