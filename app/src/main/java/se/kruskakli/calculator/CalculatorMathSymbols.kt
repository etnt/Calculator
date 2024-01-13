package se.kruskakli.calculator

object CalculatorMathSymbols {
    const val PLUS = "\u002B"
    const val MINUS = "\u2212"
    const val MULTIPLY = "\u00D7"
    const val DIVIDE = "\u00F7"
    const val EQUALS = "\u003D"
    const val ONE_OVER_X = "1/x"
    const val SQUARE_ROOT = "\u221A" + "x"
    const val NTH_SQUARE_ROOT = "n \u221A" + "y" // n √y
    const val POWER_OF_TWO = "x\u00B2" // x²
    const val POWER_OF_THREE = "x\u00B3" // x³
    const val POWER_OF_N = "x\u207F" // xⁿ
    const val PI = "\u03C0"
    const val FACTORIAL = "\u0021"
    const val PERCENT = "\u0025"
    const val THETA = "\u03B8"
    const val PHI = "\u03C6"
    const val SHIFT = "\u21E7" // thinUpArrow="\u2191" , fatUpArrow="\u21E7"

    // Unicode does not have specific symbols for these operations.
    // However, you can use superscript and subscript numbers along
    // with the base letter to represent these operations.
    const val LOG_BASE_E = "log\u208E"  // Natural logarithm, base e
    const val LOG_BASE_10 = "log\u2081\u2080"  // Common logarithm, base 10
    const val Y_TO_THE_POWER_OF_X = "y\u02E3"  // Power of with arbitrary base
    const val E_TO_THE_POWER_OF_X = "e\u02E3"  // Euler's number to the power of x
}