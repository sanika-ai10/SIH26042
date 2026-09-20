package com.example.model

enum class PhraseCategory(val displayName: String, val icon: String) {
    ALL("सभी (All)", "📚"),
    COMMANDS("कक्षा निर्देश (Instructions)", "📋"),
    GREETINGS("अभिवादन (Greetings)", "🤝"),
    NUMERACY("गिनती (FLN Numeracy)", "🔢"),
    PRAISE("प्रशंसा (Encouragement)", "🌟"),
    QUESTIONS("पूछताछ (Inquiry)", "❓")
}

data class ClassroomPhrase(
    val id: String,
    val category: PhraseCategory,
    val hindi: String,
    val santhaliOlChiki: String,
    val pronunciationLatin: String,
    val pronunciationDevanagari: String,
    val englishMeaning: String,
    val audioDurationMs: Long = 1800L
)

data class TranslationResult(
    val originalHindi: String,
    val santhaliOlChiki: String,
    val pronunciationLatin: String,
    val pronunciationDevanagari: String,
    val englishMeaning: String,
    val latencyMs: Long = 280L,
    val confidence: Float = 0.96f,
    val isFromLocalCache: Boolean = true
)
