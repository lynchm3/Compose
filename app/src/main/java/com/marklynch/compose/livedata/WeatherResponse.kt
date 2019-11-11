package com.marklynch.compose.livedata

import com.fasterxml.jackson.annotation.JsonProperty
import com.marklynch.compose.R


class WeatherResponse {

    @JsonProperty("coord")
    var coord: Coord? = null
    @JsonProperty("sys")
    var sys: Sys? = null
    @JsonProperty("weather")
    var weather: List<Weather> = listOf()
    @JsonProperty("main")
    var main: Main? = null
    @JsonProperty("wind")
    var wind: Wind? = null
    @JsonProperty("rain")
    var rain: Rain? = null
    @JsonProperty("clouds")
    var clouds: Clouds? = null
    @JsonProperty("dt")
    var dt = 0.0
    @JsonProperty("id")
    var id: Int = 0
    @JsonProperty("name")
    var name: String? = null
    @JsonProperty("cod")
    var cod = 0.0

    override fun toString(): String {
        return "WeatherResponse(coord=$coord, sys=$sys, weather=$weather, main=$main, wind=$wind, rain=$rain, clouds=$clouds, dt=$dt, id=$id, name=$name, cod=$cod)"
    }

    companion object {
        val mapWeatherCodeToDrawable: Map<String, Int> = mapOf(
            "01d" to R.drawable.weather01d
//            ,
//            "01n" to R.drawable.weather01n,
//            "02d" to R.drawable.weather02d,
//            "02n" to R.drawable.weather02n,
//            "03d" to R.drawable.weather03d,
//            "03n" to R.drawable.weather03d,
//            "04d" to R.drawable.weather04d,
//            "04n" to R.drawable.weather04d,
//            "09d" to R.drawable.weather09d,
//            "09n" to R.drawable.weather09d,
//            "10d" to R.drawable.weather10d,
//            "10n" to R.drawable.weather10n,
//            "11d" to R.drawable.weather11d,
//            "11n" to R.drawable.weather11d,
//            "13d" to R.drawable.weather13d,
//            "13n" to R.drawable.weather13d,
//            "50d" to R.drawable.weather50d,
//            "50n" to R.drawable.weather50d
        )

    }


}

class Weather {
    @JsonProperty("id")
    var id: Int = 0
    @JsonProperty("main")
    var main: String? = null
    @JsonProperty("description")
    var description: String? = null
    @JsonProperty("icon")
    var icon: String? = null

    override fun toString(): String {
        return "Weather(id=$id, main=$main, description=$description, icon=$icon)"
    }
}

class Clouds {
    @JsonProperty("all")
    var all = 0.0

    override fun toString(): String {
        return "Clouds(all=$all)"
    }
}

class Rain {
    @JsonProperty("3h")
    var h3 = 0.0

    override fun toString(): String {
        return "Rain(h3=$h3)"
    }
}

class Wind {
    @JsonProperty("speed")
    var speed = 0.0
    @JsonProperty("deg")
    var deg = 0.0

    override fun toString(): String {
        return "Wind(speed=$speed, deg=$deg)"
    }
}

class Main {
    @JsonProperty("temp")
    var temp = 0.0
    @JsonProperty("humidity")
    var humidity = 0.0
    @JsonProperty("pressure")
    var pressure = 0.0
    @JsonProperty("temp_min")
    var tempMin = 0.0
    @JsonProperty("temp_max")
    var tempMax = 0.0

    override fun toString(): String {
        return "Main(temp=$temp, humidity=$humidity, pressure=$pressure, tempMin=$tempMin, tempMax=$tempMax)"
    }
}

class Sys {
    @JsonProperty("country")
    var country: String? = null
    @JsonProperty("sunrise")
    var sunrise: Long = 0
    @JsonProperty("sunset")
    var sunset: Long = 0

    override fun toString(): String {
        return "Sys(country=$country, sunrise=$sunrise, sunset=$sunset)"
    }
}

class Coord {
    @JsonProperty("lon")
    var lon = 0.0
    @JsonProperty("lat")
    var lat = 0.0

    override fun toString(): String {
        return "Coord(lon=$lon, lat=$lat)"
    }
}