package se.kruskakli.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.kruskakli.calculator.ui.theme.LightGray
import se.kruskakli.calculator.ui.theme.MediumGray
import se.kruskakli.calculator.ui.theme.Orange

@Composable
fun Calculator(
    xstate: MutableState<CalculateEngine>,
    modifier: Modifier = Modifier,  // Note: optional with a default value
    buttonSpacing: Dp = 8.dp,
    xonAction: (CalculatorAction) -> Unit
) {
    Box(modifier = Modifier
        .background(MediumGray)
        .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),  // possible bc we are in a box!
            verticalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            Text(
                //text = state.number1 + (state.operation?.symbol ?: "") + state.number2,
                text = xstate.value.getTheDisplay(),
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                fontWeight = FontWeight.Light,
                fontSize = 50.sp,
                lineHeight = 60.sp,
                maxLines = 2,
                color = Color.White,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "AC",
                    modifier = Modifier
                        .background(LightGray)
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        xonAction(CalculatorAction.Clear)
                    }
                )
                CalculatorButton(
                    symbol = "Del",
                    modifier = Modifier
                        .background(LightGray)
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        xonAction(CalculatorAction.Delete)
                    }
                )
                CalculatorButton(
                    symbol = CalculatorMathSymbols.SHIFT,
                    color = Color.Blue,
                    style = TextStyle(
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .background(LightGray)
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        xonAction(CalculatorAction.Shift)
                    }
                )
                CalculatorButton(
                    symbol = if (xstate.value.isShifted) CalculatorMathSymbols.PI else CalculatorMathSymbols.DIVIDE,
                    style = if (xstate.value.isShifted) TextStyle(fontSize = 28.sp, fontStyle = FontStyle.Italic) else TextStyle(fontSize = 36.sp),
                    color = if (xstate.value.isShifted) Color.Blue else Color.White,
                    modifier = Modifier
                        .background(Orange)
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        if (xstate.value.isShifted) {
                            xonAction(CalculatorAction.Pi)
                        } else {
                            xonAction(CalculatorAction.Operation(CalculatorOperation.Divide))
                        }
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "7",
                    color = Color.White,
                    modifier = Modifier
                        .background(Color.DarkGray)
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        xonAction(CalculatorAction.Number(7))
                    }
                )
                CalculatorButton(
                    symbol = "8",
                    color = Color.White,
                    modifier = Modifier
                        .background(Color.DarkGray)
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        xonAction(CalculatorAction.Number(8))
                    }
                )
                CalculatorButton(
                    symbol = "9",
                    color = Color.White,
                    modifier = Modifier
                        .background(Color.DarkGray)
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        xonAction(CalculatorAction.Number(9))
                    }
                )
                CalculatorButton(
                    symbol = if (xstate.value.isShifted) CalculatorMathSymbols.ONE_OVER_X else CalculatorMathSymbols.MULTIPLY,
                    style = if (xstate.value.isShifted) TextStyle(fontSize = 28.sp, fontStyle = FontStyle.Italic) else TextStyle(fontSize = 36.sp),
                    color = if (xstate.value.isShifted) Color.Blue else Color.White,
                    modifier = Modifier
                        .background(Orange)
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        if (xstate.value.isShifted) {
                            xonAction(CalculatorAction.Operation(CalculatorOperation.OneOverX))
                        } else {
                            xonAction(CalculatorAction.Operation(CalculatorOperation.Multiply))
                        }
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "4",
                    color = Color.White,
                    modifier = Modifier
                        .background(Color.DarkGray)
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        xonAction(CalculatorAction.Number(4))
                    }
                )
                CalculatorButton(
                    symbol = "5",
                    color = Color.White,
                    modifier = Modifier
                        .background(Color.DarkGray)
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        xonAction(CalculatorAction.Number(5))
                    }
                )
                CalculatorButton(
                    symbol = "6",
                    color = Color.White,
                    modifier = Modifier
                        .background(Color.DarkGray)
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        xonAction(CalculatorAction.Number(6))
                    }
                )
                CalculatorButton(
                    symbol = if (xstate.value.isShifted) CalculatorMathSymbols.POWER_OF_TWO else CalculatorMathSymbols.MINUS,
                    style = if (xstate.value.isShifted) TextStyle(fontSize = 28.sp, fontStyle = FontStyle.Italic) else TextStyle(fontSize = 36.sp),
                    color = if (xstate.value.isShifted) Color.Blue else Color.White,
                    modifier = Modifier
                        .background(Orange)
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        if (xstate.value.isShifted) {
                            xonAction(CalculatorAction.Operation(CalculatorOperation.PowerOfTwo))
                        } else {
                            xonAction(CalculatorAction.Operation(CalculatorOperation.Subtract))
                        }
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "1",
                    color = Color.White,
                    modifier = Modifier
                        .background(Color.DarkGray)
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        xonAction(CalculatorAction.Number(1))
                    }
                )
                CalculatorButton(
                    symbol = "2",
                    color = Color.White,
                    modifier = Modifier
                        .background(Color.DarkGray)
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        xonAction(CalculatorAction.Number(2))
                    }
                )
                CalculatorButton(
                    symbol = "3",
                    color = Color.White,
                    modifier = Modifier
                        .background(Color.DarkGray)
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        xonAction(CalculatorAction.Number(3))
                    }
                )
                CalculatorButton(
                    symbol = if (xstate.value.isShifted) CalculatorMathSymbols.SQUARE_ROOT else CalculatorMathSymbols.PLUS,
                    style = if (xstate.value.isShifted) TextStyle(fontSize = 28.sp, fontStyle = FontStyle.Italic) else TextStyle(fontSize = 36.sp),
                    color = if (xstate.value.isShifted) Color.Blue else Color.White,
                    modifier = Modifier
                        .background(Orange)
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        if (xstate.value.isShifted) {
                            xonAction(CalculatorAction.Operation(CalculatorOperation.SquareRoot))
                        } else {
                            xonAction(CalculatorAction.Operation(CalculatorOperation.Add))
                        }
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "0",
                    modifier = Modifier
                        .background(LightGray)
                        .aspectRatio(2f)  // twice as wide as high!
                        .weight(2f),  // the other buttons has weight 1f, hence this button will occupy more space
                    onClick = {
                        xonAction(CalculatorAction.Number(0))
                    }
                )
                CalculatorButton(
                    symbol = ".",
                    modifier = Modifier
                        .background(LightGray)
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        xonAction(CalculatorAction.Decimal)
                    }
                )
                CalculatorButton(
                    symbol = CalculatorMathSymbols.EQUALS,
                    modifier = Modifier
                        .background(Orange)
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        xonAction(CalculatorAction.Calculate)
                    }
                )
            }
        }
    }
}

