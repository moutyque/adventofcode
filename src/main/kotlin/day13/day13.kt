package day13

import java.io.File

typealias Level = Int

typealias Value = Int
fun main() {
    val iterator = File("./src/main/kotlin/day13/input").readLines().iterator()

    day131(iterator)
}
fun List<String>.buildLevel(): MutableList<Int> {
    var level = 0
    val levels = mutableListOf<Int>()
    for (c in this) {
        when (c) {
            "[" -> {
                level++
            }

            "]" -> {
                level--
            }
        }
        levels.add(level)
    }
    return levels
}

fun String.isNumeric(): Boolean {
    return this.toDoubleOrNull() != null
}

fun String.clean(): Pair<List<Value>, MutableList<Level>> =
    this.replace("[]", "[-1]")
        .split(",")
        .flatMap { it.split("""((?=\])|(?<=\[))""".toRegex()) }
        .run {
            mapIndexed { index, c -> if (c.isNumeric()) index else -1 }
                .filter { it > 0 }
                .let { indexes ->
                    Pair(
                        indexes.map { this[it].toInt() },
                        indexes.mapTo(mutableListOf()) { buildLevel()[it] })
                }
        }


fun MutableList<Int>.updateLevel(newLevel: Int, refLevel: Int, startIndex: Int) {
    var tmpIdx = startIndex
    while (tmpIdx < this.size && this[tmpIdx] == refLevel) {
        this[tmpIdx] = newLevel
        tmpIdx++
    }
}

fun day131(iterator: Iterator<String>) {
    var counter = 0
    var score = 0


    while (iterator.hasNext()) {
        counter++
        val (left, leftLevels) = iterator.next().clean()
        val (right, rightLevels) = iterator.next().clean()
        iterator.next()//Empty line
        var index = 0
        var currentResult = true
        while (index < left.size) {
            if (leftLevels[index] == rightLevels[index]) {
                if (left[index] < right[index]) {
                    currentResult = true
                    break
                } else if (left[index] > right[index]) {
                    currentResult = false
                    break
                } else {
                    index++
                    if (index >= right.size) {
                        currentResult = false
                        break
                    }
                }
            } else if (leftLevels[index] > rightLevels[index]) {
                rightLevels.updateLevel(leftLevels[index], rightLevels[index], index)
            } else if (leftLevels[index] < rightLevels[index]) {
                leftLevels.updateLevel(rightLevels[index], leftLevels[index], index)
            }
        }


        if (currentResult) score += counter
        println("$counter: $currentResult -> $score")
    }

    println(score)

}
