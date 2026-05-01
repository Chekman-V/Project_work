package com.skydoves.pokedex.compose

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.uiautomator.UiObject2
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import androidx.test.uiautomator.Direction
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertEquals
import org.junit.After

@RunWith(AndroidJUnit4::class)
class PokedexUiTest {

  private lateinit var device: UiDevice

  @Before
  fun setUp() {
    val instrumentation = InstrumentationRegistry.getInstrumentation()
    device = UiDevice.getInstance(instrumentation)
//    device.executeShellCommand("pm clear com.skydoves.pokedex.compose")
//    Thread.sleep(2000)
    ActivityScenario.launch(MainActivity::class.java)
    Thread.sleep(5000)
  }

//  @After
//  fun after(){
//    val instrumentation = InstrumentationRegistry.getInstrumentation()
//    device = UiDevice.getInstance(instrumentation)
//    device.executeShellCommand("pm uninstall com.skydoves.pokedex.compose")
//  }

  private fun checkElementVisibility(uiObject2: UiObject2?, message: String) {
    if (uiObject2 != null) {
      val bounds = uiObject2.getVisibleBounds()
      assertTrue("$message: элемент не виден пользователю", !bounds.isEmpty)
    } else {
      fail("$message: элемент не найден на экране")
    }
  }

  @Test
  fun testMainScreenElements() {
    val mainText:UiObject2 = device.findObject(By.text("Pokedex Compose"))
    checkElementVisibility(mainText,"Текст заголовка главной страницы")

    val pokemonList:UiObject2 = device.findObject(By.res("PokedexList"))
    checkElementVisibility(pokemonList, "Список покемонов")
    pokemonList.wait(Until.scrollable(true), 5000)
    assertTrue("Список должен быть прокручиваемым", pokemonList.isScrollable)

    val pokemonItems = device.findObjects(By.res("Pokemon"))
    assertTrue("В списке должно быть не менее 4 элементов", pokemonItems.size >= 4)

    val settingButton:UiObject2 = device.findObject(By.clazz("android.widget.Button"))
    assertTrue("Кнопка настроек кликабельна",settingButton.isClickable)
    checkElementVisibility(settingButton, "Кнопка настроек")

    val pokemonNames = listOf("Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon")
    for (name in pokemonNames) {
      val pokemonName = device.findObject(By.text(name))
      checkElementVisibility(pokemonName, "Имя покемона $name")
    }

    val firstPokemon = device.findObjects(By.res("Pokemon"))
    assertTrue("Элемент списка должен быть кликабельным", firstPokemon[0].isClickable)
  }

  @Test
  fun testMainScreenScroll() {
    val pokemonList:UiObject2 = device.findObject(By.res("PokedexList"))
    pokemonList.wait(Until.scrollable(true), 5000)

    val firstPokemon:UiObject2 = device.findObject(By.text("Venusaur"))
    checkElementVisibility(firstPokemon, "Первый покемон")

    device.swipe(
      device.displayWidth / 2,
      device.displayHeight * 15/16,
      device.displayWidth / 2,
      device.displayHeight * 1/16,
      30
    )
    device.swipe(
      device.displayWidth / 2,
      device.displayHeight * 15/16,
      device.displayWidth / 2,
      device.displayHeight * 1/16,
      30
    )
    Thread.sleep(10000)

    val firstPokemonAfterScroll = device.findObject(By.text("Venusaur"))
    try {
      val isVisible = firstPokemonAfterScroll != null && !firstPokemonAfterScroll.getVisibleBounds().isEmpty
      assertTrue("Границ первого покемона не видно", !isVisible)
    } catch (e: androidx.test.uiautomator.StaleObjectException) {
      assertTrue("Границ первого покемона не видно", firstPokemonAfterScroll == null)
    }

  }

