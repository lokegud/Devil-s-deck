package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.ActiveGameScreen
import com.example.ui.screens.CustomCardsScreen
import com.example.ui.screens.DigitalDeckScreen
import com.example.ui.screens.GameSetupScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.QuickDrawScreen
import com.example.ui.screens.RulesScreen
import com.example.ui.theme.DevilsDeckTheme
import com.example.ui.theme.SinBlack
import com.example.viewmodel.AppScreen
import com.example.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DevilsDeckTheme {
                val viewModel: GameViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = SinBlack,
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    Crossfade(
                        targetState = uiState.currentScreen,
                        label = "ScreenTransition",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) { screen ->
                        when (screen) {
                            AppScreen.HOME -> HomeScreen(viewModel = viewModel)
                            AppScreen.SETUP -> GameSetupScreen(viewModel = viewModel)
                            AppScreen.ACTIVE_GAME -> ActiveGameScreen(viewModel = viewModel)
                            AppScreen.QUICK_DRAW -> QuickDrawScreen(viewModel = viewModel)
                            AppScreen.DIGITAL_DECK -> DigitalDeckScreen(viewModel = viewModel)
                            AppScreen.CUSTOM_CARDS -> CustomCardsScreen(viewModel = viewModel)
                            AppScreen.RULES -> RulesScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
