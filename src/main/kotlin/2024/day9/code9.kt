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
    File("${prefix}/input").readLines().asSequence().compute92().let { println(it) }

}

data class Block(val start: Int, val size: Int, val value: Long) {
    fun end() = start + size
}

fun Sequence<Line>.compute9(block: (list: MutableList<Long>) -> Unit): Long = this.map { l ->
    val explicitRow = mutableListOf<Long>()
    var id = 0L
    var isFile = true
    l.split("").filter { it.isNotBlank() }.forEach { n ->
        for (i in 1..n.toLong()) {
            if (isFile) {
                explicitRow.add(id)
            } else {
                explicitRow.add(-1)
            }
        }
        if (isFile) {
            id++
        }
        isFile = !isFile
    }
    block(explicitRow)
    explicitRow.mapIndexed { i, j -> i * j }.filter { it > -1 }.sum()
}.sum()

fun Sequence<Line>.compute92(): Long = this.compute9 { list -> list.reorderBlock() }

fun Sequence<Line>.compute91(): Long = this.compute9 { list -> list.reorderIndividual() }

fun MutableList<Long>.reorderBlock() {
    var j = this.lastIndex
    var firstEmptyIndex = 0
    while (this[firstEmptyIndex] !=-1L){
        firstEmptyIndex++
    }
    while (firstEmptyIndex < j) {
        //Find block
        val blockToMove = this.buildBlockBackward(j) ?: return
        val emptyBlock = this.findEmptySpot(blockToMove,firstEmptyIndex)
        if (emptyBlock != null) {
            //Move block
            this.moveBlock(blockToMove, emptyBlock.start)
            this.clean(blockToMove)
        }
        j -= blockToMove.size
        while (this[j] == -1L) {
            j--
        }
        while (this[firstEmptyIndex] !=-1L){
            firstEmptyIndex++
        }
    }

}

fun MutableList<Long>.clean(block: Block) {
    for (i in block.start until block.end()) {
        this[i] = -1
    }
}

fun MutableList<Long>.moveBlock(block: Block, startIndex: Int) {
    for (i in startIndex until startIndex + block.size) {
        this[i] = block.value
    }
}

fun MutableList<Long>.findEmptySpot(blockToMove: Block,firstEmptyIndex:Int): Block? =
    try {
        var i = firstEmptyIndex
        var emptyBlock = this.buildBlockForward(i)
        while (emptyBlock.size < blockToMove.size && i < blockToMove.start) {
            i += emptyBlock.size
            emptyBlock = this.buildBlockForward(i)
        }
        if (i >= blockToMove.start) {
            null
        } else {
            emptyBlock
        }
    } catch (e: IndexOutOfBoundsException) {
        null
    }


fun MutableList<Long>.buildBlockBackward(startIndex: Int): Block? {
    var i = startIndex
    val ref = this[i]
    while (ref == this[i] && i > 0) {
        i--
    }
    if (i == 0) {
        return null
    }
    val size = startIndex - i
    return Block(i + 1, size, ref)
}

fun MutableList<Long>.buildBlockForward(initialIndex: Int): Block {
    var i = initialIndex
    while (this[i] != -1L) {
        i++
    }
    val startIndex = i
    while (this[i] == -1L) {
        i++
    }
    val size = i - startIndex
    return Block(startIndex, size, -1)
}

fun MutableList<Long>.reorderIndividual() {
    var i = 0
    var j = this.lastIndex
    while (i < j) {
        if (this[i] == -1L) {
            while (this[j] == -1L) {
                j--
            }
            this[i] = this[j]
            this[j] = -1
            j--
            i++

        } else {
            i++
        }
    }
}

