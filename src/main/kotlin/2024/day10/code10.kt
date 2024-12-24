package `2024`.day10

import buildIntGrid
import directions
import getValue
import plus
import prepare
import java.io.File


const val day = 10

val prefix = "./src/main/kotlin/2024/day$day"

fun main() {
    File("$prefix/test").prepare().compute101().also { println(it) }.also { require(it == 36) }
    File("${prefix}/input").prepare().compute101().let { println(it) }

    File("$prefix/test").prepare().compute102().also { println(it) }.also { require(it == 81) }
    File("${prefix}/input").prepare().compute102().let { println(it) }
}


fun Sequence<String>.compute101(): Int {

    val (nodes, starts) = prepare()
    //Loop over grid
    return starts.sumOf {
        nodes.forEach { it.value.visited = false }
        nodes[it]!!.reachableDestination()
    }
}

fun Sequence<String>.compute102(): Int {

    val (nodes, starts) = prepare()
    //Loop over grid
    return starts.sumOf {
        nodes[it]!!.pathsToDestination()
    }
}

private fun Sequence<String>.prepare(): Pair<MutableMap<Node, Node>, MutableSet<Node>> {
    val nodes = mutableMapOf<Node, Node>()
    val starts = mutableSetOf<Node>()
    val grid = this.buildIntGrid()
    //Prepare node grids
    for (y in grid.indices) {
        for (x in grid[y].indices) {
            //Check if each direction is a valid candidate
            val value = grid[y][x]
            if (value == 0) {
                starts.add(Pair(y, x).toNode().also { it.value = 0 })
            }
            val neighbors = directions.mapNotNull { d ->
                Pair(y, x).plus(d).let {
                    //Is value of neighbor ok
                    val nValue = grid.getValue(it) ?: return@mapNotNull null
                    when {
                        nValue - value != 1 -> null
                        else -> it.toNode().also { n -> n.value = nValue }
                    }
                }
            }
                .map { n ->
                    nodes.computeIfAbsent(n) { n }
                    nodes[n]!!
                }
            val node = Node(x, y).also { it.value = value }
            val savedNode = nodes.computeIfAbsent(node) { node }
            savedNode.neighbors.addAll(neighbors)
            nodes[savedNode] = savedNode
        }
    }
    return Pair(nodes, starts)
}

fun Pair<Int, Int>.toNode() = Node(second, first)

fun Node.reachableDestination(): Int {
    visited = true
    if (value == 9) return 1
    return neighbors.filter { !it.visited }.sumOf {
        it.reachableDestination()
    }
}

fun Node.pathsToDestination(): Int {
    if (value == 9) return 1
    return neighbors.sumOf {
        it.pathsToDestination()
    }
}


data class Node(val x: Int, val y: Int) {
    val neighbors: MutableSet<Node> = mutableSetOf()
    var value: Int = -1
    var visited: Boolean = false
}
