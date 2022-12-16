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
    day131(iterator)
    //day131(iterator)
}
//TODO: error at 101
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
        var s = idx.toString()
        when (compare(packet1, packet2)) {
            Result.OK -> {
                score += idx
                s += ": true -> "
            }

            else -> {
                s += ": false -> "
            }
        }
        s+=score
        println(s)
    }
    println(score)
}

enum class Result(val value: Int) {
    OK(-1),
    KO(1),
    TIE(0)
}

fun JsonNode.convert(): Any {
    if (this is IntNode) return this.intValue()
    if (this is ArrayNode) return this.map { it.convert() }.toList()
    return emptyList<Int>()
}

fun compare(left: Any, right: Any): Result {
    if (left is Int && right is Int) {
        return Result.values().first { it.value == (left.compareTo(right)) }
    } else if (left is List<*> && right is List<*>) {
        val minSize = min(left.size, right.size)
        for (idx in 0 until minSize) {
            when (val r = compare(left[idx]!!, right[idx]!!)) {
                Result.TIE -> continue
                else -> return r
            }
        }
        return if (left.size < right.size) {
            Result.OK
        } else {
            Result.KO
        }
    } else if (left is Int && right is List<*>) {
        return compare(listOf(left), right)
    } else if (left is List<*> && right is Int) {
        return compare(left, listOf(right))
    }
    return Result.TIE
}
