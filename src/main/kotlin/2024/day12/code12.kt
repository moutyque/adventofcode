package `2024`.day12

import `2024`.day11.cache
import Position
import directions
import getValue
import next
import plus
import prepare
import java.io.File

const val day = 12

val prefix = "./src/main/kotlin/2024/day$day"
fun main() {
    File("$prefix/test").prepare().compute121().also { println(it) }.also { require(it == 1930) }
    File("$prefix/input").prepare().compute121().also { println(it) }.also { require(it == 1464678) }
    File("$prefix/test").prepare().compute122().also { println(it) }.also { require(it == 1206) }
    File("$prefix/input").prepare().compute122().also { println(it) }.also { require(it == 877492) }

}

data class Cell(
    val letter: String,
    var area: Int = 0,
    var perimeter: Int = 0,
    var corner: Int = 0,
    val position: Position,
    var visited: Boolean = false,
) {
    fun score() = area * perimeter
    fun scoreCorner() = area * corner
}


fun Sequence<String>.compute121(): Int = this.compute12 { it.score() }
fun Sequence<String>.compute122(): Int = this.compute12 { it.scoreCorner() }



fun Sequence<String>.compute12(block: (cell: Cell) -> Int): Int {
    val cache = mutableListOf<Cell>()
    val grid = buildCellGrid(buildGrid())
    for (first in grid.indices) {
        for (second in grid[0].indices) {
            val position = Pair(first, second)
            val currentCell = grid.getValue(position)!!
            if (!currentCell.visited) {
                cache.add(explore(currentCell, grid))
            }
        }
    }
    return cache.sumOf { block(it) }
}

fun buildCellGrid(grid: MutableList<MutableList<Cell>>): MutableList<MutableList<Cell>> {
    val newGrid = mutableListOf<MutableList<Cell>>()
    for (line in grid) {
        newGrid.add(mutableListOf())
        for (cell in line) {
            val results = directions.associateWith { dir ->
                (cell.position.plus(dir).let { grid.getValue(it) }
                    ?.let {
                        if (it.letter != cell.letter) Cell(
                            cell.letter,
                            perimeter = 1,
                            position = cell.position
                        ) else Cell(
                            it.letter,
                            position = cell.position
                        )
                    }
                    ?: Cell(cell.letter, perimeter = 1, position = cell.position))
            }

            val data = results.onEach { (direction, result) ->
                //Concave
                val nextDirection = directions.next(direction)
                if (results[nextDirection]!!.perimeter == 1 && result.perimeter == 1) {
                    result.corner += 1
                } else if (results[nextDirection]!!.perimeter == 0 && result.perimeter == 0 && grid.getValue(
                        cell.position.plus(
                            direction
                        ).plus(nextDirection)
                    )?.let { it.letter != cell.letter } == true
                ) {//Convex
                    result.corner += 1
                }
            }.map { it.value }
                .reduce { acc, data ->
                    Cell(
                        cell.letter,
                        1,
                        acc.perimeter + data.perimeter,
                        acc.corner + data.corner,
                        cell.position
                    )
                }
            newGrid.last().add(data)
        }
    }
    return newGrid
}

private fun Sequence<String>.buildGrid() = this.mapIndexed { indexFirst, line ->
    line.split("").filter { it.isNotBlank() }
        .mapIndexed { indexSecond, c -> Cell(c, position = indexFirst to indexSecond) }.toMutableList()
}
    .toMutableList()

fun explore(cell: Cell, grid: List<List<Cell>>): Cell {
    cell.visited = true
    return directions.mapNotNull { dir ->
        grid.getValue(cell.position.plus(dir))?.takeIf { !it.visited && it.letter == cell.letter }?.let {
            explore(it, grid)
        }
    }.takeIf { it.isNotEmpty() }?.reduce { acc, data ->
        Cell(
            cell.letter,
            acc.area + data.area,
            acc.perimeter + data.perimeter,
            acc.corner + data.corner,
            cell.position
        )
    }?.let { acc ->
        Cell(
            cell.letter,
            acc.area + cell.area,
            acc.perimeter + cell.perimeter,
            acc.corner + cell.corner,
            cell.position
        )
    } ?: cell
}

