package com.example.interviewbot.view.old

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.interviewbot.R
import com.example.interviewbot.view.theme.InterviewBotTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InterviewBotTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    login()
                }
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun login() {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.vertical1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop
        )
        Column(
            Modifier
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_asking),
                contentDescription = null,
                Modifier.size(80.dp),
            )

            SimpleTextField("Username")
            SimpleTextField("Password")
            loginButton()

            Divider(
                color = Color.Blue.copy(alpha = 0.3f),
                thickness = 1.dp,
                modifier = Modifier.padding(top = 48.dp)
            )

        }

    }

}

@ExperimentalMaterial3Api
@Composable
fun TextInput1() {
    var value: String by remember { mutableStateOf("") }
    TextField(value = value, onValueChange = { value = it })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTextField(fieldName: String) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    TextField(
        value = text,
        onValueChange = { newText ->
            text = newText
        },
        label = { Text(text = fieldName) },
    )
}

@Composable
fun loginButton() {
    var mContext = LocalContext.current

    Button(onClick = {
        mContext.startActivity(Intent(mContext, InterviewActivity::class.java))
        Toast.makeText(mContext, "Interview Started", Toast.LENGTH_SHORT).show()
    }, shape = RoundedCornerShape(20.dp)) {
        Image(
            painter = painterResource(id = R.drawable.ic_right), contentDescription = null,
            modifier = Modifier.size(20.dp)
        )

        Text(text = "Login", Modifier.padding(start = 10.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    InterviewBotTheme {
        login()
    }
}