package com.example.data.model

enum class DeckCategory(
    val title: String,
    val subtitle: String,
    val emoji: String
) {
    CLASSIC("Devil's Classic", "Signature dark & wicked party prompts", "😈"),
    AFTER_HOURS("After Hours & VIP", "Nightclub debauchery, drinks & regrets", "🍸"),
    SIN_CITY("Sin City Noir", "Underworld vices, dark crime & gritty secrets", "💀"),
    SAVAGE_ROAST("Savage Roasts", "Call out everyone in the room", "🔥"),
    NSFW("18+ Uncensored", "Taboo confessions & wild bedroom secrets", "🌶️"),
    CUSTOM("House Sins", "Your custom generated & written prompts", "✍️")
}

enum class PunishmentSeverity(val label: String, val emoji: String) {
    MILD("Mild Sinner", "🍷"),
    SPICY("Hot as Hell", "🔥"),
    BRUTAL("Eternal Damnation", "💀")
}

data class PromptCard(
    val id: String,
    val text: String,
    val pickCount: Int = 1,
    val category: DeckCategory = DeckCategory.CLASSIC,
    val drinkingRule: String? = null,
    val isCustom: Boolean = false
)

data class PunishmentCard(
    val id: String,
    val title: String,
    val description: String,
    val severity: PunishmentSeverity = PunishmentSeverity.SPICY
)

data class DigitalAnswerCard(
    val id: String,
    val text: String
)

data class Player(
    val id: String,
    val name: String,
    val avatarEmoji: String = "😈",
    val score: Int = 0,
    val isDevil: Boolean = false
)

enum class GameMode(
    val title: String,
    val tagLine: String,
    val defaultTimerSeconds: Int,
    val iconEmoji: String
) {
    CLASSIC("Classic Devil's Game", "Physical cards in hand, rotating Devil judge", 0, "👑"),
    CHAOS("Chaos Grind Mode", "High adrenaline 45s timer & rapid round action", 45, "⚡"),
    DRINKING("ALF Drinking Stakes", "Drink on losses, double shots on ties", 60, "🥃"),
    QUICK_DRAW("Quick Draw Prompts", "Solo/host prompt flipper for house parties", 0, "🃏")
}

enum class RoundPhase {
    PROMPT_REVEAL,    // Prompt shown, timer running, players submitting physical cards
    DELIBERATION,     // Devil reads all answers submitted
    JUDGMENT,         // Devil picks the winner
    PUNISHMENT_DRAW,  // Devil optionally reveals a punishment for the loser
    ROUND_SUMMARY     // Scores updated, winner announced, next Devil queued
}

data class RoundHistoryItem(
    val roundNumber: Int,
    val devilName: String,
    val promptText: String,
    val winnerName: String,
    val punishmentGiven: String? = null
)
