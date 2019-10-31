package com.marklynch.compose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.unaryPlus
import androidx.ui.core.Clip
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.core.setContent
import androidx.ui.foundation.DrawImage
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.material.themeTextStyle
import androidx.ui.material.withOpacity
import androidx.ui.res.imageResource
import androidx.ui.text.style.TextOverflow
import androidx.ui.tooling.preview.Preview

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsStory()
        }
    }
}

@Composable
fun NewsStory() {
    val image = +imageResource(R.drawable.weather01d)
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

@Preview
@Composable
fun DefaultPreview() {
    NewsStory()
}
