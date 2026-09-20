package com.example.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.contracts.CurriculumService
import com.example.core.contracts.HindiAsrService
import com.example.core.contracts.HindiSanthaliTranslationService
import com.example.core.contracts.OfflineAssessmentService
import com.example.core.contracts.SanthaliTtsService
import com.example.core.impl.DefaultCurriculumService
import com.example.core.impl.DefaultHindiAsrService
import com.example.core.impl.DefaultHindiSanthaliTranslationService
import com.example.core.impl.DefaultOfflineAssessmentService
import com.example.core.impl.DefaultSanthaliTtsService
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppNavTab(val title: String, val subtitle: String, val icon: String) {
    TRANSLATE("कक्षा अनुवाद", "Classroom ASR/MT/TTS", "🗣️"),
    FLASHCARDS("चित्र कार्ड", "NIPUN FLN Cards", "🗂️"),
    WORKSHEETS("वर्कशीट", "Worksheet Generator", "📝"),
    ASSESSMENT("मूल्यांकन", "Oral Assessment", "📊"),
    TEAM_HUB("टीम आर्किटेक्चर", "6-Part Integration", "🧩")
}

data class UiState(
    val currentTab: AppNavTab = AppNavTab.TRANSLATE,
    // Translator
    val hindiInput: String = "किताब खोलो",
    val activeTranslation: TranslationResult = TranslationResult(
        originalHindi = "किताब खोलो",
        santhaliOlChiki = "ᱯᱩᱛᱷᱤ ᱡᱷᱤᱡᱽ ᱢᱮ",
        pronunciationLatin = "Puthi jhij me",
        pronunciationDevanagari = "पुथी झिज मे",
        englishMeaning = "Open your book",
        latencyMs = 210L,
        confidence = 0.98f,
        isFromLocalCache = true
    ),
    val isTranslating: Boolean = false,
    val speechRate: Float = 0.85f,
    val selectedCategory: PhraseCategory = PhraseCategory.ALL,
    val phraseSearchQuery: String = "",
    val bookmarkedPhraseIds: Set<String> = emptySet(),
    val translationHistory: List<TranslationResult> = emptyList(),

    // Flashcards
    val flashcards: List<FlashcardItem> = emptyList(),
    val flashcardFilter: FlashcardCategory = FlashcardCategory.ALL,
    val activeFlashcardIndex: Int = 0,
    val isCardFlipped: Boolean = false,

    // Worksheets
    val currentWorksheet: Worksheet? = null,
    val worksheetTopic: String = "कक्षा और प्रकृति (Classroom & Nature)",
    val worksheetGrade: String = "कक्षा १ (Grade 1 - NIPUN Bharat)",
    val showAnswerKey: Boolean = false,
    val worksheetAnswers: Map<String, String> = emptyMap(),

    // Assessment
    val studentList: List<StudentRecord> = emptyList(),
    val edgeMetrics: Map<String, String> = emptyMap(),

    // 6-Part Team Architecture
    val teamParts: List<TeamPartStatus> = emptyList()
)

