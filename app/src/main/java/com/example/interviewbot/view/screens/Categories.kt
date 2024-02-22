package com.example.interviewbot.view.screens

import android.net.Uri
import android.widget.NumberPicker
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.interviewbot.utils.PreferencesManager
import com.example.interviewbot.view.model.Question
import com.example.interviewbot.utils.Questions
import com.google.gson.Gson

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Categories(
    navController: NavHostController,
) {
    val categories = listOf(
        Questions.JAVA_QUESTIONS,
        Questions.KOTLIN_QUESTIONS,
        Questions.ANDROID_QUESTIONS,
        "Spring Boot",
        "Microservice",
    )
//    val selectedCategories by remember { mutableStateOf(mutableListOf<String()) }
    val selectedCategories = remember { mutableStateOf(arrayListOf<String>()) }

    val questionsCount = remember { mutableStateOf(50) }
    val context = LocalContext.current

    val preferencesManager = remember { PreferencesManager(context) }

    var kotlinQuestions: List<String> = remember { mutableStateListOf() }

    LaunchedEffect(key1 = Unit) {
//        kotlinQuestions = Utility.extractQuestions(Questions.getQuestions(), Questions.KOTLIN_QUESTIONS)
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (!selectedCategories.value.isEmpty()) {
                        val dataJson = Gson().toJson(selectedCategories.value)
                        val encodedJson = Uri.encode(dataJson)
                        navController.navigate("InterviewSpeaker/$encodedJson")
                    } else {
                        Toast.makeText(context, "Select any category", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .padding(bottom = 80.dp, end = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next"
                )
            }
        }
    ) {
        it
        Column(Modifier.fillMaxWidth()) {
            Text(
                text = "Select Categories",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onPrimary)
            )

            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { context ->
                    NumberPicker(context).apply {
                        setOnValueChangedListener { numberPicker, i, i2 ->
                            questionsCount.value = numberPicker.value
                            preferencesManager.saveData("questionsCount", numberPicker.value)
                        }
                        minValue = 10
                        maxValue = 1000
                    }
                }
            )

            LazyColumn() {
                itemsIndexed(categories) { index, category ->
                    val checkedState = remember { mutableStateOf(false) }
                    Card(
                        Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .clickable {
                                checkedState.value = !checkedState.value
                                if (checkedState.value) {
                                    selectedCategories.value.add(category)
                                } else {
                                    selectedCategories.value.remove(category)
                                }
                            }
                    ) {
                        Row(
                            Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = checkedState.value,
                                onCheckedChange = { isChecked ->
                                    checkedState.value = isChecked
                                    if (isChecked) {
                                        selectedCategories.value.add(category)
                                    } else {
                                        selectedCategories.value.remove(category)
                                    }
                                }
                            )
                            Text(
                                text = category,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun CheckBoxWithTextRippleFullRow(
    label: String,
    state: Boolean,
    onStateChange: (Boolean) -> Unit
) {

    // Checkbox with text on right side
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(40.dp)
        .clickable(
            role = Role.Checkbox,
            onClick = {
                onStateChange(!state)
            }
        )
        .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = state,
            onCheckedChange = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label)
    }
}