  @Test
  fun testBulbasaurCard() {
    val firstPokemon = device.findObjects(By.res("Pokemon"))
    assertTrue("Элемент списка должен быть кликабельным", firstPokemon[0].isClickable)

    firstPokemon[0].click()

    val pokemonDetails = device.wait(Until.findObject(By.res("PokedexDetails")), 10000)
    assertNotNull("Экран деталей покемона должен загрузиться", pokemonDetails)

    val detailsPokemonName = device.wait(Until.findObject(By.text("Bulbasaur")), 3000)
    checkElementVisibility(detailsPokemonName, "Имя покемона")

    val pokemonNumber = device.wait(Until.findObject(By.text("#001")), 3000)
    checkElementVisibility(pokemonNumber, "Номер покемона")

    val grassType = device.wait(Until.findObject(By.text("grass")), 3000)
    checkElementVisibility(grassType, "Тип grass")

    val poisonType = device.wait(Until.findObject(By.text("poison")), 3000)
    checkElementVisibility(poisonType, "Тип poison")

    val weight = device.wait(Until.findObject(By.text("6.9 KG")), 3000)
    checkElementVisibility(weight, "Вес покемона")

    val weightLabel = device.wait(Until.findObject(By.text("Weight")), 3000)
    checkElementVisibility(weightLabel, "Метка веса")

    val height = device.wait(Until.findObject(By.text("0.7 M")), 3000)
    checkElementVisibility(height, "Рост покемона")

    val heightLabel = device.wait(Until.findObject(By.text("Height")), 3000)
    checkElementVisibility(heightLabel, "Метка роста")

    val baseStatsHeader = device.wait(Until.findObject(By.text("Base Stats")), 3000)
    checkElementVisibility(baseStatsHeader, "Заголовок Base Stats")

    val hpLabel = device.wait(Until.findObject(By.text("HP")), 3000)
    checkElementVisibility(hpLabel, "Метка HP")

    val atkLabel = device.wait(Until.findObject(By.text("ATK")), 3000)
    checkElementVisibility(atkLabel, "Метка ATK")

    val defLabel = device.wait(Until.findObject(By.text("DEF")), 3000)
    checkElementVisibility(defLabel, "Метка DEF")

    val spdLabel = device.wait(Until.findObject(By.text("SPD")), 3000)
    checkElementVisibility(spdLabel, "Метка SPD")

    val expLabel = device.wait(Until.findObject(By.text("EXP")), 3000)
    checkElementVisibility(expLabel, "Метка EXP")

    val hpValue = device.wait(Until.findObject(By.text(" 45/300")), 3000)
    checkElementVisibility(hpValue, "Значение HP")

    val atkValue = device.wait(Until.findObject(By.text(" 49/300")), 3000)
    checkElementVisibility(atkValue, "Значение ATK")

    val defValue = device.wait(Until.findObject(By.text(" 49/300")), 3000)
    checkElementVisibility(defValue, "Значение DEF")

    val spdValue = device.wait(Until.findObject(By.text(" 45/300")), 3000)
    checkElementVisibility(spdValue, "Значение SPD")

//    val expValue = device.wait(Until.findObject(By.text(" 403/1000")), 3000)
//    assertNotNull("Значение EXP должно отображаться", expValue)
//    checkElementVisibility(expValue, "Значение EXP")

  }

  @Test
  fun testBulbasaurCardBack() {
    val firstPokemon1 = device.findObjects(By.res("Pokemon"))
    assertTrue("Элемент списка должен быть кликабельным", firstPokemon1[0].isClickable)

    firstPokemon1[0].click()
    Thread.sleep(3000)

    val backButton = device.wait(Until.findObject(By.clickable(true)), 10000)
    checkElementVisibility(backButton, "Кнопка назад")
    backButton.click()
    Thread.sleep(3000)

    val mainText1 = device.wait(Until.findObject(By.text("Pokedex Compose")), 10000)
    checkElementVisibility(mainText1,"Текст заголовка главной страницы")

    val firstPokemon2 = device.wait(Until.findObjects(By.res("Pokemon")),10000)
    assertTrue("Элемент списка должен быть кликабельным", firstPokemon2[0].isClickable)

    firstPokemon2[0].click()
    Thread.sleep(3000)

    val pokemonNumber = device.wait(Until.findObject(By.text("#001")), 10000)
    checkElementVisibility(pokemonNumber, "Номер покемона")

    device.pressBack()
    Thread.sleep(3000)

    val mainText2 = device.wait(Until.findObject(By.text("Pokedex Compose")), 10000)
    checkElementVisibility(mainText2,"Текст заголовка главной страницы")

  }

