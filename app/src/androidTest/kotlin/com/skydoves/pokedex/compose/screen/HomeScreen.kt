package com.skydoves.pokedex.compose.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class HomeScreen(semanticsProvider: SemanticsNodeInteractionsProvider) : ComposeScreen<HomeScreen>(
  semanticsProvider = semanticsProvider
)
{
  val titleText: KNode = child {
    hasTestTag("AppBarTitle")
  }

  val settingsButton: KNode = child {
    hasTestTag("SettingsButton")
  }

  val settingsScreen: KNode = child {
    hasTestTag("SettingsTitle")
  }

  val pokemonList: KNode = child {
    hasTestTag("PokedexList")
  }

  val bulbasaurCard: KNode = child {
    hasTestTag("Pokemon_Bulbasaur")
  }

  val pokemonCardScreen: KNode = child {
    hasTestTag("PokemonName")
  }

}