package com.example.ui.screens

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MasteryLevel
import com.example.model.StudentRecord
import com.example.ui.theme.JadeContainer
import com.example.ui.theme.JadeDark
import com.example.ui.theme.JadeSecondary
import com.example.ui.theme.SaffronContainer
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.MainAppViewModel

@Composable
fun AssessmentScreen(
    viewModel: MainAppViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var activeEvaluationStudent by remember { mutableStateOf<StudentRecord?>(null) }
    var oralTestPrompt by remember { mutableStateOf("किताब खोलो (ᱯᱩᱛᱷᱤ ᱡᱷᱤᱡᱽ ᱢᱮ)") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // NIPUN Bharat FLN Header
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = JadeContainer,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("🎯", fontSize = 14.sp)
                                }
                            }
                            Text(
                                text = "NIPUN FLN मौखिक मूल्यांकन (Oral Assessment)",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = JadeDark
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFDCFCE7)
                        ) {
                            Text(
                                text = "सत्र २०२६",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF166534),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = "मातृभाषा आधारित प्राथमिक शिक्षा (MTB-MLE): हिंदी-माध्यम शिक्षक द्वारा संथाली भाषी छात्रों की समझ का त्वरित मूल्यांकन।",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Edge Benchmark Summary Card (From SIH Pitch Deck Slide 4 & 5)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            tint = SaffronPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Part 6: ऑफ़लाइन एज मेट्रिक्स (Edge Benchmarks)",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF0F172A)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricMiniCard("विलंबता (Latency)", "280 ms", "< 3s Target", Color(0xFF0284C7))
                        MetricMiniCard("मेमोरी (RAM)", "174 MB", "< 2GB Budget", Color(0xFF059669))
                        MetricMiniCard("ऑफ़लाइन मॉडल", "INT8 ONNX", "Zero Bandwidth", SaffronPrimary)
                    }
                }
            }
        }

        // Student Roster Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "छात्र प्रगति सूची (Student Roster)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${uiState.studentList.size} छात्र",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        items(uiState.studentList) { student ->
            StudentEvaluationCard(
                student = student,
                onEvaluate = { activeEvaluationStudent = student },
                onScoreUp = {
                    val newScore = (student.vocabularyScore + 5).coerceAtMost(100)
                    viewModel.updateStudentScore(student.id, newScore)
                }
            )
        }
    }

    // Evaluation Dialog
    if (activeEvaluationStudent != null) {
        val st = activeEvaluationStudent!!
        AlertDialog(
            onDismissRequest = { activeEvaluationStudent = null },
            title = { Text("मौखिक परीक्षण: ${st.studentName}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("रोल नं: ${st.rollNo} • मातृभाषा: ${st.motherTongue}")
                    Text(
                        "परीक्षण निर्देश: शिक्षक हिंदी में बोलेंगे या संथाली ऑडियो सुनाएंगे। यदि छात्र समझकर प्रतिक्रिया देता है, तो अंक बढ़ाएं।",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    OutlinedCard(shape = RoundedCornerShape(8.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("ऑडियो: ᱯᱩᱛᱷᱤ ᱡᱷᱤᱡᱽ ᱢᱮ (किताब खोलो)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            IconButton(
                                onClick = { viewModel.speakSanthali(context, "ᱯᱩᱛᱷᱤ ᱡᱷᱤᱡᱽ ᱢᱮ") },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Play", tint = JadeDark)
                            }
                        }
                    }
                    Text("वर्तमान प्राप्तांक: ${st.vocabularyScore} / 100", fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newScore = (st.vocabularyScore + 5).coerceAtMost(100)
                        viewModel.updateStudentScore(st.id, newScore)
                        activeEvaluationStudent = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = JadeSecondary)
                ) {
                    Text("+५ अंक दें (Pass)")
                }
            },
            dismissButton = {
                TextButton(onClick = { activeEvaluationStudent = null }) {
                    Text("बंद करें")
                }
            }
        )
    }
}

@Composable
fun MetricMiniCard(
    title: String,
    value: String,
    subtext: String,
    accentColor: Color
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.width(106.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = Color(0xFF64748B))
            Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = accentColor)
            Text(subtext, style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = Color(0xFF94A3B8))
        }
    }
}

@Composable
fun StudentEvaluationCard(
    student: StudentRecord,
    onEvaluate: () -> Unit,
    onScoreUp: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEvaluate() }
            .testTag("student_card_${student.rollNo}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = student.rollNo,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    Text(
                        text = student.studentName,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "टिप्पणी: ${student.remarks}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(student.oralComprehension.colorHex).copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "${student.oralComprehension.labelHindi} (${student.oralComprehension.labelEnglish})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(student.oralComprehension.colorHex)
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = "प्रतिक्रिया समय: ${student.responseLatencySeconds}s",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${student.vocabularyScore}%",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = JadeDark
                    )
                )
                FilledTonalButton(
                    onClick = onScoreUp,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text("+५", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
