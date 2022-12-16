package day9

import java.io.File
import kotlin.math.abs

fun main() {
    val iterator = File("./src/main/kotlin/day9/input").readLines().iterator()

    day91(iterator)
    day92(iterator)
}

fun day92(iterator: Iterator<String>) {
    val positions = mutableSetOf<Pair<Int, Int>>()
    val rope = mutableListOf<Pair<Int, Int>>().apply {
        repeat(10) {
            add(Pair(0, 0))
        }
    }
    positions.add(rope.first())
    fun Pair<Int, Int>.isNeighbors(p: Pair<Int, Int>): Boolean =
        abs(this.first - p.first) <= 1 && abs(this.second - p.second) <= 1

    fun Pair<Int, Int>.add(pair: Pair<Int, Int>): Pair<Int, Int> = Pair(first + pair.first, second + pair.second)
    fun Pair<Int, Int>.move(pair: Pair<Int, Int>): Pair<Int, Int> = when {
        pair.first == first && pair.second == second -> {
            this
        }

        pair.first == first -> {
            if (pair.second > second) Pair(first, second + 1) else Pair(first, second - 1)
        }

        pair.second == second -> {
            if (pair.first > first) Pair(first + 1, second) else Pair(first - 1, second)
        }

        pair.first > first && pair.second > second -> Pair(first + 1, second + 1)
        pair.first > first && pair.second < second -> Pair(first + 1, second - 1)
        pair.first < first && pair.second > second -> Pair(first - 1, second + 1)
        pair.first < first && pair.second < second -> Pair(first - 1, second - 1)
        else -> this
    }

    while (iterator.hasNext()) {
        iterator.next().split(" ").apply {
            repeat(this.last().toInt()) {
                when (this.first()) {
                    "U" -> Pair(0, 1)
                    "D" -> Pair(0, -1)
                    "L" -> Pair(-1, 0)
                    "R" -> Pair(1, 0)
                    else -> throw Error()
                }.also { transformation ->
                    rope[0] = rope[0].add(transformation)
                    for (i in 1 until rope.size) {
                        if (!rope[i - 1].isNeighbors(rope[i])) {
                            rope[i] = rope[i].move(rope[i - 1])
                            if (i == rope.size - 1) {
                                positions.add(rope[i])
                            }
                        }
                    }
                }
            }
        }
    }
    println(positions.size)
}

fun day91(iterator: Iterator<String>) {
    val positions = mutableSetOf<Pair<Int, Int>>()
    var H = Pair(0, 0)
    var T = Pair(0, 0)
    positions.add(T)
    fun Pair<Int, Int>.isNeighbors(p: Pair<Int, Int>): Boolean =
        abs(this.first - p.first) <= 1 && abs(this.second - p.second) <= 1

    while (iterator.hasNext()) {
        iterator.next().split(" ").apply {
            repeat(this.last().toInt()) {
                when (this.first()) {
                    "U" -> Pair(H.first, H.second + 1)
                    "D" -> Pair(H.first, H.second - 1)
                    "L" -> Pair(H.first - 1, H.second)
                    "R" -> Pair(H.first + 1, H.second)
                    else -> throw Error()
                }.also {
                    val oldH = H
                    H = it
                    if (!T.isNeighbors(H)) {
                        T = oldH
                        positions.add(T)
                    }
                }
            }
        }
    }
    println(positions.size)
}

