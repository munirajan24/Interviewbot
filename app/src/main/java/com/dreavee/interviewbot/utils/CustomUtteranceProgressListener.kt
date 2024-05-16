package com.dreavee.interviewbot.utils

import android.speech.tts.UtteranceProgressListener
import android.util.Log

class CustomUtteranceProgressListener : UtteranceProgressListener() {
    override fun onStart(utteranceId: String) {
        Log.d("TAG1", "onStart: $utteranceId")
        // Called when TTS starts speaking an utterance
    }

    override fun onDone(utteranceId: String) {
        Log.d("TAG1", "onDone: $utteranceId")
        // Called when TTS finishes speaking an utterance
    }

    override fun onError(utteranceId: String) {
        Log.d("TAG1", "onError: $utteranceId")
        // Called when there is an error during TTS
    }
}
