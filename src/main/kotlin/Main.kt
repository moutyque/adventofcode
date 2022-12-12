import kotlin.math.abs

fun main() {
    day102()
}

fun day102() {
    var line = readln()
    var X = 1
    var cycle = 0
    var position = 1
    val sb = StringBuilder()
    fun updateCycle() {

        if (position == cycle || position + 1 == cycle || position - 1 == cycle) {
            sb.append("#")
        } else {
            sb.append(".")
        }
        cycle++
        cycle %= 40
    }
    while (line.isNotEmpty()) {
        when {
            line.startsWith("noop") -> {
                updateCycle()
            }

            line.startsWith("addx") -> {
                updateCycle()
                updateCycle()
                position += line.removePrefix("addx ").toInt()
            }
        }
        line = readln()
    }
    sb.forEachIndexed { index, c -> if (index % 40 == 0) print("\n$c") else print(c) }
}

fun day101() {
    var line = readln()
    var X = 1
    var cycle = 0
    var score = 0
    fun Int.updateScore(strength: Int) = if (((this - 20) % 40) == 0) {
        this * strength
    } else 0
    while (line.isNotEmpty()) {
        when {
            line.startsWith("noop") -> {
                cycle++
                score += cycle.updateScore(X)
            }

            line.startsWith("addx") -> {
                cycle++
                score += cycle.updateScore(X)
                cycle++
                score += cycle.updateScore(X)
                X += line.removePrefix("addx ").toInt()
            }
        }
        line = readln()
    }
    println(score)
}

fun day92() {
    var line = readln()
    val positions = mutableSetOf<Pair<Int, Int>>()
    val rope = mutableListOf<Pair<Int, Int>>().apply {
        repeat(10) {
            add(Pair(0, 0))
        }
    }
    positions.add(rope.first())
    fun Pair<Int, Int>.isNeighbors(p: Pair<Int, Int>): Boolean =
        abs(this.first - p.first) <= 1 && abs(this.second - p.second) <= 1

    fun Pair<Int, Int>.add(pair: Pair<Int, Int>): Pair<Int, Int> = Pair(first + pair.first, second + pair.second)
    fun Pair<Int, Int>.move(pair: Pair<Int, Int>): Pair<Int, Int> = when {
        pair.first == first && pair.second == second -> {
            this
        }

        pair.first == first -> {
            if (pair.second > second) Pair(first, second + 1) else Pair(first, second - 1)
        }

        pair.second == second -> {
            if (pair.first > first) Pair(first + 1, second) else Pair(first - 1, second)
        }

        pair.first > first && pair.second > second -> Pair(first + 1, second + 1)
        pair.first > first && pair.second < second -> Pair(first + 1, second - 1)
        pair.first < first && pair.second > second -> Pair(first - 1, second + 1)
        pair.first < first && pair.second < second -> Pair(first - 1, second - 1)
        else -> this
    }

    while (line.isNotBlank()) {
        line.split(" ").apply {
            repeat(this.last().toInt()) {
                when (this.first()) {
                    "U" -> Pair(0, 1)
                    "D" -> Pair(0, -1)
                    "L" -> Pair(-1, 0)
                    "R" -> Pair(1, 0)
                    else -> throw Error()
                }.also { transformation ->
                    rope[0] = rope[0].add(transformation)
                    for (i in 1 until rope.size) {
                        if (!rope[i - 1].isNeighbors(rope[i])) {
                            rope[i] = rope[i].move(rope[i - 1])
                            if (i == rope.size - 1) {
                                positions.add(rope[i])
                            }
                        }
                    }
                }
            }
        }
        line = readln()
    }
    println(positions.size)
}

