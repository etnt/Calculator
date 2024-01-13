package se.kruskakli.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.kruskakli.calculator.ui.theme.LightGray

@Composable
fun CalculatorButton(
    symbol: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    style: TextStyle = TextStyle(fontSize = 36.sp),
    onClick: () -> Unit  // the way to send event that the Button was clicked
) {
    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(CircleShape)
            .background(color)
            .clickable { onClick() }
            .then(modifier)  // will run our local 'Modifier' first then the other 'modifier'
    ) {
        Text(
            text = symbol,
            style = style,
            //fontSize = 36.sp, // for more flexibility, send in a TextStyle
            // Alt. MaterialTheme.colors.onbackground()
            color = color
         )
    }
}

@Preview
@Composable
fun CalculatorButtonPreview () {
    CalculatorButton (
        symbol = "+",
        modifier = Modifier.padding(3.dp).background(Color.Yellow),
        onClick = {},
        color = Color.DarkGray
    )
}
