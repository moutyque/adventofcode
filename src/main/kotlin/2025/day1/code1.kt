package `2025`.day1

import java.io.File
import kotlin.math.abs
import kotlin.math.sign


fun main() {
   File("./src/main/kotlin/2025/day1/test").readLines().asSequence().compute11().also { println(it) }//3
    File("./src/main/kotlin/2025/day1/1").readLines().asSequence().compute11().also { println(it) } //1152
    File("./src/main/kotlin/2025/day1/test").readLines().asSequence().compute12().also { println(it) }//6
    File("./src/main/kotlin/2025/day1/1").readLines().asSequence().compute12().also { println(it) }
}
fun Sequence<String>.compute11(): Int {
    return this.map {
        when{
            it.startsWith('L') -> it.drop(1).toInt() * -1
            it.startsWith('R') -> it.drop(1).toInt()
            else -> 0
        }
    }.fold(Pair(0, 50)) { old, i ->
        var newCounter = old.first
        var currentPos = old.second

        // Simulate each individual click
        val steps = abs(i)
        val direction = sign(i.toDouble()).toInt()

        repeat(steps) {
            currentPos = (currentPos + direction).mod(100)
        }
        if (currentPos == 0) {
            newCounter++
        }

        Pair(newCounter, currentPos)
    }.first
}

fun Sequence<String>.compute12(): Int {
    return this.map {
        when {
            it.startsWith('L') -> it.drop(1).toInt() * -1
            it.startsWith('R') -> it.drop(1).toInt()
            else -> 0
        }
    }.fold(Pair(0, 50)) { old, i ->
        var newCounter = old.first
        var currentPos = old.second

        // Simulate each individual click
        val steps = abs(i)
        val direction = sign(i.toDouble()).toInt()

        repeat(steps) {
            currentPos = (currentPos + direction).mod(100)
            if (currentPos == 0) {
                newCounter++
            }
        }

        Pair(newCounter, currentPos)
    }.first
    //2696 -> too low
}



