package `2025`.day3

import java.io.File


val path = "./src/main/kotlin/2025/day3"
fun main() {
    File("$path/test").readLines().asSequence().compute31().also { println(it) }//357
    File("$path/1").readLines().asSequence().compute31().also { println(it) } //17535
    File("$path/test").readLines().asSequence().compute32().also { println(it) }//3121910778619
    File("$path/1").readLines().asSequence().compute32().also { println(it) }//49046150754
}

fun Sequence<String>.compute31(): Int =
    this.map { line ->
        val maxIdx = line.dropLast(1).indices.maxBy { line[it].digitToInt() }
        val substring = line.substring(maxIdx+1)
        val idx = substring.indices.maxBy { substring[it].digitToInt() } + maxIdx + 1
        "${line[maxIdx]}${line[idx]}".toInt()
    }.sum()

fun Sequence<String>.compute32(): Long =
    this.map { line ->
        var start = 0
        val s = StringBuilder()
        for(i in 11 downTo 0){
            val end = line.length - i
            val idx = line.substring(start,end).let { sub ->
                sub.indices.maxBy { sub[it] }
            } + start
            s.append(line[idx])
            start = idx + 1
        }
    s.toString().toLong()
    }.sum()
