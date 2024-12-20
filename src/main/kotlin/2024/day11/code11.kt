package `2024`.day11

import prepare
import java.io.File

const val day = 11

val prefix = "./src/main/kotlin/2024/day$day"

fun main() {
    File("$prefix/test").prepare().compute11().also { println(it) }.also { require(it == 55312L) }
    cache.clear()
    File("${prefix}/input").prepare().compute11().let { println(it) }
    cache.clear()
    File("${prefix}/input").prepare().compute11(75).let { println(it) }

}

fun Sequence<String>.compute11(rounds: Int = 25): Long {
    val stones = this.flatMap {
        it.split(" ").filter { it.isNotBlank() }.map { it.toLong() }.map { it }
    }
    return stones.sumOf { it.blink(rounds) }
}

val cache = mutableMapOf<Pair<Int, Long>, Long>()

fun Long.blink(maxRound: Int = 25, currentRound: Int = 0): Long =
    cache.getOrPut(Pair(currentRound, this)) {
        when {
            currentRound == maxRound -> 1
            this == 0L -> 1L.blink(maxRound, currentRound + 1)
            toString().length % 2 == 0 -> this.split().sumOf { it.blink(maxRound, currentRound + 1) }
            else -> (this * 2024).blink(maxRound, currentRound + 1)
        }
    }


private fun Long.split(): List<Long> = listOf(
    this.subLong(0, this.toString().length / 2),
    this.subLong(this.toString().length / 2, this.toString().length)

)

fun Long.subLong(from: Int = 0, to: Int = 0): Long =
    this.toString().toCharArray().toList().subList(from, to).joinToString("").toLong()
