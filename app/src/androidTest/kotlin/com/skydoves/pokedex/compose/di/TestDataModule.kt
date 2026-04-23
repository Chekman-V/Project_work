package com.skydoves.pokedex.compose.di

import com.skydoves.pokedex.compose.core.data.repository.details.DetailsRepository
import com.skydoves.pokedex.compose.core.data.repository.details.FakeDetailsRepository
import com.skydoves.pokedex.compose.core.data.repository.home.FakeHomeRepository
import com.skydoves.pokedex.compose.core.data.repository.home.HomeRepository
import com.skydoves.pokedex.compose.core.data.repository.userdata.FakeUserDataRepository
import com.skydoves.pokedex.compose.core.data.repository.userdata.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
  components = [SingletonComponent::class],
  replaces = [com.skydoves.pokedex.compose.core.data.di.DataModule::class]
)
interface TestDataModule {

  @Binds
  @Singleton
  fun bindsHomeRepository(fakeHomeRepository: FakeHomeRepository): HomeRepository

  @Binds
  @Singleton
  fun bindsUserDataRepository(fakeUserDataRepository: FakeUserDataRepository): UserDataRepository

  @Binds
  @Singleton
  fun bindsDetailsRepository(fakeDetailsRepository: FakeDetailsRepository): DetailsRepository
}