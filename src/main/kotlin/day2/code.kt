package day2

import java.io.File

const val day = 2
const val basePath = "./src/main/kotlin/day$day"
const val testPath1 = "${basePath}/test1"
const val inputPath1 = "${basePath}/input1"
const val testPath2 = "${basePath}/test2"
const val inputPath2 = "${basePath}/input2"

val gameRegex = "Game (\\d*)".toRegex()
val blueRegex = "(\\d*) blue".toRegex()
val redRegex = "(\\d*) red".toRegex()
val greenRegex = "(\\d*) green".toRegex()

fun main() {
    File(testPath1).readLines().asSequence().also { check(it.computeResult() == 8) }
    File(inputPath1).readLines().asSequence().also { println(it.computeResult()) }
    File(testPath2).readLines().asSequence().also { check(it.computeResult2() == 2286) }
    File(inputPath2).readLines().asSequence().also { println(it.computeResult2()) }

}

data class Colors(val blue: Int, val red: Int, val green: Int)

fun String.toColors() = Colors(
    blue = blueRegex.maxColor(this),
    red = redRegex.maxColor(this),
    green = greenRegex.maxColor(this)
)

fun Colors.getValidId(id: Int) = if (red <= 12 && green <= 13 && blue <= 14) {
    id
} else {
    0
}

fun Colors.product() = red * blue * green
private fun Sequence<String>.computeResult(): Int =
    map {
        it.toColors().getValidId(gameRegex.find(it)?.groupValues?.get(1)?.toInt()!!)
    }.sum()

private fun Sequence<String>.computeResult2(): Int = map { it.toColors().product() }.sum()

fun Regex.maxColor(line: String) = findAll(line).map { it.groupValues[1].toInt() }.max()



