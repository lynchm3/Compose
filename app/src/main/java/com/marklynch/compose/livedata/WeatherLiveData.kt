package com.marklynch.compose.livedata

import androidx.lifecycle.MutableLiveData
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

import okhttp3.OkHttpClient
import retrofit2.converter.jackson.JacksonConverterFactory


open class WeatherLiveData : MutableLiveData<WeatherResponse>() {

    private val appId = "74f01822a2b8950db2986d7e28a5978a"

    fun fetchWeather(lat: Double = 0.0, lon: Double = 0.0) {

        if (lat == 0.0 && lon == 0.0)
            return


        GlobalScope.launch {

            delay(1_000)

            val retrofit = getRetrofitInstance("https://api.openweathermap.org")

            val apiService = retrofit.create(RestApiService::class.java)

            val weatherResponse = jacksonObjectMapper().readValue<WeatherResponse>(sampleWeather)
            println("weatherResponse = $weatherResponse")
            postValue(weatherResponse)

            val call = apiService.getCurrentWeatherData(lat, lon, appId)

//            call.enqueue(object : Callback<WeatherResponse> {
//                override fun onFailure(call: Call<WeatherResponse>?, t: Throwable?) {
//                    postValue(null)
//                }
//
//                override fun onResponse(
//                    call: Call<WeatherResponse>,
//                    weatherResponseWrapper: Response<WeatherResponse>
//                ) {
//                    val weatherResponse = weatherResponseWrapper.body()
//                    postValue(weatherResponse)
//                }
//            })
        }
    }

    private fun getRetrofitInstance(baseUrl: String): Retrofit {

//        val b = HttpUrl(baseUrl)
//        val httpURL: HttpUrl by inject {
//            parametersOf(baseUrl)
//        }
        val httpURL =
            baseUrl.toHttpUrlOrNull() ?: throw IllegalArgumentException("Illegal URL: $baseUrl")

        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor(ChuckInterceptor(get<Application>()))
            .build()

        return Retrofit.Builder()
            .baseUrl(httpURL)
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

    interface RestApiService {
        @GET("data/2.5/weather?")
        fun getCurrentWeatherData(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") app_id: String): Call<WeatherResponse>


    }
}

val sampleWeather = """{"coord": { "lon": 139,"lat": 35},
  "weather": [
    {
      "id": 800,
      "main": "Clear",
      "description": "clear sky",
      "icon": "01n"
    }
  ],
  "main": {
    "temp": 289.92,
    "pressure": 1009,
    "humidity": 92,
    "temp_min": 288.71,
    "temp_max": 290.93
  },
  "wind": {
    "speed": 0.47,
    "deg": 107.538
  },
  "clouds": {
    "all": 2
  },
  "dt": 1560350192,
  "sys": {
    "country": "JP",
    "sunrise": 1560281377,
    "sunset": 1560333478
  },
  "id": 1851632,
  "name": "Shuzenji",
  "cod": 200
}"""