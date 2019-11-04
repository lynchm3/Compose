package com.marklynch.compose

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.state
import androidx.compose.unaryPlus
import androidx.ui.foundation.DrawImage
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.material.surface.Surface
import androidx.ui.res.imageResource
import androidx.ui.tooling.preview.Preview


import androidx.compose.Model
import androidx.compose.frames.ModelList
import androidx.ui.animation.Crossfade
import androidx.ui.core.*

val image = +imageResource(R.drawable.weather01d)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp()
        }
    }
}


@Composable
fun WeatherApp() {

    val (drawerState, onDrawerStateChange) = +state { DrawerState.Closed }

    MaterialTheme(
        colors = lightThemeColors
//       , typography = themeTypography
    ) {
        ModalDrawerLayout(
            drawerState = drawerState,
            onStateChange = onDrawerStateChange,
            gesturesEnabled = drawerState == DrawerState.Opened,
            drawerContent = {
                AppDrawer(
                    currentScreen = JetnewsStatus.currentScreen,
                    closeDrawer = { onDrawerStateChange(DrawerState.Closed) }
                )
            },
            bodyContent = { AppContent { onDrawerStateChange(DrawerState.Opened) } }
        )
    }
}


@Composable
private fun AppContent(openDrawer: () -> Unit) {
    Crossfade(JetnewsStatus.currentScreen) { screen ->
        Surface(color = +themeColor { background }) {

            when (screen) {
                is Screen.Weather -> WeatherScreen { openDrawer() }
//                is Screen.Interests -> InterestsScreen { openDrawer() }
//                is Screen.Article -> ArticleScreen(postId = screen.postId)
            }
        }
    }
}


@Composable
private fun AppDrawer(
    currentScreen: Screen,
    closeDrawer: () -> Unit
) {
    Column(
        crossAxisSize = LayoutSize.Expand,
        mainAxisSize = LayoutSize.Expand
    ) {
        HeightSpacer(24.dp)
        Padding(16.dp) {
            Row {
                DrawImage(image)
                WidthSpacer(8.dp)
                DrawImage(image)
            }
        }
        Divider(color = Color(0x14333333))
        DrawerButton(
            icon = R.drawable.weather01d,
            label = "Home",
            isSelected = currentScreen == Screen.Weather
        ) {
            navigateTo(Screen.Weather)
            closeDrawer()
        }

        DrawerButton(
            icon = R.drawable.weather01d,
            label = "Interests",
            isSelected = currentScreen == Screen.Interests
        ) {
            navigateTo(Screen.Interests)
            closeDrawer()
        }
    }
}

@Composable
private fun DrawerButton(
    @DrawableRes icon: Int,
    label: String,
    isSelected: Boolean,
    action: () -> Unit
) {
    val textIconColor = if (isSelected) {
        +themeColor { primary }
    } else {
        (+themeColor { onSurface }).copy(alpha = 0.6f)
    }
    val backgroundColor = if (isSelected) {
        (+themeColor { primary }).copy(alpha = 0.12f)
    } else {
        +themeColor { surface }
    }

    Padding(left = 8.dp, top = 8.dp, right = 8.dp) {
        Surface(
            color = backgroundColor,
            shape = RoundedCornerShape(4.dp)
        ) {
            Button(onClick = action, style = TextButtonStyle()) {
                Row(
                    mainAxisSize = LayoutSize.Expand,
                    crossAxisAlignment = CrossAxisAlignment.Center
                ) {
                    DrawImage(image)
                    WidthSpacer(16.dp)
                    Text(
                        text = label,
                        style = (+themeTextStyle { body2 }).copy(
                            color = textIconColor
                        )
                    )
                }
            }
        }
    }
}


@Composable
fun WeatherScreen(openDrawer: () -> Unit) {

    FlexColumn {
        inflexible {
            TopAppBar(
                title = { Text(text = "Weather") },
                navigationIcon = {
                    DrawImage(image)
                    Button(text = "DR", onClick = openDrawer, style = TextButtonStyle())
                }
            )
        }
        flexible(flex = 1f) {
            MaterialTheme {
                Column(
                    crossAxisSize = LayoutSize.Expand,
                    crossAxisAlignment = CrossAxisAlignment.Center,
                    modifier = Spacing(16.dp)
                ) {

                    Text(
                        "14°C",
                        style = (+themeTextStyle { h1 }).withOpacity(0.87f)
                    )
                    Row {
                        Text(
                            "Clear Sky",
                            style = (+themeTextStyle { h6 }).withOpacity(0.87f)
                        )
                        Container(width = 32.dp, height = 32.dp) {
                            DrawImage(image)
                        }
                    }
                    Text(
                        "Humidity 46%",
                        style = (+themeTextStyle { body1 }).withOpacity(0.6f)
                    )
                    Text(
                        "Max 15°C",
                        style = (+themeTextStyle { body1 }).withOpacity(0.6f)
                    )
                    Text(
                        "Min 5°C",
                        style = (+themeTextStyle { body1 }).withOpacity(0.6f)
                    )
                    Text(
                        "Wind 3km/h E",
                        style = (+themeTextStyle { body1 }).withOpacity(0.6f)
                    )
                    Text(
                        "Cloudiness 23%",
                        style = (+themeTextStyle { body1 }).withOpacity(0.6f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    WeatherApp()
}

/**
 * Class defining the screens we have in the app: home, article details and interests
 */
sealed class Screen {
    object Weather : Screen()
    data class Article(val postId: String) : Screen()
    object Interests : Screen()
}

@Model
object JetnewsStatus {
    var currentScreen: Screen = Screen.Weather
    val favorites = ModelList<String>()
    val selectedTopics = ModelList<String>()
}

/**
 * Temporary solution pending navigation support.
 */
fun navigateTo(destination: Screen) {
    JetnewsStatus.currentScreen = destination
}


