package day13

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.IntNode
import java.io.File
import java.lang.Integer.min


typealias Level = Int

typealias Value = Int

fun main() {
    val iterator = File("./src/main/kotlin/day13/input").readLines().iterator()
    day132(iterator)
}

fun day132(iterator: Iterator<String>) {
    val dp1 = listOf(listOf(2))
    val dp2 = listOf(listOf(6))

    println(iterator.asSequence()
        .map { ObjectMapper().readTree(it).convert() }
        .filter { it != -1 }
        .sortedWith { o1, o2 -> compare(o1!!, o2!!).value }
        .mapIndexed { index, any -> if (any == dp1 || any == dp2) index + 1 else 0 }
        .filter { it != 0 }
        .fold(1) { acc: Int, i: Int ->
            acc * i
        })
}

fun day131(iterator: Iterator<String>) {
    val mapper = ObjectMapper()
    //JSON file to Java object
    var score = 0
    var idx = 0
    while (iterator.hasNext()) {
        val packet1 = mapper.readTree(iterator.next()).convert()
        val packet2 = mapper.readTree(iterator.next()).convert()
        iterator.next()
        idx++
        when (compare(packet1, packet2)) {
            Result.OK -> {
                score += idx
            }
            else -> {}
        }
    }
    println(score)
}

enum class Result(val value: Int) {
    OK(-1),
    KO(1),
    TIE(0)
}

fun JsonNode.convert(): Any =
    when (this) {
        is IntNode -> this.intValue()
        is ArrayNode -> this.map { it.convert() }.toList()
        else -> -1
    }

fun compare(left: Any, right: Any): Result =
    when {
        (left is Int && right is Int) -> Result.values().first { it.value == (left.compareTo(right)) }
        (left is Int && right is List<*>) -> compare(listOf(left), right)
        (left is List<*> && right is Int) -> compare(left, listOf(right))
        (left is List<*> && right is List<*>) -> {
            min(left.size, right.size).let {
                left.asSequence()
                    .filterIndexed { index, _ -> index < it }
                    .mapIndexed { index, any -> compare(any!!, right[index]!!) }
                    .firstOrNull { it != Result.TIE } ?: run {
                    when (left.size.compareTo(right.size)) {
                        -1 -> Result.OK
                        1 -> Result.KO
                        else -> Result.TIE
                    }
                }
            }

        }
        else -> Result.TIE
    }

