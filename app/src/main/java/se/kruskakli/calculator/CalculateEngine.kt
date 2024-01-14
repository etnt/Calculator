/*
 * MIT License
 * 
 * Copyright (c) [2024] [Torbjörn Törnkvist - kruskakli@gmail.com]
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package se.kruskakli.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.util.Stack
import kotlin.math.sqrt


class CalculateEngine : ViewModel() {

    var state by mutableStateOf(CalculatorState())

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Pi -> enterPi()
            is CalculatorAction.Shift -> state.isShifted.value = !state.isShifted.value  // toggle!
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Clear -> doReset()
            is CalculatorAction.Operation -> enterOperation(action.operation)
            is CalculatorAction.Calculate -> {
                performCalculation()
                updateDisplay("%.5f")
                return
            }
            is CalculatorAction.Delete -> performDeletion()
        }
    }

    fun getTheDisplay() : String {
        return state.display.value
    }

    private fun enterNumber(digit: Int) {
        val char: Char = '0' + digit
        state.expression = state.expression.plus(char)

        if (state.displayItems.isNotEmpty() && state.displayItems.last().toDoubleOrNull() != null) {
            // If the last item is a number, append the digit to it
            val lastIndex = state.displayItems.lastIndex
            val newItem = state.displayItems[lastIndex].plus(char)
            state.displayItems[lastIndex] = newItem
        } else {
            // If the last item is not a number, add a new item
            state.displayItems.add(char.toString())
        }

        // Update the display string
        state.display.value = state.displayItems.joinToString("")
    }

    private fun doReset() {
        state.expression = ""
        state.numbers.clear()
        state.operations.clear()
        state.displayItems.clear()
        state.display.value = ""
    }

    // Check if the number is an integer. If it is, it's
    // converted to an integer before being converted to
    // a string. If it's not an integer, it's formatted as
    // a floating-point number with <X> decimal places.
    private fun updateDisplay(fmtStr: String = "%.2f") {
        val result = state.numbers.peek()
        state.displayItems[state.displayItems.lastIndex] = if (result % 1 == 0.0) {
            result.toInt().toString()
        } else {
            String.format(fmtStr, result)
        }
        state.display.value = state.displayItems.joinToString("")
    }

    // To handle the numbers and operations stacks when performing a deletion,
    // we need to consider what the last item in displayItems represents.
    // If it's a number, we should pop the last number from the numbers stack.
    // If it's an operation, you should pop the last operation from the operations stack.
    private fun performDeletion() {
        if (state.displayItems.isNotEmpty()) {
            val lastItem = state.displayItems.last()
            if (lastItem.length > 1) {
                // If the last item has more than one character, remove the last character
                state.displayItems[state.displayItems.lastIndex] = lastItem.substring(0, lastItem.length - 1)
            } else {
                // If the last item has only one character, remove the item
                state.displayItems.removeAt(state.displayItems.lastIndex)
                // If the last item was a number, pop the last number from the numbers stack
                if (lastItem.toDoubleOrNull() != null) {
                    if (state.numbers.isNotEmpty()) {
                        state.numbers.pop()
                    }
                } else {
                    // If the last item was an operation, pop the last operation from the operations stack
                    if (state.operations.isNotEmpty()) {
                        state.operations.pop()
                    }
                }
            }
            // Update the display string
            state.display.value = state.displayItems.joinToString("")
        }
    }

    private fun performCalculation() {
        maybe_push_number()
        while (!state.operations.isEmpty() && state.numbers.size >= 2) {
            processOperation()
        }
    }

    private fun enterOperation(operation: CalculatorOperation) {
        val op: Char

        when (operation) {
            is CalculatorOperation.Add -> op = '+'
            is CalculatorOperation.Subtract -> op = '-'
            is CalculatorOperation.Multiply -> op = '*'
            is CalculatorOperation.Divide -> op = '/'
            is CalculatorOperation.PowerOfTwo -> {
                power_of_two()
                updateDisplay()
                return
            }
            is CalculatorOperation.OneOverX -> {
                one_over_x()
                updateDisplay()
                return
            }
            is CalculatorOperation.SquareRoot -> {
                square_root()
                updateDisplay()
                return
            }

            // FIXME!
            is CalculatorOperation.Factorial -> op = ' '
            is CalculatorOperation.Percent -> op = ' '
            is CalculatorOperation.LogBaseE -> op = ' '
            is CalculatorOperation.LogBase10 -> op = ' '
            is CalculatorOperation.EToThePowerOfX -> op = ' '
            is CalculatorOperation.Sin -> op = ' '
            is CalculatorOperation.Cos -> op = ' '
            is CalculatorOperation.Tan -> op = ' '
            is CalculatorOperation.Csc -> op = ' '
            is CalculatorOperation.Sec -> op = ' '
            is CalculatorOperation.Cot -> op = ' '
        }

        maybe_push_number()

        while (!state.operations.isEmpty() && hasPrecedence(state.operations.peek(), op) && state.numbers.size >= 2) {
            processOperation()
        }

        state.operations.push(op)
        state.displayItems.add(op.toString())
        state.display.value = state.displayItems.joinToString("")
    }

    private fun square_root() {
        maybe_push_number()
        if (!state.numbers.isEmpty()) {
            val n = state.numbers.pop()
            val sqrtN = sqrt(n)
            state.numbers.push(sqrtN)
            updateLastDisplayItem(sqrtN)
        }
    }

    private fun power_of_two() {
        maybe_push_number()
        if (!state.numbers.isEmpty()) {
            val n = state.numbers.pop()
            val powN = n * n
            state.numbers.push(powN)
            updateLastDisplayItem(powN)
        }
    }
    
    private fun one_over_x() {
        maybe_push_number()
        if (!state.numbers.isEmpty()) {
            val n = state.numbers.pop()
            if (n.toDouble() != 0.0) {
                val oneOverN = 1.0 / n.toDouble()
                state.numbers.push(oneOverN)
                updateLastDisplayItem(oneOverN)
            } else {
                throw ArithmeticException("Division by zero")
            }
        }
    }

    private fun updateLastDisplayItem(value: Double) {
        if (state.displayItems.isNotEmpty()) {
            state.displayItems[state.displayItems.lastIndex] = if (value % 1 == 0.0) {
                value.toInt().toString()
            } else {
                String.format("%.2f", value)
            }
        }   
        state.display.value = state.displayItems.joinToString("")
    }

    private fun maybe_push_number() {
        // If we have collected a number, turn it into a Double
        // and push it on to the numbers stack.
        if (!state.expression.isEmpty()) {
            val number = state.expression.toDouble()
            state.numbers.push(number)
            state.expression = ""
        }
    }

    private fun enterDecimal() {
        val char: Char = '.'

        // If no previous number has been entered then
        // prepend Zero in front of the decimal.
        if (state.expression.isEmpty()) {
            state.expression = state.expression.plus('0')
            state.expression = state.expression.plus(char)
        } else {
            state.expression = state.expression.plus(char)
        }

        if (state.displayItems.isNotEmpty() && state.displayItems.last().toDoubleOrNull() != null) {
            // If the last item is a number, append the decimal point to it
            val lastIndex = state.displayItems.lastIndex
            state.displayItems.set(lastIndex, state.expression)
        } else {
            // If the last item is not a number, add a new item
            state.displayItems.add(state.expression)
        }
        state.display.value = state.displayItems.joinToString("")
    }

    private fun enterPi() {
        val pi = 3.1416

        // If the last item in displayItems is a number, remove it
        if (state.displayItems.isNotEmpty() && state.displayItems.last().toDoubleOrNull() != null) {
            state.displayItems.removeAt(state.displayItems.size - 1)
            if (state.numbers.isNotEmpty()) {
                state.numbers.pop()
            }
        }

        state.expression = ""
        state.numbers.push(pi)
        state.displayItems.add(pi.toString())
        state.display.value = state.displayItems.joinToString("")
    }

    private fun processOperation() {
        val operator = state.operations.pop()
        val right = state.numbers.pop()
        val left = state.numbers.pop()

        val result = when (operator) {
            '+' -> left + right
            '-' -> left - right
            '*' -> left * right
            '/' -> {
                if (right == 0.0) throw ArithmeticException("Division by zero")
                left / right
            }

            else -> throw IllegalArgumentException("Invalid operator: '$operator'")
        }
        state.numbers.push(result)

        // Remove the last two numbers and the operation from displayItems
        if (state.displayItems.size >= 3) {
            state.displayItems.removeAt(state.displayItems.lastIndex)
            state.displayItems.removeAt(state.displayItems.lastIndex)
            state.displayItems.removeAt(state.displayItems.lastIndex)
        }

        // Add the result to displayItems
        state.displayItems.add(result.toString())

        updateLastDisplayItem(result)

    }

    private fun hasPrecedence(op1: Char, op2: Char): Boolean {
        if (op2 == '(' || op2 == ')')
            return false
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
            return true
        if (op1 == op2)
            return false
        return false
    }


}

