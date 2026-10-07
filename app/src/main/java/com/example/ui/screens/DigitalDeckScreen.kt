package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GrindBackground
import com.example.ui.theme.BebasNeueFontFamily
import com.example.ui.theme.DevilBlood
import com.example.ui.theme.DevilCrimson
import com.example.ui.theme.DevilGold
import com.example.ui.theme.MontserratFontFamily
import com.example.ui.theme.SinBlack
import com.example.ui.theme.SinBorder
import com.example.ui.theme.SinGrey
import com.example.ui.theme.SinSurface
import com.example.ui.theme.SinSurfaceVariant
import com.example.ui.theme.SinWhite
import com.example.viewmodel.AppScreen
import com.example.viewmodel.GameViewModel

@Composable
fun DigitalDeckScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    GrindBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    modifier = Modifier.testTag("digital_deck_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = SinWhite
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "DIGITAL ANSWER HAND",
                        color = SinWhite,
                        fontSize = 20.sp,
                        fontFamily = BebasNeueFontFamily,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Backup white response cards (tap to play or discard)",
                        color = SinGrey,
                        fontSize = 11.sp,
                        fontFamily = MontserratFontFamily
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Hand Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "YOUR HAND (${uiState.userHand.size} CARDS)",
                    color = DevilGold,
                    fontSize = 14.sp,
                    fontFamily = BebasNeueFontFamily,
                    letterSpacing = 1.sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { viewModel.drawOneDigitalAnswer() },
                        border = BorderStroke(1.dp, SinBorder),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("draw_answer_button")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Draw", modifier = Modifier.size(16.dp), tint = SinWhite)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "DRAW 1", color = SinWhite, fontFamily = BebasNeueFontFamily, fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { viewModel.dealDigitalAnswers() },
                        border = BorderStroke(1.dp, SinBorder),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("redealt_hand_button")
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Redeal", modifier = Modifier.size(16.dp), tint = SinGrey)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "REDEAL", color = SinGrey, fontFamily = BebasNeueFontFamily, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // White Cards List (Cards Against Humanity style white card look)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.userHand, key = { it.id }) { card ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.discardAnswerCard(card.id) }
                            .testTag("white_card_${card.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SinWhite),
                        border = BorderStroke(1.5.dp, Color(0xFFDCDCE6))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = card.text,
                                    color = SinBlack,
                                    fontSize = 15.sp,
                                    fontFamily = MontserratFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 21.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "DEVIL'S DECK ANSWER",
                                    color = Color.Gray,
                                    fontSize = 9.sp,
                                    fontFamily = BebasNeueFontFamily,
                                    letterSpacing = 1.sp
                                )
                            }

                            IconButton(
                                onClick = { viewModel.discardAnswerCard(card.id) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Discard",
                                    tint = Color.DarkGray
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
