package com.dreavee.interviewbot.view.old

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults.shape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.dreavee.interviewbot.view.theme.InterviewBotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InterviewBotTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(width = 2.dp, color = Color.Black)
                            .shadow(elevation = 5.dp, shape = RoundedCornerShape(10.dp))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        SimpleText("Interview Bot")
                        GetInText("START")

                    }
                }
            }


        }
    }
}


@Composable
fun SimpleText(displayText: String) {
    Box(
        modifier = Modifier
            .padding(15.dp)
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(10.dp))
            .background(color = Color.White, shape = shape)
            .fillMaxWidth()
            .height(50.dp),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = displayText,
            style = TextStyle(
                color = Color.Blue,
                fontSize = 25.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.W800,
                fontStyle = FontStyle.Italic,
                letterSpacing = 0.1.em,
                shadow = Shadow(
                    color = Color.LightGray,
                    offset = Offset(10f, 5f),
                    blurRadius = 5f
                )
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(width = 2.dp, color = Color.Black)
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(10.dp))
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        SimpleText("Interview Bot")
        GetInText("START")

    }

}

@Composable
fun GetInText(s: String) {
    val mContext = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ElevatedButton(
            onClick =
            {
                mContext.startActivity(Intent(mContext, LoginActivity::class.java))
//                mContext.startActivity(Intent(mContext, InterviewActivity::class.java))
            }, Modifier.padding(15.dp)
        ) {
            Text(text = s)
        }
    }

}
