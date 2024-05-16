package com.dreavee.interviewbot.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class UpdateUIWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Perform your background task here
        // Update the UI or send a notification

        // Return Result.success() if the work is completed successfully
        return Result.success()
    }
}
