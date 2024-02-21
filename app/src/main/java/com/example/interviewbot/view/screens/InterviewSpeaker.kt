package com.example.interviewbot.view.screens


import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.interviewbot.utils.PreferencesManager
import com.example.interviewbot.utils.Questions
import com.example.interviewbot.utils.Utility
import com.example.interviewbot.view.model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun InterviewSpeaker(selectedCategories: List<String>) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val data = remember { mutableStateOf(preferencesManager.getData("questionsCount", 10)) }

    val textToSpeech = remember { TextToSpeech(context, TextToSpeech.OnInitListener { }) }
//    var categoryList: List<Category> = remember { mutableStateListOf() }
    var categoryList by remember { mutableStateOf(mutableListOf<Category>()) }

    val currentQuestionIndex = remember { mutableStateOf(0) }
    val currentCategory = remember { mutableStateOf(categoryList.firstOrNull()) }

    var questionIndex = remember { mutableStateOf(0) }
    var questionCategory = remember { mutableStateOf("") }


    LaunchedEffect(key1 = Unit) {
        launch(Dispatchers.Default) {
            categoryList = Utility.extractAllQuestions(Questions.getQuestions(), selectedCategories)
        }
    }

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
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Questions Speaker",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(color = Color.White)
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            val (categoryText, questionText, playButton, stopButton, nextButton) = createRefs()

            Text(
                text = currentCategory.value?.categoryName ?: "",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier.constrainAs(categoryText) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            Text(
                text = currentCategory.value?.questionList?.getOrNull(currentQuestionIndex.value)?.text
                    ?: "",
                style = TextStyle(fontSize = 36.sp, fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(questionText) {
                        top.linkTo(categoryText.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(playButton) {
                        bottom.linkTo(parent.bottom, margin = 70.dp)
                        start.linkTo(parent.start)
                    }
            ) {
                Button(
                    onClick = {
                        textToSpeech.speak(
                            currentCategory.value?.questionList?.getOrNull(currentQuestionIndex.value)?.text,
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            null
                        )
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(text = "Play")
                }

                Button(
                    onClick = { textToSpeech.stop() },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(text = "Stop")
                }

                Button(
                    onClick = {
                        currentQuestionIndex.value++
                        if (currentQuestionIndex.value >= (currentCategory.value?.questionList?.size ?: 0)) {
                            currentQuestionIndex.value = 0
                        }

                        if (currentQuestionIndex.value >= (currentCategory.value?.questionList?.size ?: 0)) {
                            currentQuestionIndex.value = 0
                            val currentIndex = categoryList.indexOf(currentCategory.value)
                            val nextIndex =
                                if (currentIndex == categoryList.size - 1) 0 else currentIndex + 1
                            currentCategory.value = categoryList.getOrNull(nextIndex)
                        }
                        textToSpeech.speak(
                            currentCategory.value?.questionList?.getOrNull(currentQuestionIndex.value)?.text,
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            null
                        )
                    }
                ) {
                    Text(text = "Next")
                }
            }
        }
    }
}


@Preview
@Composable
fun InterviewSpeakerPreview() {
    val selectedCategories = listOf(
        Questions.JAVA_QUESTIONS,
        Questions.KOTLIN_QUESTIONS,
        Questions.ANDROID_QUESTIONS
    )
    InterviewSpeaker(selectedCategories)
}