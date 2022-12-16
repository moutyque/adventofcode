package day6

import java.io.File

fun main() {
    val iterator = File("./src/main/kotlin/day6/input").readLines().iterator()

    day61(iterator)
    day62(iterator)
}

fun day62(iterator: Iterator<String>) {
    var score = 0
    while (iterator.hasNext()) {
        val line = iterator.next()
        for (i in 0 until line.length - 4) {
            if (line.substring(i, i + 14).run {
                    this.length == this.toSet().count()
                }) {
                score += i + 14
                break
            }
        }
    }
    println(score)

}

fun day61(iterator: Iterator<String>) {
    var score = 0
    while (iterator.hasNext()) {
        val line = iterator.next()
        for (i in 0 until line.length - 4) {
            if (line.substring(i, i + 4).run {
                    this.length == this.toSet().count()
                }) {
                score += i + 4
                break
            }
        }
    }
    println(score)

}
