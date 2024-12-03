package `2024`.day3

import java.io.File

const val day = 3
val prefix = "./src/main/kotlin/2024/day$day"
fun main() {

    val actual = File("$prefix/test").readLines().asSequence().compute31()
    require(actual == 161)
    File("${prefix}/1").readLines().asSequence().compute31().let { println(it) }
    val actual2 = File("${prefix}/test2").readLines().asSequence().compute32()
    require(actual2 == 48)
    File("${prefix}/2").readLines().asSequence().compute32().let { println(it) }
}

val regex = """mul\((?<first>(\d){1,3}),(?<second>(\d){1,3})\)""".toRegex()
val regexDo = """do\(\)""".toRegex()
val regexDont = """(?<off>don't\(\))""".toRegex()
fun Sequence<String>.compute31() =

    this.map {
        regex.findAll(it).map { it.groups["first"]!!.value.toInt() * it.groups["second"]!!.value.toInt() }.sum()
    }.sum()


fun Sequence<String>.compute32(): Int {
    var isOn = true
    var sum = 0
    val seq: Sequence<Pair<Int, Any>> =
        this.flatMap { regexDo.findAll(it).map { it.groups[0]!!.range.first to true } } +
                this.flatMap {
                    regexDont.findAll(it).map { it.groups[0]!!.range.first to false }
                } +
                this.flatMap {
                    regex.findAll(it)
                        .map { it.groups[0]!!.range.first to it.groups["first"]!!.value.toInt() + it.groups["second"]!!.value.toInt() }
                }
    seq.sortedBy { it.first }.forEach {
        when {
            it.second == true -> isOn = true
            it.second == false -> isOn = false
            it.second is Int && isOn -> sum += it.second as Int
        }
    }
    return sum


}

