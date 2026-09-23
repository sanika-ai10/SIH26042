package com.example.core.assessment

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assessment_results")
data class AssessmentResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: String,
    val topic: String,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val score: Int,
    val level: String,
    val timestamp: Long = System.currentTimeMillis()
)