fun day91() {
    var line = readln()
    val positions = mutableSetOf<Pair<Int, Int>>()
    var H = Pair(0, 0)
    var T = Pair(0, 0)
    positions.add(T)
    fun Pair<Int, Int>.isNeighbors(p: Pair<Int, Int>): Boolean =
        abs(this.first - p.first) <= 1 && abs(this.second - p.second) <= 1

    while (line.isNotBlank()) {
        line.split(" ").apply {
            repeat(this.last().toInt()) {
                when (this.first()) {
                    "U" -> Pair(H.first, H.second + 1)
                    "D" -> Pair(H.first, H.second - 1)
                    "L" -> Pair(H.first - 1, H.second)
                    "R" -> Pair(H.first + 1, H.second)
                    else -> throw Error()
                }.also {
                    val oldH = H
                    H = it
                    if (!T.isNeighbors(H)) {
                        T = oldH
                        positions.add(T)
                    }
                }
            }
        }

        line = readln()
    }
    println(positions.size)
}

fun day82() {
    val trees = mutableListOf<List<Int>>()
    var line = readln()
    while (line.isNotEmpty()) {
        trees.add(line.toCharArray().map { it.code - 48 })
        line = readln()
    }

    fun Int.visibleTreeOnLeft(r: Int, c: Int, currentScore: Int = 1): Int =
        when {
            c < 0 -> currentScore - 1
            trees[r][c] >= this -> currentScore
            else -> visibleTreeOnLeft(r, c - 1, currentScore + 1)
        }

    fun Int.visibleTreeOnRight(r: Int, c: Int, currentScore: Int = 1): Int =
        when {
            c > trees.first().size - 1 -> currentScore - 1
            trees[r][c] >= this -> currentScore
            else -> visibleTreeOnRight(r, c + 1, currentScore + 1)
        }


    fun Int.visibleTreeOnTop(r: Int, c: Int, currentScore: Int = 1): Int =
        when {
            r < 0 -> currentScore - 1
            trees[r][c] >= this -> currentScore
            else -> visibleTreeOnTop(r - 1, c, currentScore + 1)
        }

    fun Int.visibleTreeButtom(r: Int, c: Int, currentScore: Int = 1): Int =
        when {
            r > trees.size - 1 -> currentScore - 1
            trees[r][c] >= this -> currentScore
            else -> visibleTreeButtom(r + 1, c, currentScore + 1)
        }

    var visibleInside = 0
    for (column in 1..trees.first().size - 2) {
        for (row in 1..trees.size - 2) {
            visibleInside = maxOf(
                visibleInside, trees[row][column].run {
                    visibleTreeOnLeft(row, column - 1) * visibleTreeOnRight(row, column + 1) * visibleTreeOnTop(
                        row - 1,
                        column
                    ) * visibleTreeButtom(row + 1, column)

                })

        }
    }
    println(visibleInside)

}

fun day81() {
    val trees = mutableListOf<List<Int>>()
    var line = readln()
    while (line.isNotEmpty()) {
        trees.add(line.toCharArray().map { it.code - 48 })
        line = readln()
    }
    fun Int.visibleOnLeftTree(r: Int, c: Int): Boolean =
        when {
            c < 0 -> true
            trees[r][c] >= this -> false
            else -> visibleOnLeftTree(r, c - 1)
        }

    fun Int.visibleOnRightTree(r: Int, c: Int): Boolean =
        when {
            c > trees.first().size - 1 -> true
            trees[r][c] >= this -> false
            else -> visibleOnRightTree(r, c + 1)
        }


    fun Int.visibleOnTopTree(r: Int, c: Int): Boolean =
        when {
            r < 0 -> true
            trees[r][c] >= this -> false
            else -> visibleOnTopTree(r - 1, c)
        }

    fun Int.visibleOnBottomTree(r: Int, c: Int): Boolean =
        when {
            r > trees.size - 1 -> true
            trees[r][c] >= this -> false
            else -> visibleOnBottomTree(r + 1, c)
        }

    val visibleEdges = trees.first().size + trees.last().size + (trees.size * 2) - 4
    var visibleInside = 0
    for (column in 1..trees.first().size - 2) {
        for (row in 1..trees.size - 2) {
            trees[row][column].apply {
                visibleInside += when (visibleOnLeftTree(row, column - 1) || visibleOnRightTree(
                    row,
                    column + 1
                ) || visibleOnTopTree(row - 1, column) || visibleOnBottomTree(row + 1, column)) {
                    true -> 1
                    false -> 0
                }
            }

        }
    }
    println(visibleEdges + visibleInside)

}

