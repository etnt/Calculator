package se.kruskakli.calculator

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Stack

data class CalculatorState(
    val displayItems: MutableList<String> = mutableListOf(),
    var display: MutableState<String> = mutableStateOf(""),
    var isShifted: MutableState<Boolean> = mutableStateOf(false),
    var expression: String = "",
    var numbers: Stack<Double> = Stack(),
    var operations: Stack<Char> = Stack()
)
