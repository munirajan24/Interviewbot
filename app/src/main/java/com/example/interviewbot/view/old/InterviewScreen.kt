package com.example.interviewbot.view.old

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.WorkManager
import com.example.interviewbot.R
import com.example.interviewbot.utils.EncryptionUtils
import com.example.interviewbot.viewmodel.MyViewModel
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.Locale
import java.util.Objects


class InterviewScreen : AppCompatActivity(), OnInitListener {
    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }

    // on below line we are creating variables
    // for text view and image view
    lateinit var outputTV: TextView
    lateinit var txtQuestion: TextView
    lateinit var micIV: ImageView

    // on below line we are creating a constant value
    private val REQUEST_CODE_SPEECH_INPUT = 1

    var currentQuestion: String = "Welcome"
    val questionList = ArrayList<String>()

    //    private lateinit var utteranceProgressListener: UtteranceProgressListener
    val ttsParams = Bundle()
    lateinit var mUtteranceId: String

    private lateinit var tts: TextToSpeech
    private lateinit var workManager: WorkManager

    private val viewModel: MyViewModel by viewModels()

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var playIB: ImageButton
    private lateinit var idIBPlayNext: ImageButton
    private lateinit var pauseIB: ImageButton
    private lateinit var random: Button
    private lateinit var btnJava: Button
    private lateinit var btnKotlin: Button
    private lateinit var btnAndroid: Button
    private lateinit var progress: ProgressBar
    var position = 0
    var previousID = ""

    private lateinit var speechRecognizer: SpeechRecognizer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interview_screen)

        tts = TextToSpeech(this, this)
        ttsParams.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "")

        mediaPlayer = MediaPlayer()
        playIB = findViewById(R.id.idIBPlay)
        idIBPlayNext = findViewById(R.id.idIBPlayNext)
        pauseIB = findViewById(R.id.idIBPause)
        random = findViewById(R.id.random)
        btnJava = findViewById(R.id.Java)
        btnKotlin = findViewById(R.id.kotlin)
        btnAndroid = findViewById(R.id.android)
        progress = findViewById(R.id.progressYellow)

        // Check and request the necessary permissions
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO_PERMISSION
            )
        } else {
            initializeSpeechRecognizer()
        }
        val txtTitle = findViewById<Button>(R.id.title)
        initializeWorkManager()
        setQuestionsListKotlin()
        txtTitle!!.setOnClickListener {
            currentQuestion = questionList[position]
//            askQuestion(currentQuestion)
            callTtsApi(currentQuestion)
        }

        playIB.setOnClickListener {
            currentQuestion = questionList[position]
            askQuestion(currentQuestion)
        }
        btnJava.setOnClickListener {
            askQuestion("Java topic selected")
            setQuestionsListJava()
        }
        btnKotlin.setOnClickListener {
            askQuestion("Kotlin topic selected")
            setQuestionsListKotlin()
        }
        btnAndroid.setOnClickListener {
            askQuestion("Android topic selected")
            setQuestionsListAndroid()
        }
        idIBPlayNext.setOnClickListener {
            Log.d("idIBPlayNext", "position: $position")
            Log.d("idIBPlayNext", "questionList.size: $questionList.size")
            if (position <= questionList.size - 2) {
                position += 1
                currentQuestion = questionList[position]
                askQuestion(currentQuestion)
//            callTtsApi(currentQuestion)
            } else {
                askQuestion("you have completed all questions... Well done")
            }
        }
        pauseIB.setOnClickListener {
//            pauseAudio()
            if (tts.isSpeaking) {
                tts.stop()
            }
        }
        random.setOnClickListener {
            questionList.shuffle();
        }

        //---------------------------------------------
        // initializing variables of list view with their ids.
        outputTV = findViewById(R.id.idTVOutput)
        txtQuestion = findViewById(R.id.txtQuestion)
        micIV = findViewById(R.id.idIVMic)

        // on below line we are adding on click
        // listener for mic image view.
        micIV.setOnClickListener {
//            recordFromMic()
            startSpeechRecognition()
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeSpeechRecognizer()
            } else {
                Toast.makeText(
                    this,
                    "Permission to record audio denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "") // Provide an empty string as the prompt

        speechRecognizer.startListening(intent)
    }

    private fun askQuestion(textToAsk: String) {
        txtQuestion.text = textToAsk
        tts.language = Locale.US

        Toast.makeText(this, "Speaking", Toast.LENGTH_SHORT).show()
        tts.speak(textToAsk, TextToSpeech.QUEUE_ADD, ttsParams, null)
    }

    private fun recordFromMic() {

        // on below line we are calling speech recognizer intent.
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

        // on below line we are passing language model
        // and model free form in our intent
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )

        // on below line we are passing our
        // language as a default language.
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        )

        // on below line we are specifying a prompt
        // message as speak to text on below line.
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

        // on below line we are specifying a try catch block.
        // in this block we are calling a start activity
        // for result method and passing our result code.
        try {
            ActivityCompat.startActivityForResult(this, intent, REQUEST_CODE_SPEECH_INPUT, null)
        } catch (e: Exception) {
            // on below line we are displaying error message in toast
            Toast
                .makeText(
                    this@InterviewScreen, " " + e.message,
                    Toast.LENGTH_SHORT
                )
                .show()
        }
    }

    // on below line we are calling on activity result method.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // in this method we are checking request
        // code with our result code.
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            // on below line we are checking if result code is ok
            if (resultCode == RESULT_OK && data != null) {

                // in that case we are extracting the
                // data from our array list
                val res: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                // on below line we are setting data
                // to our output text view.
                outputTV.setText(
                    Objects.requireNonNull(res)[0]
                )
            }
        }
    }

    override fun onInit(status: Int) {
//        Toast.makeText(this@InterviewScreen, "OnInit: $status", Toast.LENGTH_SHORT).show()

        if (status == TextToSpeech.SUCCESS) {
            tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String) {
                    // Called when TTS starts speaking an utterance
                    Log.d("TTS", "Started speaking: $utteranceId")
                    mUtteranceId = utteranceId;
                    Toast.makeText(
                        this@InterviewScreen,
                        "onStart: $utteranceId",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onDone(utteranceId: String) {
                    // Called when TTS finishes speaking an utterance
                    Log.d("TTS", "Finished speaking: $utteranceId")
                    mUtteranceId = utteranceId;
                    Toast.makeText(
                        this@InterviewScreen,
                        "onDone: $utteranceId",
                        Toast.LENGTH_SHORT
                    )
                }

                override fun onError(utteranceId: String) {
                    // Called when there is an error during TTS
                    Log.e("TTS", "Error speaking: $utteranceId")
                    mUtteranceId = utteranceId;
                    Toast.makeText(
                        this@InterviewScreen,
                        "onError: $utteranceId",
                        Toast.LENGTH_SHORT
                    )
                }
            })
        }
    }