fun day72() {
    var line = readln()

    data class Dir(val name: String, val size: Int = 0, val subDirs: MutableCollection<Dir> = mutableListOf()) {
        fun computeSize(): Int = size + subDirs.sumOf { it.computeSize() }
    }

    val dirs = HashMap<String, Dir>()
    var currentKey = ""
    while (line.isNotEmpty()) {
        when {
            line.startsWith("$") -> {
                when {
                    line.startsWith("$ cd") && !line.endsWith("..") -> {
                        currentKey += line.removePrefix("$ cd ").run { if (currentKey.isEmpty()) this else "/$this" }
                        dirs.computeIfAbsent(currentKey) { Dir(currentKey) }
                    }

                    line.startsWith("$ cd ..") -> {
                        currentKey = currentKey.substringBeforeLast("/")
                    }
                }
            }

            else -> {
                line.split(" ").run {
                    when {
                        this.first() == "dir" -> {
                            val key = "$currentKey/${this.last()}"
                            Dir(key).also {
                                dirs[currentKey]!!.subDirs.add(it)
                                dirs.putIfAbsent(key, it)
                            }
                        }

                        else -> {
                            dirs[currentKey]!!.subDirs.add(Dir(this.last(), this.first().toInt()))
                        }
                    }
                }
            }
        }
        line = readln()
    }
    val freeSpace = 70_000_000 - dirs["/"]!!.computeSize()
    println(dirs.values.filter { freeSpace + it.computeSize() > 30_000_000 }.minBy { it.computeSize() }.computeSize())
}

fun day71() {
    var line = readln()

    data class Dir(val name: String, val size: Int = 0, val subDirs: MutableCollection<Dir> = mutableListOf()) {
        fun computeSize(): Int = size + subDirs.sumOf { it.computeSize() }
    }

    val dirs = HashMap<String, Dir>()
    var currentKey = ""
    while (line.isNotEmpty()) {
        when {
            line.startsWith("$") -> {
                when {
                    line.startsWith("$ cd") && !line.endsWith("..") -> {
                        currentKey += line.removePrefix("$ cd ").run { if (currentKey.isEmpty()) this else "/$this" }
                        dirs.computeIfAbsent(currentKey) { Dir(currentKey) }
                    }

                    line.startsWith("$ cd ..") -> {
                        currentKey = currentKey.substringBeforeLast("/")
                    }
                }
            }

            else -> {
                line.split(" ").run {
                    when {
                        this.first() == "dir" -> {
                            val key = "$currentKey/${this.last()}"
                            Dir(key).also {
                                dirs[currentKey]!!.subDirs.add(it)
                                dirs.putIfAbsent(key, it)
                            }
                        }

                        else -> {
                            dirs[currentKey]!!.subDirs.add(Dir(this.last(), this.first().toInt()))
                        }
                    }
                }
            }
        }
        line = readln()
    }
    println(dirs.values.filter { it.computeSize() <= 100_000 }.sumOf { it.computeSize() })
}

fun day62() {
    var line = readln()
    var score = 0
    while (line.isNotEmpty()) {
        for (i in 0 until line.length - 4) {
            if (line.substring(i, i + 14).run {
                    this.length == this.toSet().count()
                }) {
                score += i + 14
                break
            }
        }
        line = readln()
    }
    println(score)

}

fun day61() {
    var line = readln()
    var score = 0
    while (line.isNotEmpty()) {
        for (i in 0 until line.length - 4) {
            if (line.substring(i, i + 4).run {
                    this.length == this.toSet().count()
                }) {
                score += i + 4
                break
            }
        }
        line = readln()
    }
    println(score)

}

