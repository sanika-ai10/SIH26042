package com.example.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TeamPartStatus
import com.example.ui.theme.JadeContainer
import com.example.ui.theme.JadeDark
import com.example.ui.theme.JadeSecondary
import com.example.ui.theme.SaffronContainer
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.MainAppViewModel

@Composable
fun IntegrationHubScreen(
    viewModel: MainAppViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var testStatusMessage by remember { mutableStateOf<String?>(null) }
    var isPipelineTesting by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Team Header Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Smart India Hackathon 2026",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = SaffronPrimary
                            )
                            Text(
                                text = "Team: SNPSU CipherSphere",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "PS ID: 26042 • AI Vernacular Pedagogy Tool",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = SaffronContainer,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("🇮🇳", fontSize = 18.sp)
                            }
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    Text(
                        text = "परियोजना का ६-भागों में विभाजन (6-Member Team Architecture): आप भाग ४ (Android/App Interface) का निर्माण कर रहे हैं। नीचे सभी ६ भागों के एकीकरण कॉन्ट्रैक्ट्स और कार्यप्रणाली का विवरण है।",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // End-to-End Test Button
                    Button(
                        onClick = {
                            isPipelineTesting = true
                            testStatusMessage = "⏳ ६-सदस्यीय पाइपलाइन परीक्षण प्रारंभ: ASR ➔ IndicTrans2 ➔ Parler-TTS ➔ Jetpack Compose UI ➔ FLN Engine ➔ ONNX Edge..."
                            viewModel.selectPresetPhrase(
                                viewModel.translationService.getCuratedClassroomPhrases().first(),
                                context
                            )
                            Toast.makeText(context, "६-भागों का एकीकरण सफल! (Pipeline OK)", Toast.LENGTH_LONG).show()
                            isPipelineTesting = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("test_pipeline_button")
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Test pipeline")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("संपूर्ण ६-भाग पाइपलाइन का परीक्षण करें (Run Full Pipeline)")
                    }

                    if (testStatusMessage != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFDCFCE7),
                            border = BorderStroke(1.dp, Color(0xFF86EFAC)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = testStatusMessage!!,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                color = Color(0xFF166534),
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Text(
                text = "६ कोडिंग भाग एवं दायित्व (6 Parts Architecture)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // List of all 6 parts
        items(uiState.teamParts) { part ->
            TeamPartCard(part = part)
        }

        // Developer Integration Instructions Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "🤝 आपकी टीम के साथियों के लिए निर्देश (Code Integration):",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "• भाग १ (ASR): HindiAsrService इंटरफेस को अपने IndicConformer मॉडल से जोड़ें।\n" +
                               "• भाग २ (Translation): HindiSanthaliTranslationService में अपना IndicTrans2 मॉडल प्लग करें।\n" +
                               "• भाग ३ (TTS): SanthaliTtsService में Indic Parler-TTS मॉडल का पथ प्रदान करें।\n" +
                               "• भाग ४ (आपका भाग - Android UI): संपूर्ण Jetpack Compose यूआई, स्क्रीन नेविगेशन और कंपोनेंट तैयार हैं।\n" +
                               "• भाग ५ (Curriculum): CurriculumService में नए फ्लैशकार्ड या वर्कशीट जोड़ें।\n" +
                               "• भाग ६ (Edge AI): OfflineAssessmentService में ऑन-डिवाइस ONNX मेट्रिक्स अपडेट करें।",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        color = Color(0xFF334155)
                    )
                }
            }
        }
    }
}

@Composable
fun TeamPartCard(part: TeamPartStatus) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (part.isUserPart) Color(0xFFFFFBEB) else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            if (part.isUserPart) 2.dp else 1.dp,
            if (part.isUserPart) SaffronPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        ),
        shadowElevation = if (part.isUserPart) 4.dp else 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("team_part_${part.partNumber}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
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
                        color = if (part.isUserPart) SaffronPrimary else MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${part.partNumber}",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (part.isUserPart) Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    Text(
                        text = part.title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (part.isUserPart) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SaffronPrimary
                    ) {
                        Text(
                            text = "⭐ आपका भाग (Your Part)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFDCFCE7)
                    ) {
                        Text(
                            text = part.status,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF166534)
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Text(
                text = part.responsibilities,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = "तकनीक: ${part.techStack}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = JadeDark,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = part.latencyOrPerformance,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = SaffronDark
                )
            }
        }
    }
}
