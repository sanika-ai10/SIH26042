package com.example.core.assessment

import kotlinx.coroutines.flow.Flow

class AssessmentEngine(private val dao: AssessmentDao) {

    fun calculateScore(correctAnswers: Int, totalQuestions: Int): Int {
        if (totalQuestions <= 0) return 0
        val score = (correctAnswers * 100) / totalQuestions
        return score.coerceIn(0, 100)
    }

    fun getLevel(score: Int): String {
        return when {
            score >= 80 -> "ADVANCED"
            score >= 60 -> "DEVELOPING"
            else -> "NOT_ASSESSED"
        }
    }

    // Call this after a student finishes a worksheet/flashcard set (point 5's output).
    // Scores and saves the attempt offline in one step.
    suspend fun recordAttempt(
        studentId: String,
        topic: String,
        correctAnswers: Int,
        totalQuestions: Int
    ): AssessmentResult {
        val score = calculateScore(correctAnswers, totalQuestions)
        val level = getLevel(score)
        val result = AssessmentResult(
            studentId = studentId,
            topic = topic,
            correctAnswers = correctAnswers,
            totalQuestions = totalQuestions,
            score = score,
            level = level
        )
        val id = dao.insert(result)
        return result.copy(id = id)
    }

    fun getHistory(studentId: String): Flow<List<AssessmentResult>> {
        return dao.getResultsForStudent(studentId)
    }

    suspend fun getAverage(studentId: String): Double {
        return dao.getAverageScore(studentId) ?: 0.0
    }
}