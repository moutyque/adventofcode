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


fun day21() {
    /*
    Rock: A,X
    Paper: B, Z
    Scissors: C,Y
     */

    var line = readln()
    var score = 0
    val firstMap = mapOf("A" to "Rock","B" to "Paper","C" to "Scissors")
    val secondMap = mapOf("X" to "Rock","Y" to "Paper","Z" to "Scissors")
    while (line.isNotBlank()) {
        val (first, second) = line.split(" ").run {
            Pair(firstMap[this.first()], secondMap[this.last()])
        }
        score += when (first) {
            "Rock" -> { //Rock
                when (second) {
                    "Rock" -> 1 + 3
                    "Paper" -> 2 + 6
                    "Scissors" -> 3 + 0
                    else -> 0
                }
            }
            "Paper" -> {
                when (second) {
                    "Rock" -> 1 + 0
                    "Paper" -> 2 + 3
                    "Scissors" -> 3 + 6
                    else -> 0
                }
            }
            "Scissors" -> {
                when (second) {
                    "Rock" -> 1 + 6
                    "Paper" -> 2 + 0
                    "Scissors" -> 3 + 3
                    else -> 0
                }
            }
            else -> 0
        }
        line = readln()
    }
    println(score)
}

fun day22() {
    /*
    Rock: A,X
    Paper: B, Z
    Scissors: C,Y
     */

    var line = readln()
    var score = 0
    val firstMap = mapOf("A" to "Rock","B" to "Paper","C" to "Scissors")
    val secondMap = mapOf("X" to "Lose","Y" to "Draw","Z" to "Win")
    while (line.isNotBlank()) {
        val (first, second) = line.split(" ").run {
            Pair(firstMap[this.first()], secondMap[this.last()])
        }
        // Rock 1
        // Paper 2
        // Scissors 3
        score += when (first) {
            "Rock" -> { //Rock
                when (second) {
                    "Lose" -> 0 + 3
                    "Draw" -> 3 + 1
                    "Win" -> 6 + 2
                    else -> 0
                }
            }
            "Paper" -> {
                when (second) {
                    "Lose" -> 0 + 1
                    "Draw" -> 3 + 2
                    "Win" -> 6 + 3
                    else -> 0
                }
            }
            "Scissors" -> {
                when (second) {
                    "Lose" -> 0 + 2
                    "Draw" -> 3 + 3
                    "Win" -> 6 + 1
                    else -> 0
                }
            }
            else -> 0
        }
        line = readln()
    }
    println(score)
}

fun day32() {
    val scores = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    var line = readln()
    var score = 0
    while (line.isNotBlank()) {
        val e1 = line
        val e2 = readln()
        val e3 = readln()
        for (c in listOf(e1, e2, e3).minBy { it.length }) {
            if (c in e1 && c in e2 && c in e3) {
                score += scores.indexOf(c) + 1
                break
            }
        }
        line = readln()
    }
    println(score)


}

fun day31() {
    fun main() {
        val scores = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var line = readln()
        var score = 0
        while (line.isNotBlank()) {
            val (first, second) = line.chunked(line.length / 2)
            for (c in first) {
                if (c in second) {
                    score += scores.indexOf(c) + 1
                    break
                }
            }
            line = readln()
        }
        println(score)


    }
}

