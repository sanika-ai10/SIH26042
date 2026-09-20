package com.example.core.contracts

import android.content.Context
import com.example.model.ClassroomPhrase
import com.example.model.FlashcardItem
import com.example.model.StudentRecord
import com.example.model.TranslationResult
import com.example.model.Worksheet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * =======================================================================
 * PART 1 CONTRACT: Hindi Speech → Text (ASR)
 * Responsible team member implements IndicConformer / Android Speech ASR
 * =======================================================================
 */
interface HindiAsrService {
    val isListening: StateFlow<Boolean>
    val recognizedText: StateFlow<String>
    val audioVolumeLevel: StateFlow<Float>

    fun startListening(context: Context, onResult: (String) -> Unit, onError: (String) -> Unit)
    fun stopListening()
}

/**
 * =======================================================================
 * PART 2 CONTRACT: Hindi → Santhali Translation
 * Responsible team member implements IndicTrans2 / Local Quantized Model
 * =======================================================================
 */
interface HindiSanthaliTranslationService {
    suspend fun translateHindiToSanthali(hindiText: String): TranslationResult
    fun getCuratedClassroomPhrases(): List<ClassroomPhrase>
    fun searchPhrases(query: String): List<ClassroomPhrase>
}

/**
 * =======================================================================
 * PART 3 CONTRACT: Santhali → Speech (TTS)
 * Responsible team member implements Indic Parler-TTS / Offline Audio
 * =======================================================================
 */
interface SanthaliTtsService {
    val isSpeaking: StateFlow<Boolean>
    fun speakSanthali(context: Context, olChikiText: String, speechRate: Float = 0.9f)
    fun stopPlayback()
}

/**
 * =======================================================================
 * PART 5 CONTRACT: Worksheet + Flashcard Generator
 * Responsible team member implements NIPUN Bharat FLN curriculum generator
 * =======================================================================
 */
interface CurriculumService {
    fun getFlashcards(): List<FlashcardItem>
    fun generateWorksheet(topic: String, grade: String): Worksheet
}

/**
 * =======================================================================
 * PART 6 CONTRACT: Offline / AI Optimization + Assessment
 * Responsible team member implements ONNX Runtime, Quantization & FLN Scoring
 * =======================================================================
 */
interface OfflineAssessmentService {
    fun getEdgeOptimizationMetrics(): Map<String, String>
    fun getStudentAssessments(): List<StudentRecord>
    fun updateStudentComprehension(studentId: String, newScore: Int)
}
