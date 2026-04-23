package com.skydoves.pokedex.compose.screen


import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode


class CardScreen(semanticsProvider: SemanticsNodeInteractionsProvider) : ComposeScreen<CardScreen>(
  semanticsProvider = semanticsProvider
)
{
  val pokemonName: KNode = child {
    hasTestTag("PokemonName")
  }

  val bulbasaurCard: KNode = child {
    hasTestTag("Pokemon_Bulbasaur")
  }

  val textNearBack: KNode = child {
    hasTestTag("TextNearBack")
  }

  val pokemonNumber: KNode = child {
    hasTestTag("PokemonNumber")
  }

  fun pokemonType(index: Int): KNode = child {
    hasTestTag("PokemonType_$index")
  }

  val pokemonWeight: KNode = child {
    hasTestTag("PokemonWeight")
  }

  val pokemonHeight: KNode = child {
    hasTestTag("PokemonHeight")
  }

  val weightValue: KNode = pokemonWeight.child {
    hasTestTag("InfoItemValue")
  }

  val heightValue: KNode = pokemonHeight.child {
    hasTestTag("InfoItemValue")
  }

  fun pokemonStatus(index: Int): KNode = child {
    hasTestTag("PokemonStatus_$index")
  }

  fun pokemonStatusProgressBar(index: Int): KNode = pokemonStatus(index).child {
    hasTestTag("StatusProgressBar")
  }

}