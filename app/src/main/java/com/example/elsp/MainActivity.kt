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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Box


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

                        Spacer(modifier = Modifier.height(15.dp))

                        Text("Min: ${minPr?.pricePerKwh} €/kWh (${minPr?.time})")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Peak: ${peakPr?.pricePerKwh} €/kWh (${peakPr?.time})")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Average: $averagePr €/kWh")

                        Spacer(modifier = Modifier.height(24.dp))



                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(50.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${peakPr?.pricePerKwh}")
                                Text("%.2f".format(averagePr))
                                Text("${minPr?.pricePerKwh}")
                            }

                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                            ) {
                                val minV = prices.minOf { it.pricePerKwh }
                                val peakV = prices.maxOf { it.pricePerKwh }
                                val range = if (peakV - minV == 0.0) 1.0 else peakV - minV

                                val points = prices.mapIndexed { index, price ->
                                    val x = index.toFloat() / (prices.size - 1) * size.width

                                    val y =
                                        size.height - ((price.pricePerKwh - minV) / range).toFloat() * size.height

                                    Offset(x, y)
                                }

                                //AXIS
                                drawLine(
                                    color = Color.Gray,
                                    start = Offset(0f, 0f),
                                    end = Offset(0f, size.height),
                                    strokeWidth = 2f
                                )
                                //LINE
                                for (i in 0 until points.size - 1) {
                                    drawLine(
                                        color = Color.Black,
                                        start = points[i],
                                        end = points[i + 1],
                                        strokeWidth = 5f
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("00:00")
                            Text("00:15")
                            Text("00:30")
                            Text("00:45")
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