package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.DefaultDecks
import com.example.data.model.GameMode
import com.example.ui.components.GrindBackground
import com.example.ui.theme.BebasNeueFontFamily
import com.example.ui.theme.DevilBlood
import com.example.ui.theme.DevilCrimson
import com.example.ui.theme.DevilGold
import com.example.ui.theme.DevilRed
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
fun HomeScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    GrindBackground(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(28.dp))

                // Hero Banner Box with Noir Graphic Illustration
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(175.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(2.dp, DevilBlood, RoundedCornerShape(16.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_devils_banner),
                        contentDescription = "Devil's Deck Hero Art",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        SinBlack.copy(alpha = 0.85f),
                                        SinBlack
                                    )
                                )
                            )
                    )

                    // Banner content text
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(14.dp)
                    ) {
                        Surface(
                            color = DevilCrimson,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "ASSHOLES LIVE FOREVER COMPANION",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontFamily = BebasNeueFontFamily,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "DEVIL'S DECK",
                            color = SinWhite,
                            fontSize = 32.sp,
                            fontFamily = BebasNeueFontFamily,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "The dark party engine for your physical cards",
                            color = SinGrey,
                            fontSize = 12.sp,
                            fontFamily = MontserratFontFamily
                        )
                    }
                }
            }

            // Quick Stats Banner
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatPill("250+ PROMPTS", "😈 WICKED CARDS", Modifier.weight(1f))
                    StatPill("5 THEMES", "💀 SIN CITY DECKS", Modifier.weight(1f))
                    StatPill("35+ DARES", "🔥 LOSER PENALTIES", Modifier.weight(1f))
                }
            }

            // Primary Start Button
            item {
                Button(
                    onClick = {
                        viewModel.setGameMode(GameMode.CLASSIC)
                        viewModel.navigateTo(AppScreen.SETUP)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("start_party_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = DevilCrimson),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.8f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "START NEW GAME 🔥",
                            fontSize = 20.sp,
                            fontFamily = BebasNeueFontFamily,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            color = Color.White
                        )
                    }
                }
            }

            // Section Header: GAME MODES
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "GAME MODES",
                        color = DevilGold,
                        fontSize = 18.sp,
                        fontFamily = BebasNeueFontFamily,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(SinBorder)
                    )
                }
            }

            // Game Mode Cards
            item {
                GameModeCard(
                    title = "CLASSIC DEVIL'S GAME",
                    subtitle = "Turn-based party rounds. One player is the Devil (Judge), players submit physical cards.",
                    tag = "RECOMMENDED",
                    emoji = "👑",
                    onClick = {
                        viewModel.setGameMode(GameMode.CLASSIC)
                        viewModel.navigateTo(AppScreen.SETUP)
                    }
                )
            }

            item {
                GameModeCard(
                    title = "CHAOS GRIND MODE",
                    subtitle = "High pressure 45-second round timer. Slowest submission takes an automatic penalty!",
                    tag = "FAST PACED",
                    emoji = "⚡",
                    onClick = {
                        viewModel.setGameMode(GameMode.CHAOS)
                        viewModel.navigateTo(AppScreen.SETUP)
                    }
                )
            }

            item {
                GameModeCard(
                    title = "ALF DRINKING STAKES",
                    subtitle = "Brutal house drinking rules integrated into every prompt and loser punishment.",
                    tag = "18+ PARTY",
                    emoji = "🥃",
                    onClick = {
                        viewModel.setGameMode(GameMode.DRINKING)
                        viewModel.navigateTo(AppScreen.SETUP)
                    }
                )
            }

            // Section Header: TOOLS & DECKS
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DECK UTILITIES",
                        color = DevilGold,
                        fontSize = 18.sp,
                        fontFamily = BebasNeueFontFamily,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(SinBorder)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    UtilityMiniCard(
                        title = "QUICK DRAW",
                        subtitle = "Solo / host prompt flipper",
                        icon = Icons.Default.ViewCarousel,
                        modifier = Modifier.weight(1f),
                        testTag = "nav_quick_draw",
                        onClick = { viewModel.navigateTo(AppScreen.QUICK_DRAW) }
                    )
                    UtilityMiniCard(
                        title = "DIGITAL HAND",
                        subtitle = "Backup digital answer cards",
                        icon = Icons.Default.Style,
                        modifier = Modifier.weight(1f),
                        testTag = "nav_digital_deck",
                        onClick = { viewModel.navigateTo(AppScreen.DIGITAL_DECK) }
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    UtilityMiniCard(
                        title = "CUSTOM CARDS",
                        subtitle = "Add your house prompts",
                        icon = Icons.Default.Add,
                        modifier = Modifier.weight(1f),
                        testTag = "nav_custom_cards",
                        onClick = { viewModel.navigateTo(AppScreen.CUSTOM_CARDS) }
                    )
                    UtilityMiniCard(
                        title = "HOW TO PLAY",
                        subtitle = "Official physical rules",
                        icon = Icons.Default.Book,
                        modifier = Modifier.weight(1f),
                        testTag = "nav_rules",
                        onClick = { viewModel.navigateTo(AppScreen.RULES) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun StatPill(top: String, bottom: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = SinSurfaceVariant,
        border = BorderStroke(1.dp, SinBorder)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = top,
                color = DevilGold,
                fontFamily = BebasNeueFontFamily,
                fontSize = 13.sp,
                letterSpacing = 0.5.sp
            )
            Text(
                text = bottom,
                color = SinGrey,
                fontSize = 9.sp,
                fontFamily = MontserratFontFamily
            )
        }
    }
}

@Composable
private fun GameModeCard(
    title: String,
    subtitle: String,
    tag: String,
    emoji: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SinSurface),
        border = BorderStroke(1.5.dp, SinBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = emoji,
                fontSize = 30.sp,
                modifier = Modifier.padding(end = 12.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        color = SinWhite,
                        fontSize = 17.sp,
                        fontFamily = BebasNeueFontFamily,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        color = DevilBlood,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = tag,
                            color = Color.White,
                            fontSize = 9.sp,
                            fontFamily = BebasNeueFontFamily,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    color = SinGrey,
                    fontSize = 12.sp,
                    fontFamily = MontserratFontFamily,
                    lineHeight = 16.sp
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Select",
                tint = DevilCrimson,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun UtilityMiniCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .testTag(testTag)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = SinSurfaceVariant,
        border = BorderStroke(1.2.dp, SinBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = DevilGold,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = SinWhite,
                fontSize = 14.sp,
                fontFamily = BebasNeueFontFamily,
                letterSpacing = 1.sp
            )
            Text(
                text = subtitle,
                color = SinGrey,
                fontSize = 10.sp,
                fontFamily = MontserratFontFamily
            )
        }
    }
}
