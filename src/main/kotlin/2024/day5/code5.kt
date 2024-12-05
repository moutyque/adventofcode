package `2024`.day5

import java.io.File

const val day = 5

val prefix = "./src/main/kotlin/2024/day$day"

fun main() {

    val actual = File("$prefix/test").readLines().asSequence().filter { it.isNotBlank() }.compute51()
    println(actual)
    require(actual == 143)
    File("${prefix}/1").readLines().asSequence().compute51().let { println(it) }
    val actual2 = File("${prefix}/test").readLines().asSequence().filter { it.isNotBlank() }.compute52()
    println(actual2)
    require(actual2 == 123)
    File("${prefix}/1").readLines().asSequence().compute52().let { println(it) }
}

fun Sequence<String>.compute51(): Int {
    val rules = mutableMapOf<Int, MutableList<Int>>()
    val revertRules = mutableMapOf<Int, MutableList<Int>>()
    var sum = 0
    this.forEach { l ->
        if (l.contains("|")) {
            l.split("|").also {
                rules.computeIfAbsent(it[0].toInt()) {
                    mutableListOf()
                }.add(it[1].toInt())
                revertRules.computeIfAbsent(it[1].toInt()) {
                    mutableListOf()
                }.add(it[0].toInt())
            }
        } else if (l.contains(",")) {
            val seq = l.split(",").map { it.toInt() }
            if ((seq.size - 1 downTo 0).map { checkOrder(rules, revertRules, seq, it) }.all { it }) {
                sum += seq[seq.size / 2]
            }

        }

    }
    return sum
}
fun Sequence<String>.compute52(): Int {
    val rules = mutableMapOf<Int, MutableList<Int>>()
    val revertRules = mutableMapOf<Int, MutableList<Int>>()
    var sum = 0
    this.forEach { l ->
        if (l.contains("|")) {
            l.split("|").also {
                rules.computeIfAbsent(it[0].toInt()) {
                    mutableListOf()
                }.add(it[1].toInt())
                revertRules.computeIfAbsent(it[1].toInt()) {
                    mutableListOf()
                }.add(it[0].toInt())
            }
        } else if (l.contains(",")) {
            val seq = l.split(",").map { it.toInt() }.toMutableList()
            val original = seq.toList()
            order(rules, revertRules, seq)
            if(original != seq){
                sum += seq[seq.size / 2]
            }

        }

    }
    return sum
}
fun order(rules: Map<Int, List<Int>>, revertRule: Map<Int, List<Int>>, seq: MutableList<Int>) {
    for(index in  seq.size - 1 downTo 0){
        for (i in index - 1 downTo 0) {
            val B = seq[index]
            val A = seq[i]
            if (rules[A] != null && B in rules[A]!!) {
                //This index is ok
                continue
            }
            revertRule[A]?.let {
                if (B in it) {
                    seq[i] = B
                    seq[index] = A
                    order(rules,revertRule,seq)
                }
            }

        }
    }

}
fun checkOrder(rules: Map<Int, List<Int>>, revertRule: Map<Int, List<Int>>, seq: List<Int>, index: Int): Boolean {
    val B = seq[index]
    for (i in index - 1 downTo 0) {
        val A = seq[i]
        if (rules[A] != null && B in rules[A]!!) {
            //This index is ok
            continue
        }
        revertRule[A]?.let {
            if (B in it) {
                return false
            }
        }

    }
    return true
}
