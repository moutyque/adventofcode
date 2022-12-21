package day15

import java.io.File
import java.math.BigInteger
import kotlin.math.abs

fun main() {
    val iterator = File("./src/main/kotlin/day15/input").readLines().iterator()
    day151(iterator, 2_000_000)
    //day152(iterator, 4_000_000)
}

fun Coord.distanceTo(c: Coord): Int = abs(c.x - x) + abs(c.y - y)
val reg =
    """Sensor at x=(?<x1>(-?)\d*), y=(?<y1>(-?)\d*): closest beacon is at x=(?<x2>(-?)\d*), y=(?<y2>(-?)\d*)""".toRegex()

data class Coord(val x: Int, val y: Int)
data class Sensor(val coord: Coord, val reach: Int) {
    fun x() = coord.x
    fun y() = coord.y

    fun canReach(c: Coord) =
        reach >= coord.distanceTo(c)


}

data class Beacon(val coord: Coord)

// Sensor area are exclusive because we always have a couple with the closest beacon to a sensor and they can be only one
//  - I.e. determine all points at d+1 relative to a sensor.
// Find if no other sensor can reach that point == that point is out of reach for all sensors
fun day152(iterator: Iterator<String>, max: Int) {
    val sensors = mutableMapOf<Sensor, Beacon>().apply {
        buildSensors(iterator)
    }
    val multiply = 4_000_000
    sensors.keys.asSequence()
        .forEach { sensor ->
            (0..max)
                .forEach { y ->
                    (sensor.reach - abs(sensor.y() - y)).also { delta ->
                        (sensor.x() - delta - 1).also { xL ->
                            if (xL in 0..max && sensors.keys.none { it.canReach(Coord(xL, y)) }) {
                                println(
                                    xL.toBigInteger().multiply(multiply.toBigInteger()).add(y.toBigInteger()).toString()
                                )
                                return
                            }
                        }
                        (sensor.x() + delta + 1).also { xR ->
                            if (xR in 0..max && sensors.keys.none { it.canReach(Coord(xR, y)) }) {
                                println(
                                    xR.toBigInteger().multiply(multiply.toBigInteger()).add(y.toBigInteger()).toString()
                                )
                                return
                            }
                        }
                    }
                }
        }
}

fun day151(iterator: Iterator<String>, targetLine: Int) {
    val sensors = mutableMapOf<Sensor, Beacon>().apply { buildSensors(iterator) }
    val (maxX, minX) = sensors.flatMap { listOf(it.key.x(), it.value.coord.x) }.run {
        Pair(max(), min())
    }
    val maxReach = sensors.keys.maxOfOrNull { it.reach }!!
    var counter = BigInteger.ZERO
    val coords = sensors.values.map { it.coord }
    for (idx in minX - maxReach..maxX+maxReach) {
        Coord(idx, targetLine).also { c ->
            if (coords.none { it == c } && sensors.keys.asSequence().any { it.canReach(c) }
                 ) {
                counter = counter.inc()
            }
        }
    }
    println(counter)
}


private fun MutableMap<Sensor, Beacon>.buildSensors(
    iterator: Iterator<String>
) {
    while (iterator.hasNext()) {
        //Parse sensor beacon
        reg.matchEntire(iterator.next())?.let {
            val groups = it.groups
            val s = Coord(groups["x1"]!!.value.toInt(), groups["y1"]!!.value.toInt())
            val b = Coord(groups["x2"]!!.value.toInt(), groups["y2"]!!.value.toInt())
            put(
                Sensor(s, s.distanceTo(b)),
                Beacon(b)
            )
        }
    }
}