fun day52() {
    //https://adventofcode.com/2022/day/5/input
    val stacks = List(9) {
        mutableListOf<Char>()
    }.toMutableList()
    val reg = """move (?<nb>\d+) from (?<from>\d) to (?<to>\d)""".toRegex()
    var line: String
    var counter = 0
    while (counter < 2) {
        line = readln()
        if (line.isEmpty()) {
            counter++
        } else {
            counter = 0
        }
        if (line.startsWith("move")) {
            val groups = reg.matchEntire(line)!!.groups as MatchNamedGroupCollection
            val (new, last) = stacks[groups["from"]!!.value.toInt() - 1].run {
                val index = this.size - groups["nb"]!!.value.toInt()
                Pair(subList(0, index), subList(index, size))
            }
            stacks[groups["from"]!!.value.toInt() - 1] = new
            stacks[groups["to"]!!.value.toInt() - 1].addAll(
                last
            )
        } else if (line.startsWith(" 1")) {
            stacks.forEach { it.reverse() }
        } else {
            line.chunked(4).map { it.trim() }.forEachIndexed { index, s ->
                if (s.isNotBlank()) stacks[index].add(s[1])
            }
        }
    }
    stacks.filterNotNull().forEachIndexed { index, chars -> print(chars.last()) }
}

fun day51() {
    //https://adventofcode.com/2022/day/5/input
    val stacks = List(9) {
        mutableListOf<Char>()
    }.toMutableList()
    val reg = """move (?<nb>\d+) from (?<from>\d) to (?<to>\d)""".toRegex()
    var line: String
    var counter = 0
    while (counter < 2) {
        line = readln()
        if (line.isEmpty()) {
            counter++
        } else {
            counter = 0
        }
        if (line.startsWith("move")) {
            val groups = reg.matchEntire(line)!!.groups as MatchNamedGroupCollection
            val (new, last) = stacks[groups["from"]!!.value.toInt() - 1].run {
                val index = this.size - groups["nb"]!!.value.toInt()
                Pair(subList(0, index), subList(index, size))
            }
            stacks[groups["from"]!!.value.toInt() - 1] = new
            stacks[groups["to"]!!.value.toInt() - 1].addAll(
                last.reversed()
            )
        } else if (line.startsWith(" 1")) {
            stacks.forEach { it.reverse() }
        } else {
            line.chunked(4).map { it.trim() }.forEachIndexed { index, s ->
                if (s.isNotBlank()) stacks[index].add(s[1])
            }
        }
    }
    stacks.filterNotNull().forEachIndexed { index, chars -> print(chars.last()) }
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
    val firstMap = mapOf("A" to "Rock", "B" to "Paper", "C" to "Scissors")
    val secondMap = mapOf("X" to "Rock", "Y" to "Paper", "Z" to "Scissors")
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
    val firstMap = mapOf("A" to "Rock", "B" to "Paper", "C" to "Scissors")
    val secondMap = mapOf("X" to "Lose", "Y" to "Draw", "Z" to "Win")
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

fun day41() {
    var line = readln()
    var score = 0
    while (line.isNotBlank()) {

        val (r1, r2) = line.split(",").run {
            Pair(this.first().split("-").run {
                (this.first().toInt()..this.last().toInt()).toList()
            }, this.last().split("-").run {
                (this.first().toInt()..this.last().toInt()).toList()
            })
        }

        if (r1.containsAll(r2) || r2.containsAll(r1)) {
            score += 1
        }
        line = readln()
    }
    println(score)
}

fun day42() {
    var line = readln()
    var score = 0
    while (line.isNotBlank()) {

        val (r1, r2) = line.split(",").run {
            Pair(this.first().split("-").run {
                (this.first().toInt()..this.last().toInt())
            }, this.last().split("-").run {
                (this.first().toInt()..this.last().toInt())
            })
        }

        for (i in r1) {
            if (i in r2) {
                score += 1
                break
            }
        }
        line = readln()
    }
    println(score)
}
