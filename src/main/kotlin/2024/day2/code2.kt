import java.io.File
import kotlin.math.abs

val prefix = "./src/main/kotlin/2024/day2"
fun main() {

    val actual = File("${prefix}/test").readLines().asSequence().compute21()
    require(actual == 2)
    File("${prefix}/1").readLines().asSequence().compute21().let { println(it) }
    val actual2 = File("${prefix}/test").readLines().asSequence().compute22()
    println(actual2)
    require(actual2 == 4)
    File("${prefix}/2").readLines().asSequence().compute22().let { println(it) }
}

fun Sequence<String>.compute21(): Int {
    return this.filter { it.isNotBlank() }.map {
        val l: MutableList<Int> = it.split("\\s+".toRegex()).map { s -> s.toInt() }.toMutableList()
        when {
            l == l.sorted() || l == l.sortedDescending() -> {
                for (i in 0 until l.size - 1) {
                    val diff = abs(l[i + 1] - l[i])
                    if (!diff.inAcceptableRange()) {
                        return@map 0
                    }
                }
                return@map 1
            }

            else -> {
                0
            }
        }
    }.sum()
}

fun Int.inAcceptableRange() = this in 1..3

fun Sequence<String>.compute22(): Int {
    return this.filter { it.isNotBlank() }.map {
        val l: MutableList<Int> = it.split("\\s+".toRegex()).map { s -> s.toInt() }.toMutableList()
        return@map countErrors(l, true) ?: countErrors(l, false) ?: 0
    }.sum()
}

private fun countErrors(l: MutableList<Int>, increase: Boolean): Int? {
    var j = 0
    for (i in 1 until l.size) {
        if (!checkCrease(l, j, i, increase)) {
            //Check sublist with index i or j removed
            val opt1 = l.filterIndexed { index, _ -> index != j }.toList()
            val opt2 = l.filterIndexed { index, _ -> index != i }.toList()
            val opt3 = l.filterIndexed { index, _ -> index != i + 1 }.toList()
            return subChecks(opt1, increase)
            ?: subChecks(opt2, increase)
            ?: subChecks(opt3, increase)
        } else {
            j++
        }
    }
    return 1
}

//If return 1 this is ok
private fun subChecks(l: List<Int>, increase: Boolean): Int? {
    var j = 0
    for (i in 1 until l.size) {
        if (!checkCrease(l, j, i, increase)) {
            return null
        } else {
            j++
        }
    }
    return 1
}

fun checkCrease(l: List<Int>, index: Int, nextIndex: Int, increase: Boolean): Boolean =
    if (increase && l[index] < l[nextIndex]) {
        checkNext(l[index], l[nextIndex])
    } else if (!increase && l[index] > l[nextIndex]) {
        checkNext(l[index], l[nextIndex])
    } else {
        false
    }


fun checkNext(ref: Int, next: Int): Boolean = abs(ref - next).inAcceptableRange()

