plugins {
  id("skydoves.pokedex.android.feature")
  id("skydoves.pokedex.android.hilt")
}

android {
  namespace = "com.skydoves.pokedex.compose.feature.home"
}


dependencies {
  testImplementation(projects.core.test)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.turbine)
  testImplementation(libs.mockito.core)
  testImplementation(libs.mockito.kotlin)
}

tasks.withType<Test>().configureEach {
  systemProperties(mapOf(
    "allure.results.directory" to layout.buildDirectory.dir("allure-results").get().asFile.absolutePath
  ))
}

tasks.register("allureReport", Exec::class) {
  group = "allure"
  description = "Generate Allure report"
  commandLine = listOf(
    "allure",
    "serve",
    layout.buildDirectory.dir("allure-results").get().asFile.absolutePath
  )
  dependsOn("/opt/homebrew/bin/allure")
}