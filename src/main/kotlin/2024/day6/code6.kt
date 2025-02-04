package `2024`.day6

import Direction
import Position
import directions
import down
import getValue
import left
import nextPositionIndex
import plus
import prepare
import right
import up
import java.io.File
import java.lang.IndexOutOfBoundsException


const val day = 6

val prefix = "./src/main/kotlin/2024/day$day"

fun main() {

    val actual = File("$prefix/test").prepare().compute61()
    println(actual)
    require(actual == 41)
    File("${prefix}/1").prepare().compute61().let { println(it) }
    val actual2 = File("${prefix}/test").readLines().asSequence().filter { it.isNotBlank() }.compute62()
    println(actual2)
    require(actual2 == 6)
    File("${prefix}/1").readLines().asSequence().compute62().let { println(it) }
}


fun Sequence<String>.compute61(): Int {
    val grid = this.map { it.split("") }.toList()
    val (position, currentMove) = grid.findStartPosition()
    //Until out apply move
    return walkAround(position, grid, currentMove).size + 1
}

private fun walkAround(
    initPosition: Position,
    grid: List<List<String>>,
    initMove: Direction,
): MutableSet<Pair<Int, Int>> {
    val crossedPosition: MutableSet<Pair<Int, Int>> = mutableSetOf()
    var position = initPosition
    var currentMove = initMove
    var moveIndex = directions.indexOf(initMove)
    while (position.first in grid.indices && position.second in grid.first().indices) {
        try {
            if (grid.getValue(position.plus(currentMove)) == "#") {
                moveIndex = (moveIndex + 1) % directions.size
                currentMove = directions[moveIndex]
            }
            crossedPosition.add(position)
            position = position.plus(currentMove)
        } catch (e: IndexOutOfBoundsException) {
            position = position.plus(currentMove)
        }
    }
    return crossedPosition
}


//Ugly solution
//Walk the grid once
//For each crossed tile try to place a obstacle
//Make the guard walk all again -> if in loop (count nb time guard on tile) -> valid position else next

//Change to find if any other square using next position on the same line until end of grid
fun Sequence<String>.compute62(): Int {
    val grid = this.map { it.split("").toMutableList() }.toMutableList()
    val (position, currentMove) = grid.findStartPosition()
    val crossedPosition = walkAround(position, grid, currentMove)
    crossedPosition.remove(position)
    var sum = 0
    for (testPosition in crossedPosition) {
        val initChar = grid.getValue(testPosition)!!
        grid[testPosition.first][testPosition.second] = "#"
        val countingPosition = mutableMapOf<Position, Int>()
        walkAround(grid) { p ->
            countingPosition.compute(p) { _, v -> (v ?: 0) + 1 }
            if (countingPosition[p]!! > 4) {
                sum++
                return@walkAround false
            }
            return@walkAround true
        }
        grid[testPosition.first][testPosition.second] = initChar

    }
    return sum +1
}

fun walkAround(grid: List<List<String>>, block: (p: Position) -> Boolean) {
    var (position, currentMove) = grid.findStartPosition()
    var moveIndex = directions.indexOf(currentMove)
    //Until out apply move
    var shallContinue = true
    while (shallContinue) {
        try {
            if (grid.getValue(position.plus(currentMove)) == "#") {
                moveIndex = moveIndex.nextPositionIndex()
                currentMove = directions[moveIndex]
            } else if (block.invoke(position)) {
                position = position.plus(currentMove)
            } else {
                shallContinue = false
            }

        } catch (e: java.lang.IndexOutOfBoundsException) {
            shallContinue = false
        }
    }
}




fun List<List<String>>.findStartPosition(): Pair<Position, Direction> {
    for (x in this.indices) {
        for (y in this.first().indices) {
            when {
                this[x][y] == "." || this[x][y] == "#" -> {
                    continue
                }

                this[x][y] == "^" -> {
                    return Pair(Pair(x, y), up)
                }

                this[x][y] == "<" -> {
                    return Pair(Pair(x, y), left)
                }

                this[x][y] == ">" -> {
                    return Pair(Pair(x, y), right)
                }

                this[x][y] == "v" -> {
                    return Pair(Pair(x, y), down)

                }
            }
        }
    }
    throw IllegalStateException("Not found")
}
