package com.skydoves.pokedex.compose.feature.home

import app.cash.turbine.test
import com.skydoves.pokedex.compose.core.data.repository.home.HomeRepository
import com.skydoves.pokedex.compose.core.model.Pokemon
import com.skydoves.pokedex.compose.core.test.MainCoroutinesRule
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class HomeViewModelTest {

  @get:Rule
  val coroutinesRule = MainCoroutinesRule()

  private val homeRepository: HomeRepository = mock()
  private lateinit var viewModel: HomeViewModel

  @Before
  fun setUp() {
    viewModel = HomeViewModel(homeRepository)
  }

  @Test
  fun `initial state is Loading`() = runTest {
    assertEquals(HomeUiState.Loading, viewModel.uiState.value)
  }

  @Test
  fun `fetch pokemon list on init`() = runTest {
    val mockPokemonList = listOf(Pokemon(nameField = "bulbasaur", url = "url"))

    whenever(homeRepository.fetchPokemonList(any(), any(), any(), any(), any()))
      .doReturn(flowOf(mockPokemonList))

    viewModel.pokemonList.test {
      val item = awaitItem()
      if (item.isEmpty()) {
        assertEquals(mockPokemonList, awaitItem())
      } else {
        assertEquals(mockPokemonList, item)
      }
    }
  }

  @Test
  fun `fetchNextPokemonList increments page when not loading and not last page`() = runTest {
    whenever(homeRepository.fetchPokemonList(any(), any(), any(), any(), any())).thenAnswer { invocation ->
      val onComplete = invocation.arguments[2] as () -> Unit
      onComplete()
      flowOf(emptyList<Pokemon>())
    }

    val job = backgroundScope.launch { viewModel.pokemonList.collect() }

    viewModel.uiState.test {
      val state = awaitItem()
      if (state == HomeUiState.Loading) {
        assertEquals(HomeUiState.Idle, awaitItem())
      }

      viewModel.fetchNextPokemonList()

      verify(homeRepository, org.mockito.kotlin.times(2)).fetchPokemonList(any(), any(), any(), any(), any())
    }

    job.cancel()
  }

  @Test
  fun `fetchNextPokemonList does not increment page if currently loading`() = runTest {
    whenever(homeRepository.fetchPokemonList(any(), any(), any(), any(), any()))
      .doReturn(flowOf(emptyList()))

    viewModel.pokemonList.test {
      awaitItem()
      assertEquals(HomeUiState.Loading, viewModel.uiState.value)

      viewModel.fetchNextPokemonList()

      verify(homeRepository, org.mockito.kotlin.times(1)).fetchPokemonList(any(), any(), any(), any(), any())
    }
  }

  @Test
  fun `fetchNextPokemonList does not increment page if last page is reached`() = runTest {
    whenever(homeRepository.fetchPokemonList(any(), any(), any(), any(), any())).thenAnswer { invocation ->
      val onComplete = invocation.arguments[2] as () -> Unit
      val onLastPageReached = invocation.arguments[3] as () -> Unit

      onLastPageReached()
      onComplete()

      flowOf(emptyList<Pokemon>())
    }

    val job = backgroundScope.launch { viewModel.pokemonList.collect() }

    viewModel.uiState.test {
      val state = awaitItem()
      if (state == HomeUiState.Loading) {
        assertEquals(HomeUiState.Idle, awaitItem())
      }

      viewModel.fetchNextPokemonList()
      verify(homeRepository, org.mockito.kotlin.times(1)).fetchPokemonList(any(), any(), any(), any(), any())
    }

    job.cancel()
  }

}
