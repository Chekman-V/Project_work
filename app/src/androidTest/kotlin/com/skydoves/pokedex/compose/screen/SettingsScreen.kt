package com.skydoves.pokedex.compose.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class SettingsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) : ComposeScreen<SettingsScreen>(
  semanticsProvider = semanticsProvider
)
{
  val homeTitleText: KNode = child {
    hasTestTag("AppBarTitle")
  }
  val settingsButton: KNode = child {
    hasTestTag("SettingsButton")
  }

  val settingsScreen: KNode = child {
    hasTestTag("SettingsTitle")
  }

  val listTitle: KNode = child {
    hasTestTag("ListTitle")
  }

  val OKButton: KNode = child {
    hasTestTag("OKButton")
  }

  fun themeRow(themeName: String): KNode = child {
    hasTestTag("ThemeRow_$themeName")
  }

}