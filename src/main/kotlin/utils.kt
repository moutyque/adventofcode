import java.io.File

fun File.prepare() = this.readLines().asSequence().filter { it.isNotBlank() }
fun Pair<Int, Int>.plus(b: Pair<Int, Int>) = Pair(this.first + b.first, this.second + b.second)
typealias Line = String
fun Sequence<String>.buildGrid(): List<List<String>> = this.map { line ->
    line.split("").filter { it.isNotBlank() }.toList()
}.toList()

fun Sequence<String>.buildIntGrid(): MutableList<MutableList<Int>> = this.map { line ->
    line.split("").filter { it.isNotBlank() }.map { it.toInt() }.toMutableList()
}.toMutableList()


val up = Pair(-1, 0)
val down = Pair(1, 0)
val right = Pair(0, 1)
val left = Pair(0, -1)
val directions = listOf(up, right, down, left)

fun <T: Any> List<List<T>>.getValue(position: Pair<Int, Int>)= this[position.first][position.second]

