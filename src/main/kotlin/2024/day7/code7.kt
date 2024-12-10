package `2024`.day7

import prepare
import java.io.File

const val day = 7

val prefix = "./src/main/kotlin/2024/day$day"

fun main() {

    val actual = File("$prefix/test").prepare().compute71()
    println(actual)
    require(actual == 3749L)
    File("${prefix}/input").prepare().compute71().let { println(it) }
    val actual2 = File("${prefix}/test").readLines().asSequence().filter { it.isNotBlank() }.compute72()
    println(actual2)
    require(actual2 == 11387L)
    File("${prefix}/input").readLines().asSequence().compute72().let { println(it) }
    //140599577242124 -> too high
}



fun Sequence<String>.compute71(): Long = this.compute7(
    listOf(
        { a: Long, b: Long -> a * b },
        { a: Long, b: Long -> a + b }
    ))

fun Sequence<String>.compute72(): Long = this.compute7(
    listOf(
        { a: Long, b: Long -> a * b },
        { a: Long, b: Long -> "$a$b".toLong() },
        { a: Long, b: Long -> a + b }
    ))


fun Sequence<String>.compute7(operations: List<(a: Long, b: Long) -> Long>): Long =
    this.map {
        val input = it.split(":", " ").filter { it.isNotBlank() }.map { it.toLong() }
        if (findSolution(
                input[0], input, 2, input[1],
                operations
            )
        ) {
            input[0]
        } else {
            0
        }
    }.sum()

fun findSolution(
    target: Long,
    list: List<Long>,
    index: Int,
    currentValue: Long,
    operations: List<(a: Long, b: Long) -> Long>
): Boolean =
    when {
        currentValue == target && index == list.size -> true
        currentValue > target -> false
        index == list.size -> false
        else -> operations.map {
            findSolution(target, list, index + 1, it(currentValue, list[index]), operations)
        }.any { it }
    }






