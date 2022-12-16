import java.io.File

fun main() {
    val iterator = File("./src/main/kotlin/day2/input").readLines().iterator()

    day21(iterator)
    day22(iterator)
}


fun day21(iterator: Iterator<String>) {
    /*
    Rock: A,X
    Paper: B, Z
    Scissors: C,Y
     */

    var score = 0
    val firstMap = mapOf("A" to "Rock", "B" to "Paper", "C" to "Scissors")
    val secondMap = mapOf("X" to "Rock", "Y" to "Paper", "Z" to "Scissors")
    while (iterator.hasNext()) {
        val (first, second) = iterator.next().split(" ").run {
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
    }
    println(score)
}

fun day22(iterator: Iterator<String>) {/*
    Rock: A,X
    Paper: B, Z
    Scissors: C,Y
     */

    var score = 0
    val firstMap = mapOf("A" to "Rock", "B" to "Paper", "C" to "Scissors")
    val secondMap = mapOf("X" to "Lose", "Y" to "Draw", "Z" to "Win")
    while (iterator.hasNext()) {
        val (first, second) = iterator.next().split(" ").run {
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
    }
    println(score)
}
