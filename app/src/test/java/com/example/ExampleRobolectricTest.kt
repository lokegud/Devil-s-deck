package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.DefaultDecks
import com.example.data.model.DeckCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Devils Deck", appName)
    }

    @Test
    fun `verify default decks populated`() {
        assertTrue("Prompts should not be empty", DefaultDecks.PROMPT_CARDS.isNotEmpty())
        assertTrue("Punishments should not be empty", DefaultDecks.PUNISHMENT_CARDS.isNotEmpty())
        assertTrue("Digital answers should not be empty", DefaultDecks.DIGITAL_ANSWERS.isNotEmpty())

        val classicCards = DefaultDecks.PROMPT_CARDS.filter { it.category == DeckCategory.CLASSIC }
        assertTrue("Classic cards should exist", classicCards.isNotEmpty())

        val afterHoursCards = DefaultDecks.PROMPT_CARDS.filter { it.category == DeckCategory.AFTER_HOURS }
        assertTrue("After hours cards should exist", afterHoursCards.isNotEmpty())
    }
}
