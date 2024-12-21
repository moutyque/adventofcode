package `2024`.day9

import Line
import prepare
import java.io.File

const val day = 9

val prefix = "./src/main/kotlin/2024/day$day"

fun main() {
    val actual = File("$prefix/test").prepare().compute91()
    println(actual)
    require(actual == 1928L)
    File("${prefix}/input").prepare().compute91().let { println(it) }
    val actual2 = File("${prefix}/test").readLines().asSequence().filter { it.isNotBlank() }.compute92()
    println(actual2)
    require(actual2 == 2858L)
    File("${prefix}/input").readLines().asSequence().compute92().let {
        println(it)
        require(it == 6227018762750L)
    }

}

data class Block(var start: Int, var size: Int, val id: Long) {
    fun subBlocks() = (0 until size).map { Block(it + start, 1, id) }

}

//Build to list of block (free + occupied) for each occupied try to place it in the first empty space
fun Pair<MutableList<Block>, MutableList<Block>>.splitBlockList() =
    let { it.first.flatMap { it.subBlocks() } to it.second.flatMap { it.subBlocks() } }

fun Sequence<Line>.compute91(): Long = this.map { l ->
    val (files, freeSpaces) = buildBlocks(l).splitBlockList()
    reorder(files, freeSpaces)
    computeScore(files)
}.sum()
fun Sequence<Line>.compute92(): Long = this.map { l ->
    val (files, freeSpaces) = buildBlocks(l)
    reorder(files, freeSpaces)
    computeScore(files)
}.sum()
private fun computeScore(files: List<Block>) = files.sortedBy { it.start }.sumOf { f ->
    (0 until f.size).sumOf { f.id * (f.start + it) }
}

private fun reorder(
    files: List<Block>,
    freeSpaces: List<Block>
) {
    for (file in files.reversed()) {
        //Read forward free
        for (free in freeSpaces) {
            if (free.start >= file.start) break
            if (free.size >= file.size) {
                //Move block
                free.size -= file.size
                file.start = free.start
                free.start += file.size
                break
            }
        }
    }
}

private fun buildBlocks(l: Line): Pair<MutableList<Block>, MutableList<Block>> {
    val files = mutableListOf<Block>()
    val freeSpaces = mutableListOf<Block>()
    var currentIndex = 0
    l.split("").filter { it.isNotBlank() }.map { it.toInt() }.forEachIndexed { index, length ->
        (if (index % 2 == 0) files else freeSpaces).add(
            Block(currentIndex, length, if (index % 2 == 0) (index / 2).toLong() else -1L)
        )
        currentIndex += length
    }
    return Pair(files, freeSpaces)
}
