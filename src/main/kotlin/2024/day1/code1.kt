import java.io.File
import kotlin.math.abs


fun main() {
    val actual = File("./src/main/kotlin/2024/day1/test").readLines().asSequence().compute11()
    assert(actual == 11)
    val actual2 = File("./src/main/kotlin/2024/day1/test").readLines().asSequence().compute12()
    assert(actual2 == 31)
    File("./src/main/kotlin/2024/day1/2").readLines().asSequence().compute12().let { println(it) }
}

fun Sequence<String>.compute11(): Int {
    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()

    this.map { it.split("   ") } //Sequence<List<String>>
        .forEach { l ->
            left.add(l[0].toInt())
            right.add(l[1].toInt())
        }
    left.sort()
    right.sort()

    var diff = 0
    for (i in 0 until left.size) {
        diff += abs(left[i] - right[i])
    }
    return diff
}

fun Sequence<String>.compute12(): Int {
    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()

    this.map { it.split("   ") } //Sequence<List<String>>
        .forEach { l ->
            left.add(l[0].toInt())
            right.add(l[1].toInt())
        }
    left.sort()
    right.sort()
    var score = 0
    for (i in left) {
        score += i * right.count { it == i }

    }

    return score
}


