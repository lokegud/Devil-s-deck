package com.example.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PromptCard
import com.example.ui.theme.BebasNeueFontFamily
import com.example.ui.theme.DevilBlood
import com.example.ui.theme.DevilCrimson
import com.example.ui.theme.DevilGold
import com.example.ui.theme.DevilRed
import com.example.ui.theme.MontserratFontFamily
import com.example.ui.theme.SinBlack
import com.example.ui.theme.SinBorder
import com.example.ui.theme.SinGrey
import com.example.ui.theme.SinSurfaceVariant
import com.example.ui.theme.SinWhite

@Composable
fun DevilPromptCardView(
    prompt: PromptCard,
    modifier: Modifier = Modifier,
    onCardClick: (() -> Unit)? = null
) {
    var flipped by remember(prompt.id) { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (flipped) 360f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "cardFlip"
    )

    Card(
        modifier = modifier
            .testTag("prompt_card_view")
            .shadow(16.dp, RoundedCornerShape(18.dp), spotColor = DevilRed)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .then(
                if (onCardClick != null) Modifier.clickable {
                    flipped = !flipped
                    onCardClick()
                } else Modifier
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SinBlack),
        border = BorderStroke(2.5.dp, Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Row: Category Badge & Pick Count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Chip
                Surface(
                    color = SinSurfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, SinBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = prompt.category.emoji,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = prompt.category.title.uppercase(),
                            color = SinGrey,
                            fontSize = 11.sp,
                            fontFamily = BebasNeueFontFamily,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Pick Badge
                Surface(
                    color = if (prompt.pickCount > 1) DevilBlood else DevilCrimson,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "PICK ${prompt.pickCount}",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontFamily = BebasNeueFontFamily,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Card Text (High contrast stark white on pure black)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = prompt.text,
                    color = SinWhite,
                    fontSize = 24.sp,
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 34.sp,
                    letterSpacing = 0.3.sp
                )
            }

            // Optional Drinking Rule Bar
            if (!prompt.drinkingRule.isNullOrBlank()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    color = DevilGold.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, DevilGold.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalBar,
                            contentDescription = "Drinking Rule",
                            tint = DevilGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = prompt.drinkingRule,
                            color = DevilGold,
                            fontSize = 12.sp,
                            fontFamily = MontserratFontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Footer branding
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "DEVIL'S DECK",
                        color = DevilCrimson,
                        fontFamily = BebasNeueFontFamily,
                        fontSize = 16.sp,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = "• ALF COMPANION",
                        color = SinGrey.copy(alpha = 0.6f),
                        fontSize = 10.sp,
                        fontFamily = MontserratFontFamily
                    )
                }

                Text(
                    text = if (prompt.isCustom) "CUSTOM CARD" else "#${prompt.id.uppercase()}",
                    color = SinGrey.copy(alpha = 0.5f),
                    fontSize = 10.sp,
                    fontFamily = BebasNeueFontFamily
                )
            }
        }
    }
}
