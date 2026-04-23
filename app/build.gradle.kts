import com.skydoves.pokedex.compose.Configuration
import java.io.FileInputStream
import java.util.Properties

plugins {
  id("skydoves.pokedex.android.application")
  id("skydoves.pokedex.android.application.compose")
  id("skydoves.pokedex.android.hilt")
  id("skydoves.pokedex.spotless")
  alias(libs.plugins.kotlin.parcelize)
  alias(libs.plugins.baselineprofile)
  alias(libs.plugins.hotswan.compiler)
}

android {
  namespace = "com.skydoves.pokedex.compose"

  defaultConfig {
    applicationId = "com.skydoves.pokedex.compose"
    versionCode = Configuration.versionCode
    versionName = Configuration.versionName
    testInstrumentationRunner = "com.skydoves.pokedex.compose.HiltTestRunner"
  }

  signingConfigs {
    val properties = Properties()
    val localPropertyFile = project.rootProject.file("local.properties")
    if (localPropertyFile.canRead()) {
      properties.load(FileInputStream("$rootDir/local.properties"))
    }
    create("release") {
      storeFile = file(properties["RELEASE_KEYSTORE_PATH"] ?: "../keystores/pokedex.jks")
      keyAlias = properties["RELEASE_KEY_ALIAS"].toString()
      keyPassword = properties["RELEASE_KEY_PASSWORD"].toString()
      storePassword = properties["RELEASE_KEYSTORE_PASSWORD"].toString()
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles("proguard-rules.pro",)
      signingConfig = signingConfigs.getByName("release")

      packaging {
        resources {
          excludes += listOf(
            "DebugProbesKt.bin",
            "kotlin-tooling-metadata.json",
            "kotlin/**",
          )
        }
      }
    }
  }

  buildFeatures {
    buildConfig = true
  }

  hilt {
    enableAggregatingTask = true
  }

  testOptions.unitTests {
    isIncludeAndroidResources = true
    isReturnDefaultValues = true
  }
}

hotSwanCompiler {
  preview {
    sdkModeEnabled.set(true)
    renderDelayMs.set(4000L)
  }
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll(
      "-Xno-param-assertions",
      "-Xno-call-assertions",
      "-Xno-receiver-assertions"
    )
  }
}

// Configure stability analyzer
composeStabilityAnalyzer {
  enabled.set(true)
}

dependencies {
  // compose hotswan
  debugImplementation(libs.hotswan.preview)

  // features
  implementation(projects.feature.home)
  implementation(projects.feature.details)
  implementation(projects.feature.settings)

  // cores
  implementation(projects.core.data)
  implementation(projects.core.model)
  implementation(projects.core.designsystem)
  implementation(projects.core.navigation)

  // compose
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.compose.foundation)

  // di
  implementation(libs.hilt.android)
  ksp(libs.hilt.compiler)
  androidTestImplementation(libs.hilt.testing)
  kspAndroidTest(libs.hilt.compiler)

  // baseline profile
  implementation(libs.profileinstaller)
  baselineProfile(project(":baselineprofile"))

  // unit test
  testImplementation(libs.junit)
  testImplementation(libs.turbine)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.mockito.core)
  testImplementation(libs.mockito.kotlin)
  testImplementation(libs.kotlinx.coroutines.test)
  androidTestImplementation(libs.truth)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso)
  androidTestImplementation(libs.kaspresso)
  androidTestImplementation(libs.kaspresso.compose)
  androidTestImplementation(libs.kotlinx.coroutines.test)
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  androidTestImplementation(libs.kaspresso.allure)
}
val allureResultsDir = layout.buildDirectory.dir("allure-results")
val allureReportDir = layout.buildDirectory.dir("reports/allure-report")
val allureAssetsDir = layout.projectDirectory.dir("src/main/assets/allure-report")

tasks.register<Exec>("pullAllureResults") {
  group = "verification"
  val adbFile = androidComponents.sdkComponents.adb.get().asFile
  val resultsFile = allureResultsDir.get().asFile

  doFirst {
    resultsFile.deleteRecursively()
  }
  executable = adbFile.absolutePath
  args(
    "pull",
    "/sdcard/Documents/allure-results",
    resultsFile.absolutePath
  )

  isIgnoreExitValue = true
}

tasks.register<Exec>("generateAllureReport") {
  group = "verification"

  dependsOn("pullAllureResults")
  val resultsFile = allureResultsDir.get().asFile
  val reportFile = allureReportDir.get().asFile

  doFirst {
    reportFile.deleteRecursively()
  }

  executable = "/opt/homebrew/bin/allure"
  args(
    "generate",
    resultsFile.absolutePath,
    "-o",
    reportFile.absolutePath,
    "--clean"
  )
}

tasks.register<Copy>("saveAllureReportToResources") {
  group = "verification"

  dependsOn("generateAllureReport")

  from(allureReportDir)
  into(allureAssetsDir)
}

tasks.matching {
  it.name == "connectedAndroidTest" || it.name == "connectedDebugAndroidTest"
}.configureEach {
  finalizedBy("saveAllureReportToResources")
}