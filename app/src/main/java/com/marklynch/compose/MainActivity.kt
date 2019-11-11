package com.marklynch.compose

import android.annotation.SuppressLint
import android.drm.DrmStore
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.ui.animation.Crossfade
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.core.setContent
import androidx.ui.foundation.DrawImage
import androidx.ui.graphics.Image
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.material.surface.Surface
import androidx.ui.res.imageResource
import androidx.ui.tooling.preview.Preview
import com.marklynch.compose.data.ManualLocation
import com.marklynch.compose.livedata.WeatherResponse
import com.marklynch.compose.viewmodel.MainViewModel
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    //Weather screen
    private lateinit var weatherImage: State<Image>
    private lateinit var temperature: State<String>
    private lateinit var weatherDescription: State<String>
    private lateinit var maximumTemperature: State<String>
    private lateinit var minimumTemperature: State<String>
    private lateinit var wind: State<String>
    private lateinit var humidity: State<String>
    private lateinit var cloudiness: State<String>

    //Messaging screen
    private lateinit var message: State<String>

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        setContent {
            WeatherApp()
        }


        //Weather
        viewModel.weatherLiveData.observe(this,
            Observer<WeatherResponse> { weatherResponse ->
                if (weatherResponse == null) {
//                    showNoNetworkConnectionDialog()
//                    swip_refresh_layout.isRefreshing = false
                } else {
                    updateWeatherUI()
//                    if (alertDialog?.isShowing == true) {
//                        alertDialog?.dismiss()
//                    }
//                    swip_refresh_layout.isRefreshing = false
//                    tv_messaging.visibility = android.view.View.GONE
//                    ll_weather_info.visibility = android.view.View.VISIBLE
//                    tv_time_of_last_refresh.text = generateTimeString(viewModel.isUse24hrClock())
                }
            })

        viewModel.fetchWeather(ManualLocation(1, "Loc", 5.0, 5.0))
    }

    private fun updateWeatherUI() {
        if (viewModel.getWeather() == null)
            return

//        tv_messaging.visibility = View.GONE
//        ll_weather_info.visibility = View.VISIBLE

        val weatherResponse = viewModel.getWeather()
        val useCelsius = true//viewModel.isUseCelsius()
        val useKm = true//viewModel.isUseKm()

        if (useCelsius == null || !useCelsius) {
            temperature.value =
                kelvinToFahrenheit(weatherResponse?.main?.temp).roundToInt().toString() + getString(
                    R.string.degreesF
                )
            maximumTemperature.value = getString(
                R.string.maximum_temperature_F,
                kelvinToFahrenheit(weatherResponse?.main?.tempMax).roundToInt()
            )
            minimumTemperature.value = getString(
                R.string.minimum_temperature_F,
                kelvinToFahrenheit(weatherResponse?.main?.tempMin).roundToInt()
            )
        } else {
            temperature.value =
                kelvinToCelsius(weatherResponse?.main?.temp).roundToInt().toString() + getString(R.string.degreesC)
            maximumTemperature.value =
                getString(
                    R.string.maximum_temperature_C,
                    kelvinToCelsius(weatherResponse?.main?.tempMax).roundToInt()
                )
            minimumTemperature.value =
                getString(
                    R.string.minimum_temperature_C,
                    kelvinToCelsius(weatherResponse?.main?.tempMin).roundToInt()
                )
        }

        if (useKm == null || !useKm) {
            wind.value = getString(
                R.string.wind_mi,
                metresPerSecondToMilesPerHour(weatherResponse?.wind?.speed ?: 0.0).roundToInt(),
                directionInDegreesToCardinalDirection(weatherResponse?.wind?.deg ?: 0.0)
            )
        } else {
            wind.value = getString(
                R.string.wind_km,
                metresPerSecondToKmPerHour(weatherResponse?.wind?.speed ?: 0.0).roundToInt(),
                directionInDegreesToCardinalDirection(weatherResponse?.wind?.deg ?: 0.0)
            )
        }

//        weatherImage.value =
//            imageResource(mapWeatherCodeToDrawable[weatherResponse?.weather?.getOrNull(0)?.icon])
//                ?:
//                        +imageResource(R.drawable.weather01d)

        weatherDescription.value =
            weatherResponse?.weather?.getOrNull(0)?.description?.capitalizeWords() ?: ""

//        if (viewModel.getSelectedLocationId() == 0L && weatherResponse?.name != null) {
//            spinnerList[0] =
//                getString(R.string.current_location_brackets_name, weatherResponse.name)
//            val spinner = findViewById<Spinner>(R.id.spinner_select_location)
//            spinner.invalidate()
//            spinnerArrayAdapter.notifyDataSetChanged()
//        }
//        tv_time_of_last_refresh.text = generateTimeString(viewModel.isUse24hrClock())

        humidity.value =
            getString(R.string.humidity_percentage, weatherResponse?.main?.humidity?.roundToInt())
        cloudiness.value =
            getString(R.string.cloudiness_percentage, weatherResponse?.clouds?.all?.roundToInt())

        message.value = ""
    }


    @Composable
    fun WeatherApp() {
        temperature = +state { "" }
        weatherDescription = +state { "" }
        weatherImage = +state { +imageResource(R.drawable.weather01d) }
        maximumTemperature = +state { "" }
        minimumTemperature = +state { "" }
        wind = +state { "" }
        humidity = +state { "" }
        cloudiness = +state { "" }

        message = +state { "FETCHING YOUR WEATHER..." }

        MaterialTheme(
            colors = lightThemeColors
        ) { AppContent() }
    }

    @Composable
    private fun AppContent() {
        Crossfade(WeatherAppStatus.currentScreen) { screen ->
            Surface(color = +themeColor { background }) {
                WeatherScreen()
            }
        }
    }


    @Composable
    fun WeatherScreen() {

        FlexColumn {
            flexible(flex = 1f) {
                MaterialTheme {
                    Column(
                        crossAxisSize = LayoutSize.Expand,
                        crossAxisAlignment = CrossAxisAlignment.Center,
                        modifier = Spacing(16.dp)
                    ) {

                        Text(
                            temperature.value,
                            style = (+themeTextStyle { h1 }).withOpacity(0.87f)
                        )

                        Row {
                            Text(
                                weatherDescription.value,
                                style = (+themeTextStyle { h6 }).withOpacity(0.87f)
                            )
                            Container(width = 32.dp, height = 32.dp) {
                                DrawImage(weatherImage.value)
                            }
                        }
                        Text(
                            humidity.value,
                            style = (+themeTextStyle { body1 }).withOpacity(0.6f)
                        )
                        Text(
                            maximumTemperature.value,
                            style = (+themeTextStyle { body1 }).withOpacity(0.6f)
                        )
                        Text(
                            minimumTemperature.value,
                            style = (+themeTextStyle { body1 }).withOpacity(0.6f)
                        )
                        Text(
                            wind.value,
                            style = (+themeTextStyle { body1 }).withOpacity(0.6f)
                        )
                        Text(
                            cloudiness.value,
                            style = (+themeTextStyle { body1 }).withOpacity(0.6f)
                        )
                        Text(
                            message.value,
                            style = (+themeTextStyle { h6 }).withOpacity(0.87f)
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
        object Messaging : Screen()
    }

    @Model
    object WeatherAppStatus {
        var currentScreen: Screen = Screen.Messaging
    }

    fun navigateTo(destination: Screen) {
        WeatherAppStatus.currentScreen = destination
    }
}

fun kelvinToCelsius(kelvin: Double?) = if (kelvin == null) 0.0 else kelvin - 273.15
fun kelvinToFahrenheit(kelvin: Double?) = if (kelvin == null) 0.0 else kelvin * 9 / 5 - 459.67

fun metresPerSecondToKmPerHour(metresPerSecond: Double) = (metresPerSecond * 3.6)
fun metresPerSecondToMilesPerHour(metresPerSecond: Double) = (metresPerSecond * 2.23694)

fun directionInDegreesToCardinalDirection(directionInDegrees: Double): String {
    val directions = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW", "N")
    return directions[(directionInDegrees % 360 / 45).roundToInt()]
}

@SuppressLint("DefaultLocale")
fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize() }

fun <T> MutableLiveData<T>.forceRefresh() {
    this.value = this.value
}




