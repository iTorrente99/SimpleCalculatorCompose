package com.example.calculadoracompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadoracompose.ui.theme.CalculadoraComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculadoraComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    CalculatorDisplay()
                }
            }
        }
    }
}

@Composable
fun ShowTitle(title:String) {
    Text(title)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorDisplay() {
    var expressionToCalculate = remember { mutableStateOf("") }
    var calculationResult = remember { mutableStateOf("") }
    val operators = "+-*/"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.End)
                .weight(0.15f),
            textStyle = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold),
            value = expressionToCalculate.value,
            onValueChange = { newText ->
                if (newText.all { it.isDigit() || it in operators }) {
                    expressionToCalculate.value = newText
                }
            },
            label = { Text("Expression") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colorScheme.primary
            )
        )
        Surface(
            modifier = Modifier
                .align(Alignment.End)
                .weight(0.12f)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Text(
                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                text = calculationResult.value,
                fontSize = 48.sp,
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Box(
            modifier = Modifier.weight(0.7f)
        ) {
            CalculatorNumpad { buttonValue ->
                when (buttonValue) {
                    "=" -> {
                        calculationResult.value = calculateResult(expressionToCalculate.value)
                        expressionToCalculate.value = ""
                    }
                    else -> {
                        expressionToCalculate.value += buttonValue
                    }
                }
            }
        }
    }
}

fun calculateResult(expression: String): String {
    if (expression.isBlank()) return ""

    val operator = expression.find { it in "+-*/" } ?: return expression
    val parts = expression.split(operator).map { it.toDoubleOrNull() ?: return "Invalid Input" }

    if (parts.size != 2) return "Invalid Format"

    val (a, b) = parts
    return when (operator) {
        '+' -> (a + b).toString()
        '-' -> (a - b).toString()
        '*' -> (a * b).toString()
        '/' -> if (b != 0.0) (a / b).toString() else "Cannot Divide by Zero"
        else -> "Unknown Operator"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorNumpad(onCalculateClicked: (String) -> Unit) {
    val buttonWeight = 0.25f

    Column {
        for (j in 7 downTo 1 step 3) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                for (i in 0..2) {
                    val number = (j + i).toString()
                    Card(
                        modifier = Modifier
                            .weight(buttonWeight)
                            .aspectRatio(1f)
                            .padding(3.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                        onClick = { onCalculateClicked(number) }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                text = (j + i).toString()
                            )
                        }
                    }
                }
                val operator = if (j == 7) "+" else if (j == 4) "-" else if (j == 1) "*" else "/"
                Card(
                    modifier = Modifier
                        .weight(buttonWeight)
                        .aspectRatio(1f)
                        .padding(3.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                    onClick = { onCalculateClicked(operator) }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            text = if (j==7) "+" else if (j==4) "-" else if (j==1) "*" else "/"
                        )
                    }
                }
            }
        }
        Row {
            Card(
                modifier = Modifier
                    .weight(buttonWeight)
                    .aspectRatio(1f)
                    .padding(3.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                onClick = { onCalculateClicked("0") }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        text = "0"
                    )
                }
            }
            Card(
                modifier = Modifier
                    .weight(buttonWeight * 2)
                    .aspectRatio(2f)
                    .padding(3.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                onClick = { onCalculateClicked("=") }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        text = "="
                    )
                }
            }
            Card(
                modifier = Modifier
                    .weight(buttonWeight)
                    .aspectRatio(1f)
                    .padding(3.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                onClick = { onCalculateClicked("/") }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        text = "/"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    CalculadoraComposeTheme {
        ShowTitle("Calculator")
    }
}