package day1

import java.io.File

val regexPattern1 = "[0-9]".toRegex()
val regexPattern2 = "[0-9]|one|two|three|four|five|six|seven|eight|nine".toRegex()
val wordToDigit = mapOf(
    "one" to "1", "two" to "2", "three" to "3", "four" to "4", "five" to "5",
    "six" to "6", "seven" to "7", "eight" to "8", "nine" to "9"
)


fun main() {
    File("./src/main/kotlin/day1/test1")
        .readLines()
        .asSequence()
        .compute(regexPattern1) { it }.also { check(it == 142) }
    File("./src/main/kotlin/day1/test2")
        .readLines()
        .asSequence()
        .compute(regexPattern2) { wordToDigit[it] ?: it }.also { check(it == 281) }
    File("./src/main/kotlin/day1/input1")
        .readLines()
        .asSequence()
        .compute(regexPattern1) { it }.also { println(it) }
    File("./src/main/kotlin/day1/input2")
        .readLines()
        .asSequence()
        .compute(regexPattern2) { wordToDigit[it] ?: it }.also { println(it) }

}

fun Sequence<String>.compute(regex: Regex, block: (s: String) -> String) = run {

    asSequence().map {
        val matches = regex.findAll(it).map { match ->
            block(match.value)
        }.toList()

        val firstMatch = matches.firstOrNull() ?: ""
        val lastMatch = matches.lastOrNull() ?: ""
        "${firstMatch}${lastMatch}".toInt()
    }.sum()

}

fun Sequence<String>.compute2() = run {


    val regexPattern = "[0-9]|one|two|three|four|five|six|seven|eight|nine".toRegex()

    val calibrationValues = this.map { line ->
        regexPattern
            .findAll(line)
            .map { match ->
                wordToDigit[match.value] ?: match.value
            }.toList().let { matches ->
                val firstMatch = matches.firstOrNull() ?: ""
                val lastMatch = matches.lastOrNull() ?: ""
                "$firstMatch$lastMatch".toInt()
            }
    }

    val sum = calibrationValues.sum()
    println("Sum of all calibration values: $sum")
    sum
}
