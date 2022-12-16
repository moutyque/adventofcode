package day12

import java.io.File

data class Node(val x: Int, val y: Int, val height: Int) {
    val edges: MutableList<Edge> = mutableListOf()
}

data class Edge(val length: Int, val to: Node)
fun main() {
    val iterator = File("./src/main/kotlin/day12/input").readLines().iterator()

    day121(iterator)
    day122(iterator)
}
fun day122(iterator: Iterator<String>) {
    val maze = iterator.asSequence().map { it }.toList()

    val convertor = "abcdefghijklmnopqrstuvwxyz"
    val distances = mutableMapOf<Node, Long>()
    for (r in maze.indices) {
        for (c in maze[r].indices) {
            when (maze[r][c]) {
                'S' -> {
                    distances[Node(c, r, 0)] = Long.MAX_VALUE - 1
                }

                'E' -> {
                    Node(c, r, 25).apply {
                        distances[this] = 0
                    }
                }

                else -> distances[Node(c, r, convertor.indexOf(maze[r][c]))] = Long.MAX_VALUE - 1
            }
        }
    }
    computeDistances(distances)
    println(distances[distances.keys.filter { it.height == 0 }.minBy { distances[it]!! }])
}

private fun computeDistances(distances: MutableMap<Node, Long>) {
    for (node in distances.keys) {
        node.edges.addAll(distances.keys.asSequence().filter { node.height <= it.height + 1 }
            .filter { (it.x == node.x && (it.y + 1 == node.y || it.y - 1 == node.y)) || (it.y == node.y && (it.x + 1 == node.x || it.x - 1 == node.x)) }
            .map { Edge(1, it) }
            .toList())
    }

    val remaining = distances.keys.toMutableList()
    while (remaining.isNotEmpty()) {
        val node: Node = remaining.minBy { distances[it]!! } //take the vertex with the shortest path to the start
        remaining.remove(node) //remove it from vertex to process
        for (v in node.edges) {
            // Relaxation
            //For each edges linked to this vertex check
            // if the current distance to start + edge length is lesser than the distance store to the destination vertex
            if (distances[v.to]!! > distances[node]!! + v.length) { //If so
                distances[v.to] = distances[node]!! + v.length // Update destination vertex with lesser value
            }
        }
    }
}

fun day121(iterator: Iterator<String>) {
    val maze = iterator.asSequence().map { it }.toList()
    val convertor = "abcdefghijklmnopqrstuvwxyz"
    val distances = mutableMapOf<Node, Long>()
    var end: Node? = null
    for (r in maze.indices) {
        for (c in maze[r].indices) {
            when (maze[r][c]) {
                'S' -> {
                    distances[Node(c, r, 0)] = 0
                }

                'E' -> {
                    Node(c, r, 25).apply {
                        distances[this] = Long.MAX_VALUE - 1
                        end = this
                    }
                }

                else -> distances[Node(c, r, convertor.indexOf(maze[r][c]))] = Long.MAX_VALUE - 1
            }
        }
    }
    computeDistances(distances)
    println(distances[end])
}
