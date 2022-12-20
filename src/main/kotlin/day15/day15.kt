package day15

import java.io.File
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.abs


// For each calculate if the reach line Y

//For 0 to maxX on line Y how many row are filled

fun main() {
    val iterator = File("./src/main/kotlin/day15/input").readLines().iterator()
    day15(iterator, 2000000)
}

fun Pair<Int, Int>.distanceTo(p: Pair<Int, Int>): Int = abs(p.first - first) + abs(p.second - second)
val reg =
    """Sensor at x=(?<x1>(-?)\d*), y=(?<y1>(-?)\d*): closest beacon is at x=(?<x2>(-?)\d*), y=(?<y2>(-?)\d*)""".toRegex()
typealias Sensor = Pair<Int, Int>
typealias Beacon = Pair<Int, Int>

fun day15(iterator: Iterator<String>, targetLine: Int) {
    val sensors = mutableMapOf<Sensor, Beacon>()
    while (iterator.hasNext()) {
        //Parse sensor beacon
        reg.matchEntire(iterator.next())?.let {
            val groups = it.groups
            sensors.put(
                Pair(groups["x1"]!!.value.toInt(), groups["y1"]!!.value.toInt()),
                Pair(groups["x2"]!!.value.toInt(), groups["y2"]!!.value.toInt())
            )
        }
    }
    val maxX = max(sensors.keys.maxOfOrNull { it.first }!!, sensors.values.maxOfOrNull { it.first }!!)
    val minX = min(sensors.keys.minOfOrNull { it.first }!!, sensors.values.minOfOrNull { it.first }!!)
    val maxDistance = sensors.map { it.key.distanceTo(it.value) }.max()
    val line = MutableList(maxX - minX + 2 * maxDistance) { "." }
    val offset = -minX + maxDistance
    val offsetSensors =
        sensors.map { Pair(it.key.first + offset, it.key.second) to Pair(it.value.first + offset, it.value.second) }
            .toMap()

    for ((sensor, beacon) in offsetSensors) {
        if (sensor.second == targetLine) {
            line[sensor.first] = "S"
        }
        if (beacon.second == targetLine) {
            line[beacon.first] = "B"
        }
        val reachDistance = sensor.distanceTo(beacon)
        //Check if zone reach target line
        for (idx in 0 until line.size) {
            if (sensor.distanceTo(Pair(idx, targetLine)) <= reachDistance) {
                if (line[idx] == ".") {
                    line[idx] = "#"
                }
            }
        }
    }
    println(line.count { it == "#" })
}
