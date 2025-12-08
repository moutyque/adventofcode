package `2025`.day5

import java.io.File


const val path = "./src/main/kotlin/2025/day5"

fun main() {
    File("$path/test").readLines().asSequence().compute51().also { println(it) } //3
    File("$path/input").readLines().asSequence().compute51().also { println(it) } //652
    File("$path/test").readLines().asSequence().compute52().also { println(it) }//14
    File("$path/input").readLines().asSequence().compute52().also { println(it) }//341753674214273
}
     fun Sequence<String>.compute51(): Int {
        val ranges = mutableListOf<LongRange>()
        return this.map {
            when {
                it.contains("-") -> {
                    it.split("-").let {
                        ranges.add(LongRange(it.first().toLong(), it.last().toLong()))
                    }
                    0
                }

                it.isBlank() -> {
                    //do nothing
                    0
                }

                else -> {
                    if (ranges.any { r -> r.contains(it.toLong()) }) {
                        1
                    } else {
                        0
                    }
                }
            }
        }.sum()
    }



fun Sequence<String>.compute52(): Long {
    val ranges = this.filter { it.contains("-") }.map {
        it.split("-").let { LongRange(it.first().toLong(), it.last().toLong()) }
    }.toList()
    val sorted : List<LongRange> = ranges.sortedBy { it.first }
    val mergedRanges = mutableListOf<LongRange>()
    var i = 0
    while (i < sorted.size -1){
        var current = sorted[i]
        var j = i+1
        while (j < sorted.size && current.last >= sorted[j].first){
            current = LongRange(current.first,sorted[j].last.coerceAtLeast(current.last))
            j++
            i++
        }
        i++
        mergedRanges.add(current)
    }
    return mergedRanges.sumOf { it.last - it.first + 1 }
}


