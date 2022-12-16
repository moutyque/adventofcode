package day8

import java.io.File

fun main() {
    val iterator = File("./src/main/kotlin/day8/input").readLines().iterator()

    day81(iterator)
    day82(iterator)
}



fun day82(iterator: Iterator<String>) {
    val trees = mutableListOf<List<Int>>()
    while (iterator.hasNext()) {
        trees.add(iterator.next().toCharArray().map { it.code - 48 })
    }

    fun Int.visibleTreeOnLeft(r: Int, c: Int, currentScore: Int = 1): Int = when {
        c < 0 -> currentScore - 1
        trees[r][c] >= this -> currentScore
        else -> visibleTreeOnLeft(r, c - 1, currentScore + 1)
    }

    fun Int.visibleTreeOnRight(r: Int, c: Int, currentScore: Int = 1): Int = when {
        c > trees.first().size - 1 -> currentScore - 1
        trees[r][c] >= this -> currentScore
        else -> visibleTreeOnRight(r, c + 1, currentScore + 1)
    }


    fun Int.visibleTreeOnTop(r: Int, c: Int, currentScore: Int = 1): Int = when {
        r < 0 -> currentScore - 1
        trees[r][c] >= this -> currentScore
        else -> visibleTreeOnTop(r - 1, c, currentScore + 1)
    }

    fun Int.visibleTreeButtom(r: Int, c: Int, currentScore: Int = 1): Int = when {
        r > trees.size - 1 -> currentScore - 1
        trees[r][c] >= this -> currentScore
        else -> visibleTreeButtom(r + 1, c, currentScore + 1)
    }

    var visibleInside = 0
    for (column in 1..trees.first().size - 2) {
        for (row in 1..trees.size - 2) {
            visibleInside = maxOf(visibleInside, trees[row][column].run {
                visibleTreeOnLeft(row, column - 1) * visibleTreeOnRight(row, column + 1) * visibleTreeOnTop(
                    row - 1, column
                ) * visibleTreeButtom(row + 1, column)

            })

        }
    }
    println(visibleInside)

}

fun day81(iterator: Iterator<String>) {
    val trees = mutableListOf<List<Int>>()
    while (iterator.hasNext()) {
        trees.add(iterator.next().toCharArray().map { it.code - 48 })
    }
    fun Int.visibleOnLeftTree(r: Int, c: Int): Boolean = when {
        c < 0 -> true
        trees[r][c] >= this -> false
        else -> visibleOnLeftTree(r, c - 1)
    }

    fun Int.visibleOnRightTree(r: Int, c: Int): Boolean = when {
        c > trees.first().size - 1 -> true
        trees[r][c] >= this -> false
        else -> visibleOnRightTree(r, c + 1)
    }


    fun Int.visibleOnTopTree(r: Int, c: Int): Boolean = when {
        r < 0 -> true
        trees[r][c] >= this -> false
        else -> visibleOnTopTree(r - 1, c)
    }

    fun Int.visibleOnBottomTree(r: Int, c: Int): Boolean = when {
        r > trees.size - 1 -> true
        trees[r][c] >= this -> false
        else -> visibleOnBottomTree(r + 1, c)
    }

    val visibleEdges = trees.first().size + trees.last().size + (trees.size * 2) - 4
    var visibleInside = 0
    for (column in 1..trees.first().size - 2) {
        for (row in 1..trees.size - 2) {
            trees[row][column].apply {
                visibleInside += when (visibleOnLeftTree(row, column - 1) || visibleOnRightTree(
                    row, column + 1
                ) || visibleOnTopTree(row - 1, column) || visibleOnBottomTree(row + 1, column)) {
                    true -> 1
                    false -> 0
                }
            }

        }
    }
    println(visibleEdges + visibleInside)

}

