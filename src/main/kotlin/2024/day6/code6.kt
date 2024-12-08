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
    // File("${prefix}/1").readLines().asSequence().compute62().let { println(it) }
}

fun File.prepare() = this.readLines().asSequence().filter { it.isNotBlank() }

val up = Pair(-1, 0)
val down = Pair(1, 0)
val right = Pair(0, 1)
val left = Pair(0, -1)
val direction = listOf(up, right, down, left)
var currentMove = up
var position = Pair(0, 0)
var moveIndex = 0
fun Sequence<String>.compute61(): Int {
    val grid = this.map { it.split("") }.toList()
    grid.findStartPosition()
    val crossedPosition = mutableSetOf<Pair<Int, Int>>()
    moveIndex = direction.indexOf(currentMove)
    //Until out apply move
    while (position.first in grid.indices && position.second in grid.first().indices) {
        try{
            if (grid.get(position.plus(currentMove)) == "#") {
                moveIndex = (moveIndex + 1) % direction.size
                currentMove = direction[moveIndex]
            }
            crossedPosition.add(position)
            position = position.plus(currentMove)
        } catch (e: java.lang.IndexOutOfBoundsException){
            position = position.plus(currentMove)
        }
    }
    return crossedPosition.size +1
}
fun Sequence<String>.compute62(): Int {
    var possibleBlock = 0
    val grid = this.map { it.split("") }.toList()
    grid.findStartPosition()
    val crossedPosition = mutableSetOf<Pair<Int, Int>>()
    crossedPosition.add(position)
    moveIndex = direction.indexOf(currentMove)
    //Until out apply move
    while (position.first in grid.indices && position.second in grid.first().indices) {
        try{
            if (grid.get(position.plus(currentMove)) == "#") {
                moveIndex = (moveIndex + 1) % direction.size
                currentMove = direction[moveIndex]
            }

            if(!crossedPosition.add(position)){
                possibleBlock++
            }

            position = position.plus(currentMove)
        } catch (e: java.lang.IndexOutOfBoundsException){
            position = position.plus(currentMove)
        }
    }
    return possibleBlock
}

fun List<List<String>>.isClose(position: Pair<Int, Int>) : Boolean=
    direction.any {
        this.get(position.plus(up)) == "#"
    }



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