  @Test
  fun testSettingScreen() {
    val settingButton:UiObject2 =device.wait(Until.findObject(By.clazz("android.widget.Button")), 3000)
    assertTrue("Кнопка настроек кликабельна",settingButton.isClickable)
    settingButton.click()

    val settingsTitle = device.wait(Until.findObject(By.text("Settings")), 3000)
    checkElementVisibility(settingsTitle, "Заголовок Settings")

    val themeText = device.wait(Until.findObject(By.text("Theme")), 3000)
    checkElementVisibility(themeText, "Текст Theme")

    val followSystemRadioButton = device.wait(Until.findObject(By.text("Follow system")), 3000)
    checkElementVisibility(followSystemRadioButton, "Радиокнопка Follow system")

    val darkRadioButton = device.wait(Until.findObject(By.text("Dark")), 3000)
    checkElementVisibility(darkRadioButton, "Радиокнопка Dark")

    val lightRadioButton = device.wait(Until.findObject(By.text("Light")), 3000)
    checkElementVisibility(lightRadioButton, "Радиокнопка Light")

    val okButton = device.wait(Until.findObject(By.text("OK")), 3000)
    checkElementVisibility(okButton, "Кнопка OK")

    val radioButtons = device.findObjects(By.checkable(true))

    assertEquals("Должно быть 3 радиокнопки", 3, radioButtons.size)

    assertTrue("Радиокнопка Follow system должна быть выбрана", radioButtons[0].isChecked)
    assertFalse("Радиокнопка Follow system не должна быть кликабельной", radioButtons[0].isClickable)

    assertFalse("Радиокнопка Dark не должна быть выбрана", radioButtons[1].isChecked)
    assertTrue("Радиокнопка Dark должна быть кликабельной", radioButtons[1].isClickable)

    assertFalse("Радиокнопка Light не должна быть выбрана", radioButtons[2].isChecked)
    assertTrue("Радиокнопка Light должна быть кликабельной", radioButtons[2].isClickable)

    val OKButton2 = device.findObjects(By.clickable(true))
    assertTrue("Кнопка ОК должна быть кликабельной", OKButton2[2].isClickable)

    OKButton2[2].click()

    val mainText:UiObject2 = device.findObject(By.text("Pokedex Compose"))
    checkElementVisibility(mainText,"Текст заголовка главной страницы")

  }

