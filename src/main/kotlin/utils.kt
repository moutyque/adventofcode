import java.io.File

fun File.prepare() = this.readLines().asSequence().filter { it.isNotBlank() }
fun Pair<Int, Int>.plus(b: Pair<Int, Int>) = Pair(this.first + b.first, this.second + b.second)
typealias Line = String
fun Sequence<String>.buildGrid(): List<List<String>> = this.map { line ->
    line.split("").filter { it.isNotBlank() }.toList()
}.toList()
