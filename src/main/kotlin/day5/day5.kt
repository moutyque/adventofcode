package day5

import java.io.File

fun main() {
    val iterator = File("./src/main/kotlin/day5/input").readLines().iterator()

    day51(iterator)
    day52(iterator)
}

fun day52(iterator: Iterator<String>) {
    //https://adventofcode.com/2022/day/5/input
    val stacks = List(9) {
        mutableListOf<Char>()
    }.toMutableList()
    val reg = """move (?<nb>\d+) from (?<from>\d) to (?<to>\d)""".toRegex()
    var line: String
    var counter = 0
    while (counter < 2) {
        line = iterator.next()
        if (line.isEmpty()) {
            counter++
        } else {
            counter = 0
        }
        if (line.startsWith("move")) {
            val groups = reg.matchEntire(line)!!.groups as MatchNamedGroupCollection
            val (new, last) = stacks[groups["from"]!!.value.toInt() - 1].run {
                val index = this.size - groups["nb"]!!.value.toInt()
                Pair(subList(0, index), subList(index, size))
            }
            stacks[groups["from"]!!.value.toInt() - 1] = new
            stacks[groups["to"]!!.value.toInt() - 1].addAll(
                last
            )
        } else if (line.startsWith(" 1")) {
            stacks.forEach { it.reverse() }
        } else {
            line.chunked(4).map { it.trim() }.forEachIndexed { index, s ->
                if (s.isNotBlank()) stacks[index].add(s[1])
            }
        }
    }
    stacks.filterNotNull().forEachIndexed { index, chars -> print(chars.last()) }
}

fun day51(iterator: Iterator<String>) {
    //https://adventofcode.com/2022/day/5/input
    val stacks = List(9) {
        mutableListOf<Char>()
    }.toMutableList()
    val reg = """move (?<nb>\d+) from (?<from>\d) to (?<to>\d)""".toRegex()
    var line: String
    var counter = 0
    while (counter < 2) {
        line = iterator.next()
        if (line.isEmpty()) {
            counter++
        } else {
            counter = 0
        }
        if (line.startsWith("move")) {
            val groups = reg.matchEntire(line)!!.groups as MatchNamedGroupCollection
            val (new, last) = stacks[groups["from"]!!.value.toInt() - 1].run {
                val index = this.size - groups["nb"]!!.value.toInt()
                Pair(subList(0, index), subList(index, size))
            }
            stacks[groups["from"]!!.value.toInt() - 1] = new
            stacks[groups["to"]!!.value.toInt() - 1].addAll(
                last.reversed()
            )
        } else if (line.startsWith(" 1")) {
            stacks.forEach { it.reverse() }
        } else {
            line.chunked(4).map { it.trim() }.forEachIndexed { index, s ->
                if (s.isNotBlank()) stacks[index].add(s[1])
            }
        }
    }
    stacks.filterNotNull().forEachIndexed { index, chars -> print(chars.last()) }
}