  @Test
  fun testThemeSwitching() {
    val settingButton:UiObject2 =device.wait(Until.findObject(By.clazz("android.widget.Button")), 3000)
    assertTrue("Кнопка настроек кликабельна",settingButton.isClickable)
    settingButton.click()

    val radioButtonsFollow_system  = device.findObjects(By.checkable(true))

    assertTrue("Радиокнопка Follow system должна быть выбрана", radioButtonsFollow_system[0].isChecked)
    assertFalse("Радиокнопка Follow system не должна быть кликабельной", radioButtonsFollow_system[0].isClickable)

    assertFalse("Радиокнопка Dark не должна быть выбрана", radioButtonsFollow_system[1].isChecked)
    assertTrue("Радиокнопка Dark должна быть кликабельной", radioButtonsFollow_system[1].isClickable)

    assertFalse("Радиокнопка Light не должна быть выбрана", radioButtonsFollow_system[2].isChecked)
    assertTrue("Радиокнопка Light должна быть кликабельной", radioButtonsFollow_system[2].isClickable)

    radioButtonsFollow_system[1].click()
    Thread.sleep(3000)

    val radioButtonsDark = device.findObjects(By.checkable(true))

    assertFalse("Радиокнопка Follow system не должна быть выбрана", radioButtonsDark[0].isChecked)
    assertTrue("Радиокнопка Follow system должна быть кликабельной", radioButtonsDark[0].isClickable)

    assertTrue("Радиокнопка Dark должна быть выбрана", radioButtonsDark[1].isChecked)
    assertFalse("Радиокнопка Dark не должна быть кликабельной", radioButtonsDark[1].isClickable)

    assertFalse("Радиокнопка Light не должна быть выбрана", radioButtonsDark[2].isChecked)
    assertTrue("Радиокнопка Light должна быть кликабельной", radioButtonsDark[2].isClickable)

    radioButtonsDark[2].click()
    Thread.sleep(3000)

    val radioButtonsLight = device.findObjects(By.checkable(true))

    assertFalse("Радиокнопка Follow system не должна быть выбрана", radioButtonsLight[0].isChecked)
    assertTrue("Радиокнопка Follow system должна быть кликабельной", radioButtonsLight[0].isClickable)

    assertFalse("Радиокнопка Dark не должна быть выбрана", radioButtonsLight[1].isChecked)
    assertTrue("Радиокнопка Dark должна быть кликабельной", radioButtonsLight[1].isClickable)

    assertTrue("Радиокнопка Light должна быть выбрана", radioButtonsLight[2].isChecked)
    assertFalse("Радиокнопка Light не должна быть кликабельной", radioButtonsLight[2].isClickable)

    radioButtonsLight[0].click()

  }

  @Test
  fun testThemeSaveState() {
    val settingButton:UiObject2 =device.wait(Until.findObject(By.clazz("android.widget.Button")), 3000)
    assertTrue("Кнопка настроек кликабельна",settingButton.isClickable)
    settingButton.click()

    val radioButtonsDark = device.findObjects(By.checkable(true))

    radioButtonsDark[1].click()
    Thread.sleep(3000)

    val OKButton2 = device.findObjects(By.clickable(true))
    assertTrue("Кнопка ОК должна быть кликабельной", OKButton2[2].isClickable)

    OKButton2[2].click()
    Thread.sleep(3000)

    val mainText:UiObject2 = device.findObject(By.text("Pokedex Compose"))
    checkElementVisibility(mainText,"Текст заголовка главной страницы")

    val settingButton2:UiObject2 =device.wait(Until.findObject(By.clazz("android.widget.Button")), 3000)
    assertTrue("Кнопка настроек кликабельна",settingButton2.isClickable)
    settingButton2.click()
    Thread.sleep(3000)

    val radioButtonsAfterReopen = device.findObjects(By.checkable(true))

    assertFalse("Радиокнопка Follow system не должна быть выбрана", radioButtonsAfterReopen[0].isChecked)
    assertTrue("Радиокнопка Follow system должна быть кликабельной", radioButtonsAfterReopen[0].isClickable)

    assertTrue("Радиокнопка Dark должна быть выбрана", radioButtonsAfterReopen[1].isChecked)
    assertFalse("Радиокнопка Dark не должна быть кликабельной", radioButtonsAfterReopen[1].isClickable)

    assertFalse("Радиокнопка Light не должна быть выбрана", radioButtonsAfterReopen[2].isChecked)
    assertTrue("Радиокнопка Light должна быть кликабельной", radioButtonsAfterReopen[2].isClickable)

    radioButtonsAfterReopen[0].click()

  }

  @Test
  fun testFail() {
    val mainText:UiObject2 = device.findObject(By.text("Pikachu"))
    checkElementVisibility(mainText,"Покемон Пикачу")
  }

}
