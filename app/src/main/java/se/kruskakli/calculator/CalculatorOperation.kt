package se.kruskakli.calculator

sealed class CalculatorOperation(val symbol: String) {
    object Add: CalculatorOperation("+")
    object Subtract: CalculatorOperation("-")
    object Multiply: CalculatorOperation("*")
    object Divide: CalculatorOperation("/")
    object SquareRoot: CalculatorOperation("√")
    object PowerOf: CalculatorOperation("^")
    object Factorial: CalculatorOperation("!")
    object Percent: CalculatorOperation("%")
    object LogBaseE: CalculatorOperation("ln")
    object LogBase10: CalculatorOperation("log")
    object EToThePowerOfX: CalculatorOperation("e^x")
    object Sin: CalculatorOperation("sin")
    object Cos: CalculatorOperation("cos")
    object Tan: CalculatorOperation("tan")
    object Csc: CalculatorOperation("csc")
    object Sec: CalculatorOperation("sec")
    object Cot: CalculatorOperation("cot")
}