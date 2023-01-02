package day16

import java.io.File
import java.util.*

fun main() {
    val iterator = File("./src/main/kotlin/day16/test").readLines().iterator()
    day161(iterator)
}

fun day161(iterator: Iterator<String>) {
    val vertices = mutableListOf<Vertex>()
    val edges = mutableListOf<Edge>()
    //Parse input
    val reg =
        """Valve (?<origin>\w+) has flow rate=(?<flowRate>\d+); (?:tunnels lead to valves|tunnel leads to valve) (?<destination>[\w,\s]+)""".toRegex()
    iterator.forEachRemaining {
        reg.matchEntire(it)?.groups?.let { groups ->
            val vertexId = groups["origin"]!!.value
            val vertexFlowRate = groups["flowRate"]!!.value
            val destinations = groups["destination"]!!.value
            vertices.add(Vertex(vertexId, vertexFlowRate.toInt()))
            edges.addAll(destinations.split(",").map { Edge(vertexId, it.trim(), 1) })

        } ?: println(it)
    }
    //Reduce vertex and Edge by removing vertex with flow of 0
    println("Before")
    println(vertices.count())
    println(edges.count())
    reduceMap(vertices, edges)
    println("After")
    println(vertices.count())
    println(edges.count())
}

fun reduceMap(vertices: MutableList<Vertex>, edges: MutableList<Edge>) {
    val vToClean = vertices.filter { it != vertices.first() && it.flowRate == 0 }
    vToClean.forEach { v ->

        var toClean = edges.filter { it.from == v.id || it.to == v.id }
        while (toClean.isNotEmpty()) {
            val edgeToRemove = mutableSetOf<Edge>()
            val edgeToAdd = mutableSetOf<Edge>()
            //Keep edges connected to empty vertex
            toClean.apply {
                forEach { e ->
                    this.filter { (it.to == e.from).xor(it.from == e.to) }//Keep edges connected to current edge
                        .forEach {
                            if (it.to == e.from) {
                                edgeToAdd.add(Edge(it.from, e.to, e.duration + it.duration))
                            } else if (it.from == e.to) {
                                edgeToAdd.add(Edge(e.from, it.to, e.duration + it.duration))
                            }
                        }
                    edgeToRemove.add(e)
                }

            }
            edges.removeAll(edgeToRemove)
            edges.addAll(edgeToAdd)
            toClean = edges.filter { it.from == v.id || it.to == v.id }
        }
    }
    vertices.removeAll(vToClean)
    val ids = vertices.map { it.id }
    edges.removeIf { it.from !in ids || it.to !in ids }
}

fun maxPressureRelease(vertices: Map<String, Vertex>, edges: Map<String, List<Edge>>): Long {
    val queue = PriorityQueue<Pair<Long, String>>(compareBy { it.first })
    val previous = mutableMapOf<String, String>()

    val distances = vertices.values.associate { it.id to Long.MAX_VALUE }.toMutableMap()

    // Add the starting vertex to the queue with distance 0
    vertices.keys.first().apply {
        queue.add(0L to this)
        distances[this] = 0L
    }

    while (queue.isNotEmpty()) {
        val current = queue.poll().second
        for (edge in edges[current]!!) {
            val neighbor = edge.to
            val distance =
                distances[current]!! + 2 // Add 2 to account for the time to open the valve and the time to travel through the tunnel
            if (distance < distances[neighbor]!!) {
                distances[neighbor] = distance
                previous[neighbor] = current
                queue.add(distance to neighbor)
            }
        }
    }

    // Calculate the maximum pressure release
    var maxPressure = 0L
    for (vertex in vertices.values) {
        val flowRate = vertex.flowRate
        val distance = distances[vertex.id]!!
        if (distance != Long.MAX_VALUE && flowRate > 0) {
            maxPressure += flowRate * (30 - distance)
        }
    }

    return maxPressure
}

data class Vertex(val id: String, val flowRate: Int)
data class Edge(val from: String, val to: String, val duration: Int)





