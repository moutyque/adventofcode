package day14

import java.io.File

fun main() {
    val iterator = File("./src/main/kotlin/day14/input").readLines().iterator()
    day141(iterator)
}

fun String.isFree() = when (this) {
    "#", "O" -> false
    else -> true
}
// first dimension left to right: x [0:700]
// second dimension top to bottom: y [0:200]

fun Array<Array<String>>.sandGoesDow(x: Int, y: Int): Boolean =
    when {

        (y >= this[x].size - 1) -> false
        this[x][y + 1].isFree() -> this.sandGoesDow(x, y + 1) //Below is free
        this[x - 1][y + 1].isFree() -> this.sandGoesDow(x - 1, y + 1) //Below left is free
        this[x + 1][y + 1].isFree() -> this.sandGoesDow(x + 1, y + 1) //Below right is free
        (x == 500 && y ==0) -> { //New condtion for second statement
            this[x][y] = "O"
            false
        }
        else -> {
            this[x][y] = "O"
            true
        }
    }


fun Array<Array<String>>.fill(p1: Pair<Int, Int>, p2: Pair<Int, Int>) =
    this.fill(p1.first, p1.second, p2.first, p2.second)

fun Array<Array<String>>.fill(x1: Int, y1: Int, x2: Int, y2: Int) {
    for (col in minOf(x1, x2)..maxOf(x1, x2)) {
        for (row in minOf(y1, y2)..maxOf(y1, y2)) {
            this[col][row] = "#"
        }
    }
}

fun day141(iterator: Iterator<String>) {
    val grid = Array(700) {
        Array(200) { "." }
    }

    //[700:200] [columns:rows]
    //Build the grid
    var maxY = 0
    iterator.asSequence().forEach { row ->
        row.split("->")
            .map { it.trim() }
            .map { coord ->
                coord.split(",").run {
                    Pair(this.first().toInt(), this.last().toInt())
                }
            }
            .run {
                onEachIndexed { index, pair ->
                    if (index > 0) {
                        grid.fill(this[index - 1], pair)
                    }
                }
                maxY = maxOf { it.second } //New condtion for second statement
            }
    }
    maxY+=2
    grid.fill(0,maxY,699,maxY ) //New condtion for second statement
    //Fil the sand until get out
    grid[500][0] = "+"
    while (grid.sandGoesDow(500, 0)){

    }
    //How many grid or field with sands
    grid.iterator().asSequence().map { row -> row.map { it == "O" }.count { it } }.sum().let { println(it) }
}