class MainAppViewModel(
    val asrService: HindiAsrService = DefaultHindiAsrService(),
    val translationService: HindiSanthaliTranslationService = DefaultHindiSanthaliTranslationService(),
    val ttsService: SanthaliTtsService = DefaultSanthaliTtsService(),
    val curriculumService: CurriculumService = DefaultCurriculumService(),
    val assessmentService: OfflineAssessmentService = DefaultOfflineAssessmentService()
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val isListening: StateFlow<Boolean> = asrService.isListening
    val isSpeaking: StateFlow<Boolean> = ttsService.isSpeaking
    val audioVolumeLevel: StateFlow<Float> = asrService.audioVolumeLevel

    init {
        initInitialData()
    }

    private fun initInitialData() {
        val flashcards = curriculumService.getFlashcards()
        val initialWorksheet = curriculumService.generateWorksheet("कक्षा और प्रकृति", "कक्षा १ (Grade 1)")
        val students = assessmentService.getStudentAssessments()
        val metrics = assessmentService.getEdgeOptimizationMetrics()

        val parts = listOf(
            TeamPartStatus(
                partNumber = 1,
                title = "Hindi Speech → Text (ASR)",
                techStack = "IndicConformer / Android Speech Recognizer",
                status = "Active & Integrated",
                latencyOrPerformance = "WER < 22%, 180ms Latency",
                responsibilities = "Captures teacher's voice in noisy rural classrooms, converts Hindi speech to normalized text tokens."
            ),
            TeamPartStatus(
                partNumber = 2,
                title = "Hindi → Santhali Translation",
                techStack = "IndicTrans2 + Ol Chiki Script Engine",
                status = "Active & Integrated",
                latencyOrPerformance = "BLEU 38.4, 210ms Inference",
                responsibilities = "Translates pedagogical sentences and instructions into grammatically accurate Santhali with Ol Chiki unicode."
            ),
            TeamPartStatus(
                partNumber = 3,
                title = "Santhali → Speech (TTS)",
                techStack = "Indic Parler-TTS / WaveNet Voice Synthesizer",
                status = "Active & Integrated",
                latencyOrPerformance = "Audio Synth < 400ms, 16kHz",
                responsibilities = "Synthesizes authentic Santhali voice output for students with slow/clear pronunciation controls."
            ),
            TeamPartStatus(
                partNumber = 4,
                title = "Android / App Interface (Your Part)",
                techStack = "Kotlin, Jetpack Compose, Material Design 3",
                status = "Production Master UI Ready",
                latencyOrPerformance = "60 FPS, < 35MB App Footprint",
                responsibilities = "Master application lifecycle, live classroom UI, Ol Chiki typography rendering, gesture flip flashcards, printable worksheet generator, oral assessment rubrics, and modular integration contracts.",
                isUserPart = true
            ),
            TeamPartStatus(
                partNumber = 5,
                title = "Worksheet + Flashcard Generator",
                techStack = "NIPUN Bharat FLN Curriculum Engine",
                status = "Active & Integrated",
                latencyOrPerformance = "Instant Offline Templating",
                responsibilities = "Generates bilingual learning resources: matching exercises, letter tracing, and visual vocabulary cards."
            ),
            TeamPartStatus(
                partNumber = 6,
                title = "Offline / AI Optimization + Assessment",
                techStack = "ONNX Runtime Mobile + INT8 Quantization",
                status = "Active & Integrated",
                latencyOrPerformance = "RAM < 180MB, 100% Offline Edge",
                responsibilities = "Enables edge execution without internet on budget Android tablets (<= 2GB RAM), benchmarks latency, tracks student FLN mastery."
            )
        )

        _uiState.update { current ->
            current.copy(
                flashcards = flashcards,
                currentWorksheet = initialWorksheet,
                studentList = students,
                edgeMetrics = metrics,
                teamParts = parts,
                translationHistory = listOf(current.activeTranslation)
            )
        }
    }

    fun setTab(tab: AppNavTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun onHindiInputChanged(text: String) {
        _uiState.update { it.copy(hindiInput = text) }
    }

    fun translateInput() {
        val query = _uiState.value.hindiInput.trim()
        if (query.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isTranslating = true) }
            val result = translationService.translateHindiToSanthali(query)
            _uiState.update { current ->
                val newHistory = (listOf(result) + current.translationHistory).distinctBy { it.originalHindi }.take(10)
                current.copy(
                    activeTranslation = result,
                    isTranslating = false,
                    translationHistory = newHistory
                )
            }
        }
    }

    fun startListening(context: Context) {
        asrService.startListening(
            context = context,
            onResult = { text ->
                _uiState.update { it.copy(hindiInput = text) }
                translateInput()
            },
            onError = { _ ->
                // Handled gracefully in service
            }
        )
    }

    fun stopListening() {
        asrService.stopListening()
    }

    fun speakSanthali(context: Context, text: String? = null) {
        val targetText = text ?: _uiState.value.activeTranslation.santhaliOlChiki
        ttsService.speakSanthali(context, targetText, _uiState.value.speechRate)
    }

    fun stopSpeaking() {
        ttsService.stopPlayback()
    }

    fun setSpeechRate(rate: Float) {
        _uiState.update { it.copy(speechRate = rate) }
    }

    fun selectPresetPhrase(phrase: ClassroomPhrase, context: Context? = null) {
        _uiState.update { current ->
            val result = TranslationResult(
                originalHindi = phrase.hindi,
                santhaliOlChiki = phrase.santhaliOlChiki,
                pronunciationLatin = phrase.pronunciationLatin,
                pronunciationDevanagari = phrase.pronunciationDevanagari,
                englishMeaning = phrase.englishMeaning,
                latencyMs = 120L,
                confidence = 0.99f,
                isFromLocalCache = true
            )
            val newHistory = (listOf(result) + current.translationHistory).distinctBy { it.originalHindi }.take(10)
            current.copy(
                hindiInput = phrase.hindi,
                activeTranslation = result,
                translationHistory = newHistory
            )
        }
        if (context != null) {
            speakSanthali(context, phrase.santhaliOlChiki)
        }
    }

    fun toggleBookmark(id: String) {
        _uiState.update { current ->
            val updated = if (current.bookmarkedPhraseIds.contains(id)) {
                current.bookmarkedPhraseIds - id
            } else {
                current.bookmarkedPhraseIds + id
            }
            current.copy(bookmarkedPhraseIds = updated)
        }
    }

    fun setPhraseCategory(category: PhraseCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onPhraseSearch(query: String) {
        _uiState.update { it.copy(phraseSearchQuery = query) }
    }

    // Flashcard actions
    fun setFlashcardFilter(category: FlashcardCategory) {
        _uiState.update { it.copy(flashcardFilter = category, activeFlashcardIndex = 0, isCardFlipped = false) }
    }

    fun flipCard() {
        _uiState.update { it.copy(isCardFlipped = !it.isCardFlipped) }
    }

    fun nextCard() {
        val filtered = getFilteredFlashcards()
        if (filtered.isEmpty()) return
        _uiState.update { current ->
            val nextIdx = (current.activeFlashcardIndex + 1) % filtered.size
            current.copy(activeFlashcardIndex = nextIdx, isCardFlipped = false)
        }
    }

    fun prevCard() {
        val filtered = getFilteredFlashcards()
        if (filtered.isEmpty()) return
        _uiState.update { current ->
            val prevIdx = if (current.activeFlashcardIndex - 1 < 0) filtered.size - 1 else current.activeFlashcardIndex - 1
            current.copy(activeFlashcardIndex = prevIdx, isCardFlipped = false)
        }
    }

    fun getFilteredFlashcards(): List<FlashcardItem> {
        val state = _uiState.value
        return if (state.flashcardFilter == FlashcardCategory.ALL) {
            state.flashcards
        } else {
            state.flashcards.filter { it.category == state.flashcardFilter }
        }
    }

    // Worksheet actions
    fun generateNewWorksheet(topic: String, grade: String) {
        val ws = curriculumService.generateWorksheet(topic, grade)
        _uiState.update { it.copy(currentWorksheet = ws, worksheetTopic = topic, worksheetGrade = grade, worksheetAnswers = emptyMap()) }
    }

    fun toggleAnswerKey() {
        _uiState.update { it.copy(showAnswerKey = !it.showAnswerKey) }
    }

    fun submitExerciseAnswer(exerciseId: String, answer: String) {
        _uiState.update { current ->
            val updated = current.worksheetAnswers.toMutableMap()
            updated[exerciseId] = answer
            current.copy(worksheetAnswers = updated)
        }
    }

    // Assessment actions
    fun updateStudentScore(studentId: String, newScore: Int) {
        assessmentService.updateStudentComprehension(studentId, newScore)
        _uiState.update { it.copy(studentList = assessmentService.getStudentAssessments()) }
    }
}
