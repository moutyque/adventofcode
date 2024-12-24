package `2024`.day13

import Position
import plus
import java.io.File
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

const val day = 13

val prefix = "./src/main/kotlin/2024/day$day"
val pattern = """Button A: X\+(?<xa>\d+), Y\+(?<ya>\d+)\nButton B: X\+(?<xb>\d+), Y\+(?<yb>\d+)
Prize: X=(?<X>\d+), Y=(?<Y>\d+)""".toRegex()

fun main() {
    File("$prefix/test").readText().split("\n\n").let { compute131(it) }.also { println(it) }
        .also { require(it == 480L) }
    File("$prefix/input").readText().split("\n\n").let { compute131(it) }.also { println(it) }
    File("$prefix/input").readText().split("\n\n").let { compute132(it) }.also { println(it) }.also { require(it > 1261380303L) }


}

private fun compute131(s: List<String>) = s.sumOf { s ->
    val matcher = pattern.findAll(s)
    // Check if there is a match and print the named groups
    matcher.map {
        val (A, B, prize) = parce(it)
        compute(A, B, target = prize.run { first.toLong() to second.toLong() }).let { it.first * 3 + it.second }
    }.sum()
}

private fun compute132(s: List<String>) = s.sumOf { s ->
    val matcher = pattern.findAll(s)
    // Check if there is a match and print the named groups
    matcher.map {
        val (A, B, prize) = parce(it)
        compute(
            A,
            B,
            target = prize.run { first + 10000000000000 to second + 10000000000000 },
            max = null
        ).let { it.first * 3L + it.second }
    }.sum()
}

private fun parce(it: MatchResult): Triple<Pair<Int, Int>, Pair<Int, Int>, Pair<Int, Int>> {
    val A = it.groups["xa"]!!.value.toInt() to it.groups["ya"]!!.value.toInt()
    val B = it.groups["xb"]!!.value.toInt() to it.groups["yb"]!!.value.toInt()
    val prize = it.groups["X"]!!.value.toInt() to it.groups["Y"]!!.value.toInt()
    return Triple(A, B, prize)
}


fun compute(A: Position, B: Position, target: Pair<Long, Long>, max: Int? = 100): Pair<Long, Long> {
    // Calculate the determinant
    val determinant = A.first * B.second - B.first * A.second

    // Check if the determinant is zero (matrix is singular)
    if (determinant == 0) {
        println("The matrix is singular, no unique solution exists.")
        return 0L to 0L
    }

    // Calculate the inverse of the matrix
    val invDet = BigDecimal.ONE.divide(BigDecimal(determinant), MathContext.DECIMAL128)
    val inverseMatrix = arrayOf(
        arrayOf(B.second.toBigDecimal().times(invDet), -B.first.toBigDecimal().times(invDet)),
        arrayOf(-A.second.toBigDecimal().times(invDet), A.first.toBigDecimal().times(invDet))
    )

    // Calculate A and B
    val A =
        (inverseMatrix[0][0] * target.first.toBigDecimal() + inverseMatrix[0][1] * target.second.toBigDecimal()).setScale(
            3,
            RoundingMode.HALF_EVEN
        )
    val B =
        (inverseMatrix[1][0] * target.first.toBigDecimal() + inverseMatrix[1][1] * target.second.toBigDecimal()).setScale(
            3,
            RoundingMode.HALF_EVEN
        )
    if (!isIntegerValue(A)) {
        return 0L to 0L
    }
    if (!isIntegerValue(B)) {
        return 0L to 0L
    }
    if (max?.let { A.toInt() > it || B.toInt() > it} == true) {
        return 0L to 0L
    }
    return A.toLong() to B.toLong()
}

private fun isIntegerValue(bd: BigDecimal): Boolean {
    return bd.stripTrailingZeros().scale() <= 0
}

fun reach(A: Position, B: Position, current: Position, target: Position, iteration: Int, cost: Int): Int? {
    if (iteration == 100) {
        return null
    }
    if (current == target) {
        return 0
    }
    return current.plus(A).let { new ->
        if (new.first < target.first && new.second < target.second) {
            reach(A, B, new, target, iteration + 1, cost + 3)
        } else {
            null
        }
    } ?: current.plus(B).let { new ->
        if (new.first < target.first && new.second < target.second) {
            reach(A, B, new, target, iteration + 1, cost + 1)
        } else {
            null
        }
    }
}
