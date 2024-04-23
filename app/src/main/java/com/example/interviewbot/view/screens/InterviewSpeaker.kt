package com.example.interviewbot.view.screens


import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.toLowerCase
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

    LaunchedEffect(key1 = Unit) {
        launch(Dispatchers.Default) {
            categoryList = Utility.extractAllQuestions(
                Questions.getQuestions(),
                selectedCategories,
                data.value,
                true
            )
            currentCategory.value = categoryList.firstOrNull()
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

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(selectedCategories) { _, category ->
                Box(
                    modifier = Modifier
//                        .padding(horizontal =  4.dp, vertical = 4.dp)
                        .background(
                            if (currentCategory.value?.categoryName == category) MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.1f
                            ) else Color.Transparent
                        )
                        .border(
                            width = 1.dp,
                            color = if (currentCategory.value?.categoryName == category) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Button(
                        modifier = Modifier
                            .padding(horizontal = 3.dp, vertical = 1.dp),
                        onClick = {
                            // Update the current category when a button is clicked
                            currentCategory.value =
                                categoryList.find { it.categoryName == category }
                            currentQuestionIndex.value = 0
                            textToSpeech.speak(
                                "${category} selected".lowercase(),
                                TextToSpeech.QUEUE_FLUSH,
                                null,
                                null
                            )
                        }
                    ) {
                        Text(text = category)
                    }
                }
            }
        }
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            val (categoryText, questionText, playButtonRow, nextButtonRow) = createRefs()

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
                    .constrainAs(playButtonRow) {
                        bottom.linkTo(nextButtonRow.top, margin = 16.dp)
                        start.linkTo(parent.start)
                    }
            ) {
                Button(
                    onClick = {
                        val speech: String? = currentCategory.value?.let { category ->
                            when {
                                !category.questionList.isNullOrEmpty() -> {
                                    category.questionList?.getOrNull(currentQuestionIndex.value)?.text
                                }

                                category.questionList.isNullOrEmpty() -> {
                                    "There are no questions"
                                }

                                currentQuestionIndex.value >= category.questionList.size -> {
                                    "This topic is over. Let's move to the next topic"
                                }

                                else -> {
                                    "There are no questions"
                                }
                            }
                        }

                        textToSpeech.speak(
                            speech,
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
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(nextButtonRow) {
                        bottom.linkTo(parent.bottom, margin = 70.dp)
                        start.linkTo(parent.start)
                    }
            ) {
                Button(
                    onClick = {
                        val speech: String? = currentCategory.value?.let { category ->
                            if (currentQuestionIndex.value == 0) {
                                "This is the first question"
                            } else {
                                currentQuestionIndex.value--
                                val questionList = category.questionList
                                questionList?.getOrNull(currentQuestionIndex.value)?.text
                            }
                        }

                        textToSpeech.speak(
                            speech,
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            null
                        )
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(text = "Previous")
                }

                Button(
                    onClick = {

                        val speech: String? = currentCategory.value?.let { category ->
                            currentQuestionIndex.value++
                            val questionList = category.questionList
                            val questionIndex = currentQuestionIndex.value


                            if (!questionList.isNullOrEmpty() && questionIndex >= questionList.size) {
                                categoryList.indexOf(category).let { currentIndex ->
                                    val nextIndex =
                                        if (currentIndex == categoryList.size - 1) 0 else currentIndex + 1
                                    currentCategory.value = categoryList.getOrNull(nextIndex)
                                }
                            }

                            when {
                                questionList.isNullOrEmpty() -> "There are no questions"
                                questionIndex >= questionList.size -> "This topic is over. Let's move to the next topic"
                                else -> questionList[questionIndex].text
                            }
                        }
                        textToSpeech.speak(
                            speech,
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