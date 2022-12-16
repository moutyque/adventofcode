import java.io.File

fun main() {
    val iterator = File("./src/main/kotlin/day1/input").readLines().iterator()
    day11(iterator)
    day12(iterator)
}

fun day11(iterator: Iterator<String>) {
    var first = 0
    var currentValue = 0
    var line = ""
    while (iterator.hasNext()) {
        with(iterator.next()) {
            when (this) {
                "" -> {
                    first = maxOf(first, currentValue)
                    currentValue = 0
                }

                else -> {
                    currentValue += this.toInt()
                }
            }
        }
    }

}

fun day12(iterator: Iterator<String>) {
    var currentValue = 0
    val elves = mutableListOf<Int>()
    while (iterator.hasNext()) {
        with(iterator.next()) {
            when (this.isEmpty()) {
                true -> {
                    elves.add(currentValue)
                    elves.sortDescending()
                    if (elves.size > 3) println(elves[0] + elves[1] + elves[2])
                    currentValue = 0
                }

                false -> {
                    currentValue += this.toInt()
                }
            }
        }
    }

}
