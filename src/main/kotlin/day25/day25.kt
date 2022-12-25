package day25

import utilities.readStringFile
import java.math.BigInteger

fun main() {

    println(sumSnafu("src/main/kotlin/day25/test.txt"))
    println(sumSnafu("src/main/kotlin/day25/input.txt"))
    //(3L..30L).forEach { println("$it -> ${decimalToSnafu(BigInteger.valueOf(it))}")}
}

fun sumSnafu(fileName: String): String {

    val input = readStringFile(fileName)

    var sumOfFuel: BigInteger = BigInteger.ZERO

    for (line in input) {
        sumOfFuel += snafuToDecimal(line)
    }
    println(sumOfFuel)
    return decimalToSnafu(sumOfFuel)
}

fun snafuToDecimal(snafu: String): BigInteger {
    var value: BigInteger = BigInteger.ZERO
    for ((i, char) in snafu.trim().toCharArray().reversed().withIndex()) {
        when (char) {
            '1' -> value += BigInteger.valueOf(5).pow(i)
            '2' -> value += BigInteger.TWO * BigInteger.valueOf(5).pow(i)
            '-' -> value -= BigInteger.valueOf(5).pow(i)
            '=' -> value -= BigInteger.TWO * BigInteger.valueOf(5).pow(i)
        }
    }
    return value
}

fun decimalToSnafu(decimal: BigInteger): String {

    var result = "0".repeat(4) + decimal.toString(5)
    for (i in result.length-1 downTo 0) {
        if (result[i] == '3') {
            result = result.replaceRange(i,i+1,"=")
            result = result.replaceRange(i-1, i, (result[i-1] + 1).toString())
        }
        else if (result[i] == '4') {
            result = result.replaceRange(i,i+1,"-")
            result = result.replaceRange(i-1, i, (result[i-1] + 1).toString())
        }
        else if (result[i] == '5') {
            result = result.replaceRange(i,i+1,"0")
            result = result.replaceRange(i-1, i, (result[i-1] + 1).toString())
        }
    }

    //println(" -> $result")
    return result.trimStart('0')
}
