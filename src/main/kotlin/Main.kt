fun main(args: Array<String>) {

}

fun day11() {
    //https://adventofcode.com/2022/day/1
    var first = 0
    var currentValue = 0
    var line = ""
    do {
        line = readln()
        with(line) {
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
    } while (line != "\n")

}

fun day12() {
    //https://adventofcode.com/2022/day/1
    var currentValue = 0
    var line = ""
    val elves = mutableListOf<Int>()
    do {
        line = readln()
        with(line) {
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
    } while (line != "\n")

}


