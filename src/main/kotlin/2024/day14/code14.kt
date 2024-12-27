package `2024`.day14

import java.io.File

const val day = 14

val prefix = "./src/main/kotlin/2024/day$day"
val regex = """p=(?<x>\d+),(?<y>\d+) v=(?<vx>-?\d+),(?<vy>-?\d+)""".toRegex()


fun main() {

    File("${prefix}/test").readText().split("\n").filter { it.isNotBlank() }.let { compute141(it, 11, 7) }
        .also { println(it) }
        .also { require(it == 12) }
    val wide = 101
    val height = 103
    File("${prefix}/input").readText().split("\n").filter { it.isNotBlank() }.let { compute141(it, wide, height) }
        .also { println(it) }
        .also { require(it == 224554908) }

    //Easy way is to print into file all possibility then do grep
    //This is nice solution done after finding the right round
    File("${prefix}/input").readText().split("\n").filter { it.isNotBlank() }.let {
        compute142(it,wide,height)
    }.also { println(it) }
}

data class Robot(val x: Int, val y: Int, val vx: Int, val vy: Int) {
    fun getQuadrant(wide: Int, height: Int): Int {
        val xLimit = (wide / 2)
        val yLimit = (height / 2)
        return when {
            x < xLimit && y < yLimit -> 1
            x > xLimit && y < yLimit -> 2
            x < xLimit && y > yLimit -> 3
            x > xLimit && y > yLimit -> 4
            else -> 0
        }
    }
}

fun compute141(lines: List<String>, wide: Int, height: Int, round: Int = 100): Int =
    buildRobot(lines, round, wide, height)
        .groupingBy { it.getQuadrant(wide, height) }
        .eachCount()
        .filterKeys { it != 0 }
        .values.reduce(Int::times)

fun compute142(lines: List<String>, wide: Int,height: Int): Int {
    for (round in 1..wide * height) {
        val grid = Array(103) { Array(101) { '.' } }
        buildRobot(lines, round, wide, height)
            .forEach { robot ->
                grid[robot.y][robot.x] = 'X'
            }
        var concurrentLine = 0
        grid.map { it.joinToString("").contains("XXXXX") }.zipWithNext { a, b -> a && b }.forEach {
            if (it) {
                concurrentLine++
            } else {
                concurrentLine = 0
            }
            if(concurrentLine > 3){
                return round
            }
        }
    }
    return -1
}


fun buildRobot(
    lines: List<String>,
    round: Int,
    wide: Int,
    height: Int
) = lines.asSequence().map { line ->
    regex.find(line)!!.let { matchResult ->
        Robot(
            matchResult.groups["x"]!!.value.toInt(),
            matchResult.groups["y"]!!.value.toInt(),
            matchResult.groups["vx"]!!.value.toInt(),
            matchResult.groups["vy"]!!.value.toInt()
        )
    }
        .let {
            it.copy(
                x = (it.x + it.vx * round).mod(wide),
                y = (it.y + it.vy * round).mod(height)
            )
        }
}




