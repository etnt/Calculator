package se.kruskakli.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import java.util.Stack


class CalculateEngine {
    //private var display = ""
    var display by mutableStateOf("")
        private set
    private var expression = ""
    private var numbers = Stack<Double>()
    private var operations = Stack<Char>()

    fun onAction(action: CalculatorAction) {
        when(action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Pi -> enterPi()
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

    }

    private fun enterOperation(operation: CalculatorOperation) {
        val op : Char

        // Push the incoming operation on to the operations stack.
        when(operation) {
            is CalculatorOperation.Add -> op = '+'
            is CalculatorOperation.Subtract -> op = '-'
            is CalculatorOperation.Multiply -> op = '*'
            is CalculatorOperation.Divide -> op = '/'
        }

        // If we have collected a number, turn it into a Double
        // and push it on to the numbers stack.
        if (!expression.isEmpty()) {
            val number = expression.toDouble()
            numbers.push(number)
            expression = ""
        }

        while (!operations.isEmpty() && hasPrecedence(operations.peek(), op) && numbers.size >= 2) {
            processOperation()
        }

        display = (numbers.peek().toString() + op).also { it }
        operations.push(op)

    }

    private fun doReset() {

    }

    private fun enterDecimal() {

    }

    private fun enterNumber(digit: Int) {
        val char: Char = '0' + digit
        expression += char
        display += expression
    }

    private fun enterPi() {
        expression += "3.1416"
        display += expression
    }

    fun enterChar(c: Char) {
        expression += c
    }

    fun getExpr() : String {
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
            return true
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