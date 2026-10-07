package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.SinBorder
import com.example.ui.theme.SinGrey
import com.example.ui.theme.SinSurface
import com.example.ui.theme.SinSurfaceVariant
import com.example.ui.theme.SinWhite
import com.example.viewmodel.AppScreen
import com.example.viewmodel.GameViewModel

@Composable
fun RulesScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
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
                    modifier = Modifier.testTag("rules_back_button")
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
                        text = "THE RULES OF HELL",
                        color = SinWhite,
                        fontSize = 20.sp,
                        fontFamily = BebasNeueFontFamily,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "How to use your physical cards with the Devil's Deck app",
                        color = SinGrey,
                        fontSize = 11.sp,
                        fontFamily = MontserratFontFamily
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    RuleSectionCard(
                        number = "01",
                        title = "SETUP: DEAL PHYSICAL CARDS",
                        text = "Every player is dealt 7 to 10 physical white answer cards from your Assholes Live Forever box. The app acts as the digital dealer and prompt deck.",
                        emoji = "🃏"
                    )
                }

                item {
                    RuleSectionCard(
                        number = "02",
                        title = "THE DEVIL'S CROWN (THE JUDGE)",
                        text = "Each round, the app appoints one player as 'The Devil' (Judge). The Devil holds the phone and reads the black prompt card aloud to the room.",
                        emoji = "👑"
                    )
                }

                item {
                    RuleSectionCard(
                        number = "03",
                        title = "SUBMITTING ANSWERS",
                        text = "All other players secretly choose their best (or most offensive/hilarious) physical answer card and place it face-down in the center pile. If a prompt requires 'PICK 2', place two cards in order.",
                        emoji = "🤫"
                    )
                }

                item {
                    RuleSectionCard(
                        number = "04",
                        title = "DELIBERATION & JUDGMENT",
                        text = "The Devil shuffles all submitted physical cards and reads each one out loud with dramatic conviction. The Devil chooses their favorite answer. The player who submitted it wins a Sin Point (🔥).",
                        emoji = "⚖️"
                    )
                }

                item {
                    RuleSectionCard(
                        number = "05",
                        title = "DEVIL'S PUNISHMENTS & DARES",
                        text = "The round loser (worst answer or slowest submission) can be forced to draw a 'Devil's Punishment' in the app (e.g. phone roulette, savage roast, or drink penalties).",
                        emoji = "💀"
                    )
                }

                item {
                    RuleSectionCard(
                        number = "06",
                        title = "ROTATION & VICTORY",
                        text = "All players draw back up to their hand size. The role of Devil rotates clockwise. The first player to reach the Target Sins (default 5 or 7) wins the throne of Hell!",
                        emoji = "🔥"
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = DevilBlood.copy(alpha = 0.5f)),
                        border = BorderStroke(1.2.dp, DevilCrimson)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "HOUSE RULE: THE ASSHOLE'S TITHE 🥃",
                                color = DevilGold,
                                fontSize = 14.sp,
                                fontFamily = BebasNeueFontFamily,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "If you played your card last, or if the Devil groans in disgust at your submission, take 2 drinks. If everyone groans at the Devil's choice, the Devil drinks double!",
                                color = SinWhite,
                                fontSize = 12.sp,
                                fontFamily = MontserratFontFamily,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun RuleSectionCard(
    number: String,
    title: String,
    text: String,
    emoji: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SinSurface),
        border = BorderStroke(1.dp, SinBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                color = DevilBlood,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, DevilCrimson),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = number,
                        color = SinWhite,
                        fontFamily = BebasNeueFontFamily,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = emoji, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = title,
                        color = DevilGold,
                        fontSize = 14.sp,
                        fontFamily = BebasNeueFontFamily,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = text,
                    color = SinWhite,
                    fontSize = 13.sp,
                    fontFamily = MontserratFontFamily,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
