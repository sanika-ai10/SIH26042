package com.example.model

enum class ExerciseType(val title: String) {
    MATCHING("जोड़ी मिलाओ (Match the pairs)"),
    PICTURE_IDENTIFY("चित्र पहचान कर लिखो (Picture ID)"),
    OL_CHIKI_TRACING("अक्षर अनुरेखण (Ol Chiki Tracing)"),
    FILL_IN_BLANKS("खाली स्थान भरो (Fill in blanks)")
}

data class MatchingPair(
    val hindi: String,
    val olChiki: String,
    val pronunciation: String
)

data class WorksheetExercise(
    val id: String,
    val type: ExerciseType,
    val promptHindi: String,
    val promptSanthali: String,
    val matchingPairs: List<MatchingPair> = emptyList(),
    val tracingLetters: List<String> = emptyList(),
    val questionText: String = "",
    val options: List<String> = emptyList(),
    val correctAnswer: String = ""
)

data class Worksheet(
    val id: String,
    val title: String,
    val gradeLevel: String,
    val flnObjective: String,
    val dateGenerated: String,
    val exercises: List<WorksheetExercise>
)
