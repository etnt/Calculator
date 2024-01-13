package se.kruskakli.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import java.util.Stack
import kotlin.math.sqrt


class CalculateEngine {
    //private var display = ""
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
            is CalculatorAction.Calculate -> performCalculation()
            is CalculatorAction.Delete -> performDeletion()
        }
    }


    private fun performDeletion() {

    }

    private fun performCalculation() {
        maybe_push_number()
        while (!operations.isEmpty() && numbers.size >= 2) {
            processOperation()
        }
        display = (numbers.peek().toString()).also { it }
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
                return
            }
            is CalculatorOperation.OneOverX -> {
                one_over_x()
                return
            }
            is CalculatorOperation.SquareRoot -> {
                square_root()
                return
            }

            // FIXME!
            is CalculatorOperation.SquareRoot -> op = ' '

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

        var did_process = false
        while (!operations.isEmpty() && hasPrecedence(operations.peek(), op) && numbers.size >= 2) {
            processOperation()
            did_process = true
        }

        if (did_process) {
            display = (numbers.peek().toString() + op).also { it }
        } else {
            display += op
        }
        operations.push(op)

    }

    private fun square_root() {
        maybe_push_number()
        if (!numbers.isEmpty()) {
            val n = numbers.pop()
            numbers.push( sqrt(n) )
            display = (numbers.peek().toString()).also { it }
        }
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

    private fun power_of_two() {
        maybe_push_number()
        if (!numbers.isEmpty()) {
            val n = numbers.pop()
            numbers.push(n * n)
            display = (numbers.peek().toString()).also { it }
        }
    }
    private fun one_over_x() {
        maybe_push_number()
        if (!numbers.isEmpty()) {
            val n = numbers.pop()
            numbers.push(1 / n)
            display = (numbers.peek().toString()).also { it }
        }
    }

    private fun doReset() {
        expression = ""
        display = ""
        isShifted = false
        numbers.clear()
        operations.clear()
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

        // If no operation is pushed, we need to clear the display.
        if (operations.isEmpty()) {
            display = expression
        } else {
            display += expression
        }
    }

    private fun enterNumber(digit: Int) {
        val char: Char = '0' + digit
        expression += char

        // If no operation is pushed, we need to clear the display.
        if (operations.isEmpty()) {
            display = expression
        } else {
            display += char
        }
    }

    private fun enterPi() {
        // Overwrite any number being entered and push PI to the stack.
        expression = "3.1416"
        display += expression
        maybe_push_number()
    }

    fun enterChar(c: Char) {
        expression += c
    }

    fun getExpr(): String {
        return display
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

    /*
    fun enterChar(c: Char) {
        when {
            c == '=' -> {
                val result = calculate(expression.value)
                expression.value = result.toString()
            }
            else -> {
                expression.value += c
            }
        }
    }

    fun reset() {
        expression.value = ""
        numbers.clear()
        operations.clear()
    }

    private fun calculate(expression: String): Double {
        if (!isBalanced(expression)) {
            throw IllegalArgumentException("Unbalanced parentheses in the expression.")
        }

        var numberBuffer = StringBuilder()

        for (c in expression) {
            when {
                c.isDigit() || c == '.' -> {
                    numberBuffer.append(c)
                }
                c == '(' -> {
                    operations.push(c)
                }
                c == ')' -> {
                    if (numberBuffer.isNotEmpty()) {
                        numbers.push(numberBuffer.toString().toDouble())
                        numberBuffer.clear()
                    }
                    while (!operations.isEmpty() && operations.peek() != '(') {
                        processOperation(numbers, operations)
                    }
                    if (!operations.isEmpty()) {
                        operations.pop()
                    }
                }
                c == ' ' || c == '+' || c == '-' || c == '*' || c == '/' -> {
                    if (numberBuffer.isNotEmpty()) {
                        numbers.push(numberBuffer.toString().toDouble())
                        numberBuffer.clear()
                    }

                    if (c != ' ') {
                        while (!operations.isEmpty() && hasPrecedence(c, operations.peek()) && numbers.size >= 2) {
                            processOperation(numbers, operations)
                        }
                        operations.push(c)
                    }
                }
                else -> throw IllegalArgumentException("Invalid character in expression: '$c'")
            }
        }

        if (numberBuffer.isNotEmpty()) {
            numbers.push(numberBuffer.toString().toDouble())
        }

        while (!operations.isEmpty()) {
            processOperation(numbers, operations)
        }

        return numbers.pop()
    }

    private fun processOperation(numbers: Stack<Double>, operations: Stack<Char>) {
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
    }

    private fun hasPrecedence(op1: Char, op2: Char): Boolean {
        if (op2 == '(' || op2 == ')')
            return false
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
            return false
        if (op1 == op2)
            return false
        return true
    }

    private fun isBalanced(expression: String): Boolean {
        val stack = Stack<Char>()

        for (char in expression) {
            when (char) {
                '(' -> stack.push(char)
                ')' -> {
                    if (stack.isEmpty()) {
                        return false
                    }
                    stack.pop()
                }
            }
        }

        return stack.isEmpty()
    }
    */

}