package com.example.core.assessment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AssessmentDao {

    @Insert
    suspend fun insert(result: AssessmentResult): Long

    @Query("SELECT * FROM assessment_results WHERE studentId = :studentId ORDER BY timestamp DESC")
    fun getResultsForStudent(studentId: String): Flow<List<AssessmentResult>>

    @Query("SELECT * FROM assessment_results WHERE studentId = :studentId AND topic = :topic ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestResult(studentId: String, topic: String): AssessmentResult?

    @Query("SELECT AVG(score) FROM assessment_results WHERE studentId = :studentId")
    suspend fun getAverageScore(studentId: String): Double?

    @Query("SELECT * FROM assessment_results ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<AssessmentResult>>
}
