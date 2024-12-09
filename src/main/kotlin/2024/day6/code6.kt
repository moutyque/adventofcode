package `2024`.day6

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

fun File.prepare() = this.readLines().asSequence().filter { it.isNotBlank() }

val up = Pair(-1, 0)
val down = Pair(1, 0)
val right = Pair(0, 1)
val left = Pair(0, -1)
val direction = listOf(up, right, down, left)
var currentMove = up
var position = Pair(0, 0)
fun Sequence<String>.compute61(): Int {
    val grid = this.map { it.split("") }.toList()
    grid.findStartPosition()
    val crossedPosition = mutableSetOf<Pair<Int, Int>>()
    var moveIndex = direction.indexOf(currentMove)
    //Until out apply move
    while (position.first in grid.indices && position.second in grid.first().indices) {
        try {
            if (grid.get(position.plus(currentMove)) == "#") {
                moveIndex = (moveIndex + 1) % direction.size
                currentMove = direction[moveIndex]
            }
            crossedPosition.add(position)
            position = position.plus(currentMove)
        } catch (e: java.lang.IndexOutOfBoundsException) {
            position = position.plus(currentMove)
        }
    }
    return crossedPosition.size + 1
}

//Change to find if any other square using next position on the same line until end of grid
fun Sequence<String>.compute62(): Int {
    val grid = this.map { it.split("") }.toList()
    grid.findStartPosition()
    val crossedPosition = mutableSetOf<Pair<Int, Int>>()
    crossedPosition.add(position)
    var moveIndex = direction.indexOf(currentMove)
    var candidatesCount = 0
    //Until out apply move
    while (position.first in grid.indices && position.second in grid.first().indices) {
        try {
            if (grid.get(position.plus(currentMove)) == "#") {
                moveIndex = moveIndex.nextPositionIndex()
                currentMove = direction[moveIndex]
            } else {
                crossedPosition.add(position)
                if(isPattern(grid, direction[moveIndex.nextPositionIndex()], position, crossedPosition)){
                    candidatesCount++
                }
            }
            position = position.plus(currentMove)
        } catch (e: java.lang.IndexOutOfBoundsException) {
            position = position.plus(currentMove)
        }
    }
    return candidatesCount
}

fun isPattern(
    grid: List<List<String>>,
    direction: Pair<Int, Int>,
    initPosition: Pair<Int, Int>,
    crossedPosition: Set<Pair<Int, Int>>
): Boolean {
    var position = initPosition
    var response = false
    var count = 0
    try {
        while (grid.get(position) != "#") {
            position = position.plus(direction)
            if(crossedPosition.contains(position)){
                count++
            }
        }
        if (count > 0) response = true
    } catch (e: java.lang.IndexOutOfBoundsException) {
        response = false
    }

    return response
}

fun Int.nextPositionIndex() = (this + 1) % direction.size


fun List<List<String>>.get(position: Pair<Int, Int>) = this[position.first][position.second]
fun Pair<Int, Int>.plus(b: Pair<Int, Int>) = Pair(this.first + b.first, this.second + b.second)

fun List<List<String>>.findStartPosition() {
    for (x in this.indices) {
        for (y in this.first().indices) {
            when {
                this[x][y] == "." || this[x][y] == "#" -> {
                    continue
                }

                this[x][y] == "^" -> {
                    position = Pair(x, y)
                    currentMove = up
                    return
                }

                this[x][y] == "<" -> {
                    position = Pair(x, y)
                    currentMove = left
                    return
                }

                this[x][y] == ">" -> {
                    position = Pair(x, y)
                    currentMove = right
                    return
                }
            }
            if (this[x][y] == "v") {
                position = Pair(x, y)
                currentMove = down
                return
            }

        }
    }
}
