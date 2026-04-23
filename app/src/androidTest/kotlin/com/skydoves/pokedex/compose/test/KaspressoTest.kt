package com.skydoves.pokedex.compose.test

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.skydoves.pokedex.compose.screen.HomeScreen
import org.junit.Rule
import org.junit.Test
import com.skydoves.pokedex.compose.MainActivity
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.test.espresso.Espresso
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import androidx.compose.ui.test.hasTestTag
import org.junit.Before
import com.skydoves.pokedex.compose.screen.CardScreen
import com.skydoves.pokedex.compose.screen.SettingsScreen

@HiltAndroidTest
class KaspressoTest : TestCase() {

  @get:Rule(order = 0)
  val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1)
  val composeTestRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    hiltRule.inject()
  }


  @OptIn(ExperimentalTestApi::class)
  @Test
  fun testHomeScreenElements() = run {

    step("Ждем загрузки главного экрана") {
      composeTestRule.waitUntilAtLeastOneExists(
        hasTestTag("AppBarTitle"),
        timeoutMillis = 15_000L
      )
    }

    step("Проверяем элементы на главном экране") {

      val homeScreen = HomeScreen(composeTestRule)

      homeScreen.apply {
        titleText {
          assertIsDisplayed()
          assertTextEquals("Pokedex Compose")
        }

        settingsButton {
          assertIsDisplayed()
          assertIsEnabled()
          assertHasClickAction()
          performClick()
        }

        settingsScreen {
          assertIsDisplayed()
        }

        Espresso.pressBack()

        composeTestRule.waitUntilAtLeastOneExists(
          hasTestTag("PokedexList"),
          timeoutMillis = 5_000L
        )

        pokemonList {
          assertIsDisplayed()
        }

        composeTestRule.waitUntilAtLeastOneExists(
          hasTestTag("Pokemon_Bulbasaur"),
          timeoutMillis = 5_000L
        )

        bulbasaurCard {
          assertIsDisplayed()
          assertIsEnabled()
          assertHasClickAction()
          performClick()
        }

        pokemonCardScreen {
          assertIsDisplayed()
        }

      }

    }
  }

  @OptIn(ExperimentalTestApi::class)
  @Test
  fun testCardScreenElements() = run {

    step("Ждем загрузки главного экрана") {
      composeTestRule.waitUntilAtLeastOneExists(
        hasTestTag("AppBarTitle"),
        timeoutMillis = 15_000L
      )
    }

    step("Проверяем элементы на экране карточки покемона Bulbasaur") {

      val cardScreen = CardScreen(composeTestRule)

      cardScreen.apply {

        composeTestRule.waitUntilAtLeastOneExists(
          hasTestTag("Pokemon_Bulbasaur"),
          timeoutMillis = 5_000L
        )

        bulbasaurCard {
          assertIsDisplayed()
          assertIsEnabled()
          assertHasClickAction()
          performClick()
        }

        composeTestRule.waitUntilAtLeastOneExists(
          hasTestTag("PokemonName"),
          timeoutMillis = 5_000L
        )

        pokemonName{
          assertIsDisplayed()
          assertTextEquals("Bulbasaur")
        }

        textNearBack{
          assertIsDisplayed()
          assertTextEquals("Bulbasaur")
        }

        pokemonNumber{
          assertIsDisplayed()
          assertTextEquals("#001")
        }

        pokemonType(0).assertIsDisplayed()
        pokemonType(0).assertTextEquals("grass")

        pokemonType(1).assertIsDisplayed()
        pokemonType(1).assertTextEquals("poison")

        weightValue{
          assertIsDisplayed()
          assertTextEquals("6.9 KG")
        }

        heightValue{
          assertIsDisplayed()
          assertTextEquals("0.7 M")
        }

        pokemonStatusProgressBar(0).assertIsDisplayed()
        pokemonStatusProgressBar(0).assertContentDescriptionEquals("Progress: 15%")

        pokemonStatusProgressBar(1).assertIsDisplayed()
        pokemonStatusProgressBar(1).assertContentDescriptionEquals("Progress: 16%")

        pokemonStatusProgressBar(2).assertIsDisplayed()
        pokemonStatusProgressBar(2).assertContentDescriptionEquals("Progress: 16%")

        pokemonStatusProgressBar(3).assertIsDisplayed()
        pokemonStatusProgressBar(3).assertContentDescriptionEquals("Progress: 15%")

        pokemonStatusProgressBar(4).assertIsDisplayed()
        pokemonStatusProgressBar(4).assertContentDescriptionEquals("Progress: 50%")

      }

    }
  }

  @OptIn(ExperimentalTestApi::class)
  @Test
  fun testSettingsScreenElements() = run {

    step("Ждем загрузки главного экрана") {
      composeTestRule.waitUntilAtLeastOneExists(
        hasTestTag("AppBarTitle"),
        timeoutMillis = 15_000L
      )
    }

    step("Проверяем элементы на экране настроек") {

      val cardScreen = SettingsScreen(composeTestRule)

      cardScreen.apply {

        settingsButton {
          assertIsDisplayed()
          assertIsEnabled()
          assertHasClickAction()
          performClick()
        }

        settingsScreen {
          assertIsDisplayed()
          assertTextEquals("Settings")
        }

        listTitle {
          assertIsDisplayed()
          assertTextEquals("Theme")
        }

        themeRow("FOLLOW_SYSTEM").assertIsDisplayed()
        themeRow("FOLLOW_SYSTEM").performClick()
        themeRow("FOLLOW_SYSTEM").assertIsSelected()
        themeRow("FOLLOW_SYSTEM").assertTextContains("Follow system")

        themeRow("LIGHT").assertIsDisplayed()
        themeRow("LIGHT").performClick()
        themeRow("LIGHT").assertIsSelected()
        themeRow("LIGHT").assertTextContains("Light")

        themeRow("DARK").assertIsDisplayed()
        themeRow("DARK").performClick()
        themeRow("DARK").assertIsSelected()
        themeRow("DARK").assertTextContains("Dark")

        OKButton {
          assertIsDisplayed()
          assertTextEquals("OK")
          performClick()
        }

        homeTitleText {
          assertIsDisplayed()
        }

      }

    }
  }

}