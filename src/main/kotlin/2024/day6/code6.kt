package `2024`.day6

import plus
import prepare
import java.io.File


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

val up = Pair(-1, 0)
val down = Pair(1, 0)
val right = Pair(0, 1)
val left = Pair(0, -1)
val directions = listOf(up, right, down, left)


fun Sequence<String>.compute61(): Int {
    val grid = this.map { it.split("") }.toList()
    var (position, currentMove) = grid.findStartPosition()
    val crossedPosition = mutableSetOf<Pair<Int, Int>>()
    var moveIndex = directions.indexOf(currentMove)
    //Until out apply move
    while (position.first in grid.indices && position.second in grid.first().indices) {
        try {
            if (grid.get(position.plus(currentMove)) == "#") {
                moveIndex = (moveIndex + 1) % directions.size
                currentMove = directions[moveIndex]
            }
            crossedPosition.add(position)
            position = position.plus(currentMove)
        } catch (e: java.lang.IndexOutOfBoundsException) {
            position = position.plus(currentMove)
        }
    }
    return crossedPosition.size + 1
}


//Ugly solution
//Walk the grid once
//For each crossed tile try to place a obstacle
//Make the guard walk all again -> if in loop (count nb time guard on tile) -> valid position else next

//Change to find if any other square using next position on the same line until end of grid
fun Sequence<String>.compute62(): Int {
    val grid = this.map { it.split("").toMutableList() }.toMutableList()
    val crossedPosition = mutableSetOf<Pair<Int, Int>>()
    walkAround(grid) {
        crossedPosition.add(it)
    }
    var (position, currentMove) = grid.findStartPosition()
    crossedPosition.remove(position)
    var sum = 0
    for (testPosition in crossedPosition) {
        val initChar = grid.get(testPosition)
        grid[testPosition.first][testPosition.second] = "#"
        val countingPosition = mutableMapOf<Position, Int>()


        walkAround(grid) {
            countingPosition[position] = (countingPosition[position] ?: 0) + 1
            if (countingPosition[position]!! > 3) {
                sum++
                return@walkAround false
            }
            return@walkAround true
        }
        grid[testPosition.first][testPosition.second] = initChar

    }
    return sum
}

fun walkAround(grid: List<List<String>>, block: (position: Position) -> Boolean) {
    var (position, currentMove) = grid.findStartPosition()
    var moveIndex = directions.indexOf(currentMove)
    //Until out apply move
    while (position.first in grid.indices && position.second in grid.first().indices) {
        try {
            if (grid.get(position.plus(currentMove)) == "#") {
                moveIndex = moveIndex.nextPositionIndex()
                currentMove = directions[moveIndex]
            } else {
                if (block.invoke(position)) {
                    position = position.plus(currentMove)
                } else {
                    position = Pair(-1, -1)
                }
            }

        } catch (e: java.lang.IndexOutOfBoundsException) {
            position = position.plus(currentMove)
        }
    }
}

fun Int.nextPositionIndex() = (this + 1) % directions.size

fun List<List<String>>.get(position: Pair<Int, Int>) = this[position.first][position.second]

typealias Direction = Pair<Int, Int>
typealias Position = Pair<Int, Int>

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
