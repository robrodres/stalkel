package com.example.elsp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.elsp.data.ElPr
import com.example.elsp.ui.theme.ElspTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val prices = listOf(
            ElPr("00:00", 0.10),
            ElPr("00:15", 0.12),
            ElPr("00:30", 0.08),
            ElPr("00:45", 0.15)
        )
        val minPr = prices.minByOrNull {it.pricePerKwh}
        val peakPr = prices.maxByOrNull {it.pricePerKwh}
        val averagePr = prices.map {it.pricePerKwh}.average()

        val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        setContent {
            ElspTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                    )
                    {
                        Text(today)
                        Text("Min: ${minPr?.pricePerKwh} €/kWh (${minPr?.time})")
                        Text("Peak: ${peakPr?.pricePerKwh} €/kWh (${peakPr?.time})")
                        Text("Average: $averagePr €/kWh")
                    }

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )
                    {
                        val minV = prices.minOf {it.pricePerKwh}
                        val peakV = prices.maxOf {it.pricePerKwh}
                        val range = if (peakV - minV == 0.0) 1.0 else peakV - minV

                        val points = prices.mapIndexed {index,price ->
                            val x = index.toFloat() / (prices.size - 1) * size.width

                            val y = size.height - ((price.pricePerKwh - minV) / range).toFloat() * size.height

                            Offset (x,y)
                        }

                        for (i in 0 until points.size - 1) {
                            drawLine(
                                color = Color.Black,
                                start = points[i],
                                end = points[i +1],
                                strokeWidth = 5f
                            )
                        }

                    }


                }
            }
        }
    }

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ElspTheme {
        Greeting("Android")
    }
}}