package day10

import java.io.File

fun main() {
    val iterator = File("./src/main/kotlin/day10/input").readLines().iterator()

    day101(iterator)
    day102(iterator)
}

fun day102(iterator: Iterator<String>) {
    var X = 1
    var cycle = 0
    var position = 1
    val sb = StringBuilder()
    fun updateCycle() {

        if (position == cycle || position + 1 == cycle || position - 1 == cycle) {
            sb.append("#")
        } else {
            sb.append(".")
        }
        cycle++
        cycle %= 40
    }
    while (iterator.hasNext()) {
        iterator.next().apply {
            when {
                startsWith("noop") -> {
                    updateCycle()
                }

                startsWith("addx") -> {
                    updateCycle()
                    updateCycle()
                    position += removePrefix("addx ").toInt()
                }
            }
        }

    }
    sb.forEachIndexed { index, c -> if (index % 40 == 0) print("\n$c") else print(c) }
}

fun day101(iterator: Iterator<String>) {
    var X = 1
    var cycle = 0
    var score = 0
    fun Int.updateScore(strength: Int) = if (((this - 20) % 40) == 0) {
        this * strength
    } else 0
    while (iterator.hasNext()) {
        iterator.next().apply {
            when {
                startsWith("noop") -> {
                    cycle++
                    score += cycle.updateScore(X)
                }

                startsWith("addx") -> {
                    cycle++
                    score += cycle.updateScore(X)
                    cycle++
                    score += cycle.updateScore(X)
                    X += removePrefix("addx ").toInt()
                }
            }
        }

    }
    println(score)
}
