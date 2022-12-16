import java.io.File

fun main() {
    val iterator = File("./src/main/kotlin/day3/input").readLines().iterator()

    day31(iterator)
    day32(iterator)
}


fun day32(iterator: Iterator<String>) {
    val scores = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    var score = 0
    while (iterator.hasNext()) {
        val e1 = iterator.next()
        val e2 = readln()
        val e3 = readln()
        for (c in listOf(e1, e2, e3).minBy { it.length }) {
            if (c in e1 && c in e2 && c in e3) {
                score += scores.indexOf(c) + 1
                break
            }
        }
    }
    println(score)


}

fun day31(iterator: Iterator<String>) {
        val scores = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var score = 0
        while (iterator.hasNext()) {
            val (first, second) = iterator.next().chunked(iterator.next().length / 2)
            for (c in first) {
                if (c in second) {
                    score += scores.indexOf(c) + 1
                    break
                }
            }
        }
        println(score)
}
