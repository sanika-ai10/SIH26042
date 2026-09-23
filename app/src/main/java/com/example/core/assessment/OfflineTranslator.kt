package com.example.core.assessment

import android.content.Context

class OfflineTranslator(context: Context) {

    private val appContext = context.applicationContext

    fun translateHindiToSanthali(text: String): String {
        return "TRANSLATION_PENDING: $text"
    }

    fun isModelAvailable(): Boolean {
        return try {
            appContext.assets.open("hindi_santhali_model.tflite").close()
            true
        } catch (e: Exception) {
            false
        }
    }
}
