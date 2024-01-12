package se.kruskakli.calculator

class CalculateEngine {
    private var expression = ""
    //private val numbers = Stack<Double>()
    //private val operations = Stack<Char>()

    fun enterChar(c: Char) {
        expression += c
    }

    fun getExpression() : String {
        return expression
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