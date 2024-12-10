package `2024`.day8

import buildGrid
import plus
import prepare
import java.io.File
import kotlin.math.abs

const val day = 8

val prefix = "./src/main/kotlin/2024/day$day"

fun main() {
    val actual = File("$prefix/test").prepare().compute81()
    println(actual)
    require(actual == 14)
    File("${prefix}/input").prepare().compute81().let { println(it) }
    val actual2 = File("${prefix}/test").readLines().asSequence().filter { it.isNotBlank() }.compute82()
    println(actual2)
    require(actual2 == 34)
    File("${prefix}/input").readLines().asSequence().compute82().let { println(it) }

}

fun Sequence<String>.compute81(): Int = this.compute8(block = { based, move, maxY, maxX, candidates ->
    val candidate = based.buildCandidate(move)
    if (candidate.isCandidate(maxY, maxX)) {
        candidates.add(candidate)
    }
}, postProcessing = { _, _ -> })

fun Sequence<String>.compute82(): Int = this.compute8(block = { based, move, maxY, maxX, candidates ->
    var candidate = based.buildCandidate(move)
    while (candidate.isCandidate(maxY, maxX)) {
        candidates.add(candidate)
        candidate = candidate.buildCandidate(move)
    }
}, postProcessing = { antennas, candidates ->
    antennas.filter { it.value.size > 1 }.forEach { k, v ->
        candidates.addAll(v)

    }
})


fun Sequence<String>.compute8(
    block: (
        based: Pair<Int, Int>,
        move: Pair<Int, Int>,
        maxY: Int,
        maxX: Int,
        candidates: MutableSet<Pair<Int, Int>>
    ) -> Unit,
    postProcessing: (antennas: MutableMap<String, MutableList<Pair<Int, Int>>>, candidates: MutableSet<Pair<Int, Int>>) -> Unit
): Int {
    val grid = this.buildGrid()
    val antennas = mutableMapOf<String, MutableList<Pair<Int, Int>>>()
    for (y in grid.indices) {
        for (x in grid[0].indices) {
            if (grid[y][x] != ".") {
                antennas.computeIfAbsent(grid[y][x]) { mutableListOf() }.add(Pair(y, x))
            }
        }
    }
    val maxY = grid.size - 1
    val maxX = grid.first().size - 1
    val candidates = mutableSetOf<Pair<Int, Int>>()
    for (symbol in antennas.keys) {
        val positions = antennas[symbol]!!
        positions.forEachIndexed { index, first ->
            //If last index continue
            if (index < positions.size - 1) {
                for (j in index + 1 until positions.size) {
                    val second = positions[j]
                    val dy = abs(first.first - second.first)
                    val dx = abs(first.second - second.second)
                    var multiplierY = 1
                    var multiplierX = 1
                    //First above second
                    if (second.first > first.first) {
                        multiplierY = -1
                    }
                    //First left to second
                    if (second.second > first.second) {
                        multiplierX = -1
                    }
                    val upgrade1 = Pair(dy * multiplierY, dx * multiplierX)

                    block(first, upgrade1, maxY, maxX, candidates)

                    val upgrade2 = Pair(-1 * dy * multiplierY, -1 * dx * multiplierX)
                    block(second, upgrade2, maxY, maxX, candidates)
                }

            }
        }
        postProcessing(antennas, candidates)
    }

    return candidates.size

}


fun Pair<Int, Int>.buildCandidate(pair: Pair<Int, Int>): Pair<Int, Int> = this.plus(pair)

fun Pair<Int, Int>.isCandidate(maxY: Int, maxX: Int) = first in 0..maxY && second in 0..maxX
