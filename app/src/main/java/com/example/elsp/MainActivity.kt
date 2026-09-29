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
import androidx.compose.foundation.background
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
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration


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
        val minV = prices.minOf { it.pricePerKwh }
        val peakV = prices.maxOf { it.pricePerKwh }
        val lowerMid = (averagePr + minV)/ 2
        val upperMid = (peakV + averagePr) / 2

        val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        setContent {
            ElspTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    //COLOR FONDO
                    containerColor = Color(20, 20, 60)) { innerPadding ->
                    CompositionLocalProvider(LocalContentColor provides Color.White){
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .padding(16.dp)
                        )
                        {
                            //DATE TODAY
                            Text(
                                text = today,
                                color = Color.White,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                                )
                            //CAMBIAR DIVISAS SEGÚN PAÍS SELECCIONADO
                            Spacer(modifier = Modifier.height(15.dp))

                            //MIN
                            Row {
                                Text("Min: ")

                                Text(
                                    text = "${"%.3f".format(minPr?.pricePerKwh)} €/kWh",
                                    color = Color.Green
                                )

                                 Text("(${minPr?.time})")
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            //MAX
                            Row{
                                Text("Peak: ")

                                Text(
                                    text = "${"%.3f".format(peakPr?.pricePerKwh)} €/kWh",
                                    color = Color.Red
                                )

                                Text("(${peakPr?.time})")
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            //AVERAGE
                            Row {
                                Text("Average: ")

                                Text(
                                    text = "${"%.3f".format(averagePr)} €/kWh",
                                    color = Color.Yellow
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            //MOSTRAR PRECIO DEL RANGO EN {HORA ACTUAL}
                            //CODIGO CODIGO CODIGO CODIGO CODIGO CODIGO CODIGO CODIGO

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .background(Color.Gray)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(50.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("%.3f".format(peakV))
                                    Text("%.3f".format(upperMid))
                                    Text("%.3f".format(averagePr))
                                    Text("%.3f".format(lowerMid))
                                    Text("%.3f".format(minV))
                                }

                                Canvas(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(250.dp)
                                ) {

                                    val range = if (peakV - minV == 0.0) 1.0 else peakV - minV
                                    //intermedios
                                    val upperMidY = size.height - ((upperMid - minV) / range).toFloat() * size.height
                                    val averageY = size.height - ((averagePr - minV) / range).toFloat() * size.height
                                    val lowerMidY = size.height - ((lowerMid - minV) / range).toFloat() * size.height

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

                                    //MARCAR {HORA ACTUAL} CON UN PUNTO EN LA lÌNEA
                                    //CODIGO CODIGO CODIGO CODIGO CODIGO CODIGO

                                    drawLine(
                                        color = Color.White.copy(alpha = 0.3f),
                                        start = Offset(0f, upperMidY),
                                        end = Offset(size.width, upperMidY),
                                        strokeWidth = 1f
                                    )

                                    drawLine(
                                        color = Color.White.copy(alpha = 0.3f),
                                        start = Offset(0f, averageY),
                                        end = Offset(size.width, averageY),
                                        strokeWidth = 1f
                                    )

                                    drawLine(
                                        color = Color.White.copy(alpha = 0.3f),
                                        start = Offset(0f, lowerMidY),
                                        end = Offset(size.width, lowerMidY),
                                        strokeWidth = 1f
                                    )



                                    //LINE
                                    for (i in 0 until points.size - 1) {
                                        drawLine(
                                            color = Color.Red,
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