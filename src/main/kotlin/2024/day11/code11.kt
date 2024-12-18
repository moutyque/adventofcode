package `2024`.day11

import prepare
import java.io.File

const val day = 11

val prefix = "./src/main/kotlin/2024/day$day"

fun main() {
    File("$prefix/test").prepare().compute111().also { println(it) }.also { require(it == 55312) }
    File("${prefix}/input").prepare().compute111().let { println(it) }

}

fun Sequence<String>.compute111(): Int {
    val initialStones = this.flatMap {
        it.split(" ").filter { it.isNotBlank() }.map { it.toLong() }.map { Stone(it) }
    }.toMutableList()
    val updateStones = mutableListOf<Stone>()
    for (i in 1..25) {
        for (s in initialStones) {
            updateStones.addAll(s.blink())
        }
        initialStones.clear()
        initialStones.addAll(updateStones)
        updateStones.clear()
    }

    return initialStones.size
}

data class Stone(val value: Long) {
    fun blink() =
        when {
            this.value == 0L -> listOf(Stone(1))
            this.value.toString().length % 2 == 0 -> this.split()
            else -> listOf(Stone(this.value * 2024))
        }


    fun split(): List<Stone> = listOf(
        Stone(
            this.value.subLong(0, this.value.toString().length / 2)
        ),
        Stone(
            this.value.subLong(this.value.toString().length / 2, this.value.toString().length)
        )
    )
}

fun Long.subLong(from: Int = 0, to: Int = 0): Long =
    this.toString().toCharArray().toList().subList(from, to).joinToString("").toLong()
