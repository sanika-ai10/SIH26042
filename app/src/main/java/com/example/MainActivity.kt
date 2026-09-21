package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.GroupWork
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppHeader
import com.example.ui.screens.AssessmentScreen
import com.example.ui.screens.ClassroomTranslateScreen
import com.example.ui.screens.FlashcardsScreen
import com.example.ui.screens.IntegrationHubScreen
import com.example.ui.screens.WorksheetsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SaffronContainer
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.AppNavTab
import com.example.viewmodel.MainAppViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainAppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                VernacularPedagogyApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun VernacularPedagogyApp(viewModel: MainAppViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppHeader()
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_navigation_bar"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                AppNavTab.values().forEach { tab ->
                    val isSelected = uiState.currentTab == tab
                    val tabIcon = when (tab) {
                        AppNavTab.TRANSLATE -> Icons.AutoMirrored.Filled.Chat
                        AppNavTab.FLASHCARDS -> Icons.Default.Style
                        AppNavTab.WORKSHEETS -> Icons.Default.Assignment
                        AppNavTab.ASSESSMENT -> Icons.Default.Assessment
                        AppNavTab.TEAM_HUB -> Icons.Default.GroupWork
                    }

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.setTab(tab) },
                        icon = {
                            Icon(
                                imageVector = tabIcon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SaffronPrimary,
                            selectedTextColor = SaffronPrimary,
                            indicatorColor = SaffronContainer
                        ),
                        modifier = Modifier.testTag("nav_tab_${tab.name}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentTab) {
                AppNavTab.TRANSLATE -> ClassroomTranslateScreen(viewModel = viewModel)
                AppNavTab.FLASHCARDS -> FlashcardsScreen(viewModel = viewModel)
                AppNavTab.WORKSHEETS -> WorksheetsScreen(viewModel = viewModel)
                AppNavTab.ASSESSMENT -> AssessmentScreen(viewModel = viewModel)
                AppNavTab.TEAM_HUB -> IntegrationHubScreen(viewModel = viewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    MyApplicationTheme {
        VernacularPedagogyApp(viewModel = MainAppViewModel())
    }
}