//    override fun onStart(string: String?) {
//        Toast.makeText(this@InterviewScreen, "onStart: $string", Toast.LENGTH_SHORT)
//            .show()
//    }
//
//    override fun onDone(string: String?) {
//        Toast.makeText(this@InterviewScreen, "onDone: $string", Toast.LENGTH_SHORT)
//            .show()
//    }
//
//    override fun onError(string: String?) {
//        Toast.makeText(this@InterviewScreen, "onError: $string", Toast.LENGTH_SHORT)
//            .show()
//    }

    private fun setQuestionsListAndroid() {
        questionList.clear()
        position = 0
        currentQuestion = ""
        questionList.add("What is the Android architecture?")
        questionList.add("Android Architecture components?")
        questionList.add("✍️ What is Linux kernel in Android Architecture?")
        questionList.add(" What does Android ART mean?")
        questionList.add("What is fragment in Android?")
        questionList.add("What is Android Jetpack components.?")
        questionList.add("Tell all the Android Jetpack components.?")
        questionList.add("What is meant by livedata?")
        questionList.add("LiveData setValue vs postValue?")
        questionList.add("Advantages of Using LiveData?")
        questionList.add("What is service?")
        questionList.add("How to notify UI from the service?")
        questionList.add("What is BoundService?")
        questionList.add("What is IntentService?")
        questionList.add("What is pending intent?")
        questionList.add("Difference between serializable and parcelable?  Which is best approach in Android ?")
        questionList.add("What is the difference between a regular .png and a nine-patch image?")
        questionList.add("Recyclerview vs listview?")
        questionList.add("✍️What is meant by viewholder pattern?")
        questionList.add("View vs Viewgroup?")
        questionList.add("how to optimize recyclerview android?")
        questionList.add("✍️What is Okhttp intercepter in Android?")
        questionList.add("✍️How  http caching will work in android?")
        questionList.add("Can you tell me about RxJava? and  How it works? And its functionalities?")
        questionList.add("How will you handle error in RxJava?")
        questionList.add("How custom error handler works?")
        questionList.add("Tell activity lifecycle when switching to another activity?")
        questionList.add("Tell activity lifecycle when Onbackpress->?")
        questionList.add("On Home press lifecycle ->?")
        questionList.add("What happen when orientation changed in lifecycle?")
        questionList.add("Why we set setContentView in OnCreate not in onStart?")
        questionList.add("What is meant by daggerHow it works?")
        questionList.add("What is content provider?")
        questionList.add("How to pass data to another activity?")
        questionList.add("How to pass data to another fragment?")
        questionList.add("How to pass ArrayList to next activity?")
        questionList.add("What is MVVM pattern? and Explain?")
        questionList.add("What is ViewModel in MVVM?")
        questionList.add("What is Model in MVVM?")
        questionList.add("What is Repository in MVVM?")
        questionList.add("Steps to create MVVM in Android using Kotlin?")
        questionList.add("Alternatives for AsyncTask?")
        questionList.add("difference between Thread and Handler?")
        questionList.add("What is handler?")
        questionList.add("View Binding Vs databinding?")
        questionList.add("Features of Constraint layout?")
        questionList.add("How to make ConstraintLayout work with percentage values?")
        questionList.add("Barrier in constraint layout?")
        questionList.add("What is Chain in Constraint layout?")
        questionList.add("What is dimension ratio?")
        questionList.add("What are major Jetpack components?")
        questionList.add("What are Jetpack components we can usually use?")
        questionList.add("Advantages of jetpack components?")
        questionList.add("What is navigation components in jetpack?")
        questionList.add("Major Parts of Jetpack Navigation components?")
        questionList.add("What is Navigation Graph?")
        questionList.add("What is NavHost?")
        questionList.add("What is NavController?")
        questionList.add("What is room database?")
        questionList.add("Primary components of Room Database?")
        questionList.add("What is Android Activity “launchMode”?")
        questionList.add("What is ADB?")
    }

    private fun setQuestionsListJava() {
        questionList.clear()
        position = 0
        currentQuestion = ""
        questionList.add("What is OOPs?")
        questionList.add("What is class?")
        questionList.add("What is Object?")
        questionList.add("What is Inheritance and types?")
        questionList.add("What is Polymorphism and types?")
        questionList.add("What is Abstraction?")
        questionList.add("What is Encapsulation?")
        questionList.add("What are literals and identifiers in java?")
        questionList.add("What is singleton class?")
        questionList.add("JRE vs JDK?")
        questionList.add("Can you compile with JRE?")
        questionList.add("What is thread?")
        questionList.add("What is thread lifecycle?")
        questionList.add("Map vs set vs hashtable difference?")
        questionList.add("Map vs HashMap vs LinkedHashMap vs TreeMap?")
        questionList.add("List, Set, and Vector Difference?")
        questionList.add("What is vector?")
        questionList.add("HashSet vs TreeSet?")
        questionList.add("Difference between flatMap and map?")
        questionList.add("Types of Complexity in java?")
        questionList.add("What is time complexity?")


    }

    private fun setQuestionsListKotlin() {
        questionList.clear()
        position = 0
        currentQuestion = ""
        questionList.add("What is Kolin?")
        questionList.add("Difference between kotlin and java?")
        questionList.add("What is companion object?")
        questionList.add("What is higher order function?")
        questionList.add("What is null safety?")
        questionList.add("What does ?: Means in Kotlin?")
        questionList.add("Difference b/w Safe calls(?.) vs Null checks(!!) in Kotlin?")
        questionList.add("What is data class?")
        questionList.add("What is string interpolation?")
        questionList.add("Val vs var difference in Kotlin?")
        questionList.add("Val vs const difference in Kotlin?")
        questionList.add("What is destructuring?")
        questionList.add("When to use lateinit keyword?")
        questionList.add("How to check if a \" lateinit \" variable has been initialized?")
        questionList.add("What is lazy?")
        questionList.add("What is the difference between lateinit and lazy kotlin?")
        questionList.add("Const equivalent in Java?")
        questionList.add("What is Visibility modifiers in kotlin?")
        questionList.add("What are the different class declaration / inheritance")
        questionList.add("modifiers in kotlin?")
        questionList.add("What is inner in kotlin?")
        questionList.add("What is companion object in kotlin?")
        questionList.add("Tell me about constructor in kotlin?")
        questionList.add("Protected vs internal?")
        questionList.add("What is meant by livedata?")
        questionList.add("What are coroutines?")
        questionList.add("What is lightweight?")
        questionList.add("scopes?")
        questionList.add("What are the scopes we have?")
        questionList.add("What is Scope functions?")
        questionList.add("Suspend function?")
        questionList.add("What is inline function?")
        questionList.add("What is actual function?")
        questionList.add(" What are Coroutine Builders?")
        questionList.add("launch?")
        questionList.add("What is async?")
        questionList.add("await?")
        questionList.add("What is runBlocking { }?")
        questionList.add("What is withContext?")
        questionList.add("Launch vs Async ?")
        questionList.add("What are Dispatchers?")
        questionList.add("What is extension function?")
    }

    private fun initializeWorkManager() {

    }

    private fun callTtsApi(currentQuestion: String) {
        // Call the API
        val XRapidApiKey = "56f16c6520msh7fcb1e0ad4e4115p13ee2bjsnb90d1ba60fc9"
        val XRapidApiHost = "large-text-to-speech.p.rapidapi.com"

        val encryptedApiKey = EncryptionUtils.encrypt(XRapidApiKey)

        val decryptedApiKey = EncryptionUtils.decrypt(encryptedApiKey)

        var hashMap = HashMap<String, String>()
        hashMap.put("text", currentQuestion)
        var gson = Gson()

        var requestBody =
            gson.toJson(hashMap).toString().toRequestBody("application/json".toMediaTypeOrNull())

        progress.visibility = VISIBLE
        progress.progressTintList = ColorStateList.valueOf(android.graphics.Color.YELLOW)
        viewModel.postDataToApi(
            XRapidApiKey,
            XRapidApiHost,
            requestBody
        )

        // Observe the response
        viewModel.ttsRapidAPIResultLiveData.observe(this) {
            // Update UI with the response
            Toast.makeText(
                this@InterviewScreen,
                "response: ${it.id.toString()}",
                Toast.LENGTH_SHORT
            )
                .show()
            it.id?.let { id ->
                progress.visibility = GONE
                if (previousID != id) {
                    previousID = id
                    callTtsWithIDApi(id)
                }
            }
        }
    }

    private fun callTtsWithIDApi(id: String) {

        var hashMap = HashMap<String, String>()
        hashMap.put("id", id)

        progress.visibility = VISIBLE
        progress.progressTintList = ColorStateList.valueOf(android.graphics.Color.BLUE)
        viewModel.getTtsWithId(hashMap)

        // Observe the response
        viewModel.ttsWithIDResultLiveData.observe(this) {
            // Update UI with the response
//            Toast.makeText(this@InterviewScreen, "response: ${it.toString()}", Toast.LENGTH_SHORT)
//                .show()
            it.url?.let { it1 ->
                progress.visibility = GONE
                playAudio(it1)
            }
        }
    }


    private fun playAudio(audioUrl: String) {

        progress.visibility = VISIBLE
        progress.progressTintList = ColorStateList.valueOf(android.graphics.Color.GREEN)

        // on below line we are creating a variable for our audio url
//        var audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"

        // on below line we are setting audio stream
        // type as stream music on below line.
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        // Set the audio attributes for the audio manager
        mediaPlayer.setAudioAttributes(audioAttributes)

//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

        // on below line we are running a try
        // and catch block for our media player.
        try {
            // on below line we are setting audio
            // source as audio url on below line.
            mediaPlayer.setDataSource(audioUrl)
            mediaPlayer.setOnCompletionListener { mp ->
                // This method will be called when the media playback is completed
                // Handle any logic or actions you want to perform after playback completion
                pauseAudio()
            }
            // on below line we are
            // preparing our media player.
            mediaPlayer.prepare()

            // on below line we are
            // starting our media player.
            mediaPlayer.start()
            progress.visibility = GONE

        } catch (e: Exception) {
            progress.visibility = GONE

            // on below line we are handling our exception.
            Toast.makeText(applicationContext, "Audio Exception.: $e", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
        // on below line we are displaying a toast message as audio player.
//        Toast.makeText(applicationContext, "Audio started playing..", Toast.LENGTH_SHORT).show()

    }

    private fun pauseAudio() {
        // on below line we are checking
        // if media player is playing.
        if (mediaPlayer.isPlaying) {
            // if media player is playing we
            // are stopping it on below line.
            mediaPlayer.stop()

            // on below line we are resetting
            // our media player.
            mediaPlayer.reset()

            // on below line we are calling
            // release to release our media player.
            mediaPlayer.release()

            // on below line we are displaying a toast message to pause audio/
//            Toast.makeText(applicationContext, "Audio has been  paused..", Toast.LENGTH_SHORT)
//                .show()

        } else {
            // if audio player is not displaying we are displaying below toast message
            Toast.makeText(applicationContext, "Audio not played..", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializeSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Log.d(
                    "Speech",
                    "onReadyForSpeech: "
                )    // Called when the speech recognition is ready to receive input
            }

            override fun onBeginningOfSpeech() {
                Log.d(
                    "Speech",
                    "onBeginningOfSpeech: "
                )      // Called when the user starts speaking
            }

            override fun onRmsChanged(rmsdB: Float) {
                Log.d(
                    "Speech",
                    "onRmsChanged: "
                )     // Called when the RMS value of the input audio changes
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                Log.d("Speech", "onBufferReceived: ")
                // Called when partial recognition results are available
            }

            override fun onEndOfSpeech() {
                Log.d("Speech", "onEndOfSpeech: ")
                // Called when the user stops speaking
                startSpeechRecognition()
            }

            override fun onError(error: Int) {
                // Called when an error occurs during speech recognition
                Toast.makeText(this@InterviewScreen, "Error: $error", Toast.LENGTH_SHORT).show()
            }

            override fun onResults(results: Bundle?) {
                // Called when the final recognition results are available
                startSpeechRecognition()
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val recognizedText = matches[0]
                    outputTV.text = recognizedText
                    // Process the recognized text
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                Log.d("Speech", "onPartialResults: ")

                // Called when partial recognition results are available
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                Log.d("Speech", "onEvent: ")
                // Called when an event related to speech recognition occurs
            }
        })
    }

}


