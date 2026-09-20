package com.example.model

enum class MasteryLevel(val labelHindi: String, val labelEnglish: String, val colorHex: Long) {
    NOT_ASSESSED("अभी नहीं", "Not Evaluated", 0xFF94A3B8),
    DEVELOPING("प्रयास जारी", "Developing", 0xFFF59E0B),
    PROFICIENT("निपुण", "Proficient", 0xFF10B981),
    ADVANCED("उत्कृष्ट", "Advanced", 0xFF059669)
}

data class StudentRecord(
    val id: String,
    val studentName: String,
    val rollNo: String,
    val motherTongue: String = "Santhali (ᱥᱟᱱᱛᱟᱲᱤ)",
    val oralComprehension: MasteryLevel = MasteryLevel.DEVELOPING,
    val vocabularyScore: Int = 85,
    val responseLatencySeconds: Float = 1.8f,
    val remarks: String = "चित्र देखकर संथाली में सही शब्द बोला।"
)

data class TeamPartStatus(
    val partNumber: Int,
    val title: String,
    val techStack: String,
    val status: String,
    val latencyOrPerformance: String,
    val responsibilities: String,
    val isUserPart: Boolean = false
)
