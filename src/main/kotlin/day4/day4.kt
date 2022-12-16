package day4

import java.io.File

fun main(){
    val iterator = File("./src/main/kotlin/day4/input").readLines().iterator()
    day41(iterator)
    day42(iterator)
}


fun day41(iterator: Iterator<String>) {
    var score = 0
    while (iterator.hasNext()) {

        val (r1, r2) = iterator.next().split(",").run {
            Pair(this.first().split("-").run {
                (this.first().toInt()..this.last().toInt()).toList()
            }, this.last().split("-").run {
                (this.first().toInt()..this.last().toInt()).toList()
            })
        }

        if (r1.containsAll(r2) || r2.containsAll(r1)) {
            score += 1
        }
    }
    println(score)
}

fun day42(iterator: Iterator<String>) {
    var score = 0
    while (iterator.hasNext()) {

        val (r1, r2) = iterator.next().split(",").run {
            Pair(this.first().split("-").run {
                (this.first().toInt()..this.last().toInt())
            }, this.last().split("-").run {
                (this.first().toInt()..this.last().toInt())
            })
        }

        for (i in r1) {
            if (i in r2) {
                score += 1
                break
            }
        }
    }
    println(score)
}
