package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundHelper
import com.example.data.DefaultDecks
import com.example.data.local.CustomCardDao
import com.example.data.local.CustomCardEntity
import com.example.data.local.DevilsDatabase
import com.example.data.model.DeckCategory
import com.example.data.model.DigitalAnswerCard
import com.example.data.model.GameMode
import com.example.data.model.Player
import com.example.data.model.PromptCard
import com.example.data.model.PunishmentCard
import com.example.data.model.RoundHistoryItem
import com.example.data.model.RoundPhase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppScreen {
    HOME,
    SETUP,
    ACTIVE_GAME,
    QUICK_DRAW,
    DIGITAL_DECK,
    CUSTOM_CARDS,
    RULES
}

data class GameUiState(
    val currentScreen: AppScreen = AppScreen.HOME,
    val previousScreen: AppScreen = AppScreen.HOME,
    val gameMode: GameMode = GameMode.CLASSIC,
    val targetSins: Int = 5,
    val selectedCategories: Set<DeckCategory> = setOf(
        DeckCategory.CLASSIC,
        DeckCategory.AFTER_HOURS,
        DeckCategory.SIN_CITY,
        DeckCategory.SAVAGE_ROAST,
        DeckCategory.NSFW
    ),
    val players: List<Player> = listOf(
        Player("p1", "Player 1", "😈", 0, isDevil = true),
        Player("p2", "Player 2", "💀", 0, isDevil = false),
        Player("p3", "Player 3", "🍸", 0, isDevil = false)
    ),
    val currentDevilIndex: Int = 0,
    val roundNumber: Int = 1,
    val roundPhase: RoundPhase = RoundPhase.PROMPT_REVEAL,
    val currentPrompt: PromptCard? = null,
    val activeDeck: List<PromptCard> = emptyList(),
    val timerTotalSeconds: Int = 0,
    val timerRemainingSeconds: Int = 0,
    val isTimerRunning: Boolean = false,
    val currentPunishment: PunishmentCard? = null,
    val showPunishmentDialog: Boolean = false,
    val roundWinner: Player? = null,
    val overallWinner: Player? = null,
    val isGameOver: Boolean = false,
    val roundHistory: List<RoundHistoryItem> = emptyList(),
    // Quick Draw
    val quickDrawPrompt: PromptCard? = null,
    val quickDrawCategoryFilter: DeckCategory? = null,
    // Digital Answers
    val userHand: List<DigitalAnswerCard> = emptyList(),
    val remainingAnswers: List<DigitalAnswerCard> = emptyList()
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val db = DevilsDatabase.getDatabase(application)
    private val customCardDao: CustomCardDao = db.customCardDao()
    val soundHelper = SoundHelper(application)

    val customCards: StateFlow<List<CustomCardEntity>> = customCardDao.getAllCustomCards()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        // Prepare initial Quick Draw prompt
        shuffleQuickDraw(null)
        dealDigitalAnswers()
    }

    fun navigateTo(screen: AppScreen) {
        _uiState.update { it.copy(previousScreen = it.currentScreen, currentScreen = screen) }
    }

    fun navigateBack() {
        _uiState.update {
            val backTarget = if (it.currentScreen == AppScreen.HOME) AppScreen.HOME else AppScreen.HOME
            it.copy(currentScreen = backTarget)
        }
    }

    // --- SETUP LOGIC ---
    fun setGameMode(mode: GameMode) {
        _uiState.update {
            it.copy(
                gameMode = mode,
                timerTotalSeconds = mode.defaultTimerSeconds,
                timerRemainingSeconds = mode.defaultTimerSeconds
            )
        }
    }

    fun setTargetSins(sins: Int) {
        _uiState.update { it.copy(targetSins = sins.coerceIn(2, 20)) }
    }

    fun setTimerSeconds(seconds: Int) {
        _uiState.update {
            it.copy(
                timerTotalSeconds = seconds,
                timerRemainingSeconds = seconds
            )
        }
    }

    fun toggleCategory(category: DeckCategory) {
        _uiState.update { state ->
            val updated = if (state.selectedCategories.contains(category)) {
                if (state.selectedCategories.size > 1) state.selectedCategories - category else state.selectedCategories
            } else {
                state.selectedCategories + category
            }
            state.copy(selectedCategories = updated)
        }
    }

    fun addPlayer(name: String, emoji: String) {
        if (name.isBlank()) return
        _uiState.update { state ->
            val newPlayer = Player(
                id = UUID.randomUUID().toString(),
                name = name.trim(),
                avatarEmoji = emoji,
                score = 0,
                isDevil = state.players.isEmpty()
            )
            state.copy(players = state.players + newPlayer)
        }
    }

    fun removePlayer(playerId: String) {
        _uiState.update { state ->
            if (state.players.size <= 2) return@update state // Minimum 2 players for party game
            val updated = state.players.filterNot { it.id == playerId }
            // Ensure someone is Devil
            val devilIndex = state.currentDevilIndex.coerceAtMost(updated.size - 1)
            val updatedWithDevil = updated.mapIndexed { idx, p ->
                p.copy(isDevil = idx == devilIndex)
            }
            state.copy(players = updatedWithDevil, currentDevilIndex = devilIndex)
        }
    }

    // --- ACTIVE GAME FLOW ---
    fun startGame() {
        val state = _uiState.value
        // Build deck combining default cards and custom cards
        val customEntities = customCards.value
        val customPrompts = customEntities.filterNot { it.isPunishment }.map {
            PromptCard(
                id = it.id,
                text = it.text,
                pickCount = it.pickCount,
                category = DeckCategory.CUSTOM,
                isCustom = true
            )
        }

        val allPool = DefaultDecks.PROMPT_CARDS.filter { state.selectedCategories.contains(it.category) } +
                (if (state.selectedCategories.contains(DeckCategory.CUSTOM)) customPrompts else emptyList())

        val shuffled = (if (allPool.isNotEmpty()) allPool else DefaultDecks.PROMPT_CARDS).shuffled()
        val firstPrompt = shuffled.firstOrNull() ?: DefaultDecks.PROMPT_CARDS.first()
        val remainingDeck = if (shuffled.isNotEmpty()) shuffled.drop(1) else emptyList()

        val resetPlayers = state.players.mapIndexed { idx, player ->
            player.copy(score = 0, isDevil = idx == 0)
        }

        _uiState.update {
            it.copy(
                players = resetPlayers,
                currentDevilIndex = 0,
                roundNumber = 1,
                roundPhase = RoundPhase.PROMPT_REVEAL,
                currentPrompt = firstPrompt,
                activeDeck = remainingDeck,
                isGameOver = false,
                overallWinner = null,
                roundWinner = null,
                roundHistory = emptyList(),
                currentScreen = AppScreen.ACTIVE_GAME
            )
        }

        soundHelper.playCardFlip()
        startRoundTimer()
    }

    private fun startRoundTimer() {
        timerJob?.cancel()
        val totalSec = _uiState.value.timerTotalSeconds
        if (totalSec <= 0) return

        _uiState.update { it.copy(timerRemainingSeconds = totalSec, isTimerRunning = true) }

        timerJob = viewModelScope.launch {
            while (_uiState.value.timerRemainingSeconds > 0 && _uiState.value.isTimerRunning) {
                delay(1000)
                val newSec = _uiState.value.timerRemainingSeconds - 1
                _uiState.update { it.copy(timerRemainingSeconds = newSec) }
                if (newSec in 1..5) {
                    soundHelper.playTimerTick()
                } else if (newSec == 0) {
                    soundHelper.playTimeUpWarning()
                }
            }
            _uiState.update { it.copy(isTimerRunning = false) }
        }
    }

    fun toggleTimer() {
        val isRunning = _uiState.value.isTimerRunning
        if (isRunning) {
            timerJob?.cancel()
            _uiState.update { it.copy(isTimerRunning = false) }
        } else {
            val remaining = _uiState.value.timerRemainingSeconds
            val secToUse = if (remaining <= 0) _uiState.value.timerTotalSeconds else remaining
            _uiState.update { it.copy(timerRemainingSeconds = secToUse, isTimerRunning = true) }
            timerJob = viewModelScope.launch {
                while (_uiState.value.timerRemainingSeconds > 0 && _uiState.value.isTimerRunning) {
                    delay(1000)
                    val newSec = _uiState.value.timerRemainingSeconds - 1
                    _uiState.update { it.copy(timerRemainingSeconds = newSec) }
                    if (newSec in 1..5) {
                        soundHelper.playTimerTick()
                    }
                }
                _uiState.update { it.copy(isTimerRunning = false) }
            }
        }
    }

    fun resetTimer() {
        timerJob?.cancel()
        val total = _uiState.value.timerTotalSeconds
        _uiState.update { it.copy(timerRemainingSeconds = total, isTimerRunning = false) }
    }

    fun moveToDeliberation() {
        timerJob?.cancel()
        _uiState.update {
            it.copy(
                roundPhase = RoundPhase.DELIBERATION,
                isTimerRunning = false
            )
        }
        soundHelper.playCardFlip()
    }

    fun moveToJudgment() {
        _uiState.update { it.copy(roundPhase = RoundPhase.JUDGMENT) }
        soundHelper.playCardFlip()
    }

    fun selectRoundWinner(winner: Player) {
        val target = _uiState.value.targetSins
        val updatedPlayers = _uiState.value.players.map {
            if (it.id == winner.id) it.copy(score = it.score + 1) else it
        }

        val updatedWinner = updatedPlayers.first { it.id == winner.id }
        val isOverallWon = updatedWinner.score >= target

        val currentDevil = _uiState.value.players.getOrNull(_uiState.value.currentDevilIndex)
        val historyRecord = RoundHistoryItem(
            roundNumber = _uiState.value.roundNumber,
            devilName = currentDevil?.name ?: "Devil",
            promptText = _uiState.value.currentPrompt?.text ?: "",
            winnerName = winner.name
        )

        _uiState.update {
            it.copy(
                players = updatedPlayers,
                roundWinner = updatedWinner,
                overallWinner = if (isOverallWon) updatedWinner else null,
                isGameOver = isOverallWon,
                roundPhase = RoundPhase.ROUND_SUMMARY,
                roundHistory = listOf(historyRecord) + it.roundHistory
            )
        }

        soundHelper.playVictoryFanfare()
    }

    fun drawPunishmentForLoser() {
        val pool = DefaultDecks.PUNISHMENT_CARDS
        val randomPunishment = pool.random()
        _uiState.update {
            it.copy(
                currentPunishment = randomPunishment,
                showPunishmentDialog = true
            )
        }
        soundHelper.playPunishmentReveal()
    }

    fun dismissPunishmentDialog() {
        _uiState.update { it.copy(showPunishmentDialog = false) }
    }

    fun rerollPunishment() {
        val pool = DefaultDecks.PUNISHMENT_CARDS
        val newPunishment = pool.filterNot { it.id == _uiState.value.currentPunishment?.id }.randomOrNull()
            ?: pool.random()
        _uiState.update { it.copy(currentPunishment = newPunishment) }
        soundHelper.playCardFlip()
    }

    fun nextRound() {
        val state = _uiState.value
        if (state.isGameOver) return

        // Rotate Devil clockwise
        val nextDevilIndex = (state.currentDevilIndex + 1) % state.players.size
        val updatedPlayers = state.players.mapIndexed { idx, player ->
            player.copy(isDevil = idx == nextDevilIndex)
        }

        // Draw next prompt
        var remaining = state.activeDeck
        val nextPrompt = if (remaining.isNotEmpty()) {
            val card = remaining.first()
            remaining = remaining.drop(1)
            card
        } else {
            // Reshuffle all
            val reshuffled = DefaultDecks.PROMPT_CARDS.shuffled()
            remaining = reshuffled.drop(1)
            reshuffled.first()
        }

        _uiState.update {
            it.copy(
                players = updatedPlayers,
                currentDevilIndex = nextDevilIndex,
                roundNumber = it.roundNumber + 1,
                roundPhase = RoundPhase.PROMPT_REVEAL,
                currentPrompt = nextPrompt,
                activeDeck = remaining,
                roundWinner = null,
                showPunishmentDialog = false
            )
        }

        soundHelper.playCardFlip()
        startRoundTimer()
    }

    // --- QUICK DRAW PROMPT DECK ---
    fun shuffleQuickDraw(category: DeckCategory?) {
        val pool = if (category != null) {
            DefaultDecks.PROMPT_CARDS.filter { it.category == category }
        } else {
            DefaultDecks.PROMPT_CARDS
        }
        val randomPrompt = pool.randomOrNull() ?: DefaultDecks.PROMPT_CARDS.first()
        _uiState.update {
            it.copy(
                quickDrawPrompt = randomPrompt,
                quickDrawCategoryFilter = category
            )
        }
        soundHelper.playCardFlip()
    }

    // --- DIGITAL ANSWER CARDS ---
    fun dealDigitalAnswers() {
        val shuffled = DefaultDecks.DIGITAL_ANSWERS.shuffled()
        val hand = shuffled.take(7)
        val remaining = shuffled.drop(7)
        _uiState.update {
            it.copy(
                userHand = hand,
                remainingAnswers = remaining
            )
        }
    }

    fun drawOneDigitalAnswer() {
        val state = _uiState.value
        val remaining = state.remainingAnswers
        if (remaining.isNotEmpty()) {
            val drawn = remaining.first()
            _uiState.update {
                it.copy(
                    userHand = it.userHand + drawn,
                    remainingAnswers = remaining.drop(1)
                )
            }
            soundHelper.playCardFlip()
        } else {
            dealDigitalAnswers()
        }
    }

    fun discardAnswerCard(cardId: String) {
        _uiState.update { state ->
            state.copy(userHand = state.userHand.filterNot { it.id == cardId })
        }
        drawOneDigitalAnswer()
    }

    // --- CUSTOM CARDS (ROOM) ---
    fun saveCustomCard(text: String, pickCount: Int, category: String, isPunishment: Boolean) {
        if (text.isBlank()) return
        viewModelScope.launch {
            val entity = CustomCardEntity(
                id = "custom_${System.currentTimeMillis()}",
                text = text.trim(),
                pickCount = pickCount,
                category = category,
                isPunishment = isPunishment
            )
            customCardDao.insertCustomCard(entity)
        }
    }

    fun deleteCustomCard(id: String) {
        viewModelScope.launch {
            customCardDao.deleteById(id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        soundHelper.release()
    }
}
