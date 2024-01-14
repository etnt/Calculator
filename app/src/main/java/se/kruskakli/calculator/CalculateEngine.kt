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
import java.util.Stack
import kotlin.math.sqrt


class CalculateEngine {
    private val displayItems = mutableListOf<String>()
    var display by mutableStateOf("")
        private set
    var isShifted: Boolean by mutableStateOf(false)
        private set
    private var expression = ""
    private var numbers = Stack<Double>()
    private var operations = Stack<Char>()

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Pi -> enterPi()
            is CalculatorAction.Shift -> isShifted = !isShifted  // toggle!
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
        return display
    }

    private fun enterNumber(digit: Int) {
        val char: Char = '0' + digit
        expression += char

        if (displayItems.isNotEmpty() && displayItems.last().toDoubleOrNull() != null) {
            // If the last item is a number, append the digit to it
            val lastIndex = displayItems.lastIndex
            // Note: the [] operator is read-only. To modify an element at a
            // specific index, we need to use the set function.
            val newItem = displayItems[lastIndex] + char
            displayItems.set(lastIndex, newItem)
        } else {
            // If the last item is not a number, add a new item
            displayItems.add(char.toString())
        }

        // Update the display string
        display = displayItems.joinToString("") 
    }

    private fun doReset() {
        expression = ""
        numbers.clear()
        operations.clear()
        displayItems.clear()
        display = ""
    }

    // Check if the number is an integer. If it is, it's
    // converted to an integer before being converted to
    // a string. If it's not an integer, it's formatted as
    // a floating-point number with <X> decimal places.
    private fun updateDisplay(fmtStr: String = "%.2f") {
        val result = numbers.peek()
        displayItems[displayItems.lastIndex] = if (result % 1 == 0.0) {
            result.toInt().toString()
        } else {
            String.format(fmtStr, result)
        }
        display = displayItems.joinToString("")
    }

    // To handle the numbers and operations stacks when performing a deletion,
    // we need to consider what the last item in displayItems represents.
    // If it's a number, we should pop the last number from the numbers stack.
    // If it's an operation, you should pop the last operation from the operations stack.
    private fun performDeletion() {
        if (displayItems.isNotEmpty()) {
            val lastItem = displayItems.last()
            if (lastItem.length > 1) {
                // If the last item has more than one character, remove the last character
                displayItems[displayItems.lastIndex] = lastItem.substring(0, lastItem.length - 1)
            } else {
                // If the last item has only one character, remove the item
                displayItems.removeAt(displayItems.lastIndex)
                // If the last item was a number, pop the last number from the numbers stack
                if (lastItem.toDoubleOrNull() != null) {
                    if (numbers.isNotEmpty()) {
                        numbers.pop()
                    }
                } else {
                    // If the last item was an operation, pop the last operation from the operations stack
                    if (operations.isNotEmpty()) {
                        operations.pop()
                    }
                }
            }
            // Update the display string
            display = displayItems.joinToString("")
        }
    }

    private fun performCalculation() {
        maybe_push_number()
        while (!operations.isEmpty() && numbers.size >= 2) {
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

        while (!operations.isEmpty() && hasPrecedence(operations.peek(), op) && numbers.size >= 2) {
            processOperation()
        }

        operations.push(op)
        displayItems.add(op.toString())
        display = displayItems.joinToString("")
    }

    private fun square_root() {
        maybe_push_number()
        if (!numbers.isEmpty()) {
            val n = numbers.pop()
            val sqrtN = sqrt(n)
            numbers.push(sqrtN)
            updateLastDisplayItem(sqrtN)
        }
    }

    private fun power_of_two() {
        maybe_push_number()
        if (!numbers.isEmpty()) {
            val n = numbers.pop()
            val powN = n * n
            numbers.push(powN)
            updateLastDisplayItem(powN)
        }
    }
    private fun one_over_x() {
        maybe_push_number()
        if (!numbers.isEmpty()) {
            val n = numbers.pop()
            val oneOverN = 1 / n
            numbers.push(oneOverN)
            updateLastDisplayItem(oneOverN)
        }
    }

    private fun updateLastDisplayItem(value: Double) {
        if (displayItems.isNotEmpty()) {
            displayItems[displayItems.lastIndex] = if (value % 1 == 0.0) {
                value.toInt().toString()
            } else {
                String.format("%.2f", value)
            }
        }   
        display = displayItems.joinToString("")
    }

    private fun maybe_push_number() {
        // If we have collected a number, turn it into a Double
        // and push it on to the numbers stack.
        if (!expression.isEmpty()) {
            val number = expression.toDouble()
            numbers.push(number)
            expression = ""
        }
    }

    private fun enterDecimal() {
        val char: Char = '.'

        // If no previous number has been entered then
        // prepend Zero in front of the decimal.
        if (expression.isEmpty()) {
            expression += '0'
            expression += char
        } else {
            expression += char
        }

        if (displayItems.isNotEmpty() && displayItems.last().toDoubleOrNull() != null) {
            // If the last item is a number, append the decimal point to it
            val lastIndex = displayItems.lastIndex
            displayItems.set(lastIndex, expression)
        } else {
            // If the last item is not a number, add a new item
            displayItems.add(expression)
        }
        display = displayItems.joinToString("")
    }

    private fun enterPi() {
        val pi = 3.1416

        // If the last item in displayItems is a number, remove it
        if (displayItems.isNotEmpty() && displayItems.last().toDoubleOrNull() != null) {
            displayItems.removeAt(displayItems.size - 1)
            if (numbers.isNotEmpty()) {
                numbers.pop()
            }
        }
        
        expression = ""
        numbers.push(pi)
        displayItems.add(pi.toString())
        display = displayItems.joinToString("")
    }

    private fun processOperation() {
        val operator = operations.pop()
        val right = numbers.pop()
        val left = numbers.pop()

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
        numbers.push(result)

        // Remove the last two numbers and the operation from displayItems
        if (displayItems.size >= 3) {
            displayItems.removeAt(displayItems.lastIndex)
            displayItems.removeAt(displayItems.lastIndex)
            displayItems.removeAt(displayItems.lastIndex)
        }

        // Add the result to displayItems
        displayItems.add(result.toString()) 

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