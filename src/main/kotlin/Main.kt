import java.math.BigInteger
import kotlin.math.abs

fun main() {
    day131()
}

typealias Level = Int

typealias Value = Int
fun day131() {
    var counter = 0
    var score = 0

    fun List<String>.buildLevel(): MutableList<Int> {
        var level = 0
        val levels = mutableListOf<Int>()
        for (c in this) {
            when (c) {
                "[" -> {
                    level++
                }

                "]" -> {
                    level--
                }
            }
            levels.add(level)
        }
        return levels
    }

    fun String.isNumeric(): Boolean {
        return this.toDoubleOrNull() != null
    }

    fun String.clean(): Pair<List<Value>, MutableList<Level>> =
        this.replace("[]", "[-1]")
            .split(",")
            .flatMap { it.split("""((?=\])|(?<=\[))""".toRegex()) }
            .run {
                mapIndexed { index, c -> if (c.isNumeric()) index else -1 }
                    .filter { it > 0 }
                    .let { indexes ->
                        Pair(
                            indexes.map { this[it].toInt() },
                            indexes.mapTo(mutableListOf()) { buildLevel()[it] })
                    }
            }


    fun MutableList<Int>.updateLevel(newLevel: Int, refLevel: Int, startIndex: Int) {
        var tmpIdx = startIndex
        while (tmpIdx < this.size && this[tmpIdx] == refLevel) {
            this[tmpIdx] = newLevel
            tmpIdx++
        }
    }

    var isContinue = true
    while (isContinue) {
        counter++
        val (left, leftLevels) = readln().apply {
            if (isEmpty()) isContinue = false
        }.clean()

        val (right, rightLevels) = readln().apply {
            if (isEmpty()) isContinue = false
        }.clean()

        if (!isContinue) break
        var index = 0
        var currentResult = true
        while (index < left.size) {
            if (leftLevels[index] == rightLevels[index]) {
                if (left[index] < right[index]) {
                    currentResult = true
                    break
                } else if (left[index] > right[index]) {
                    currentResult = false
                    break
                } else {
                    index++
                    if (index >= right.size) {
                        currentResult = false
                        break
                    }
                }
            } else if (leftLevels[index] > rightLevels[index]) {
                rightLevels.updateLevel(leftLevels[index], rightLevels[index], index)
            } else if (leftLevels[index] < rightLevels[index]) {
                leftLevels.updateLevel(rightLevels[index], leftLevels[index], index)
            }
        }

        readln()
        if (currentResult) score += counter
        println("$counter: $currentResult -> $score")
    }

    println(score)

}
data class Node(val x: Int, val y: Int, val height: Int) {
    val edges: MutableList<Edge> = mutableListOf()
}

data class Edge(val length: Int, val to: Node)

fun day122() {
    var line = readln()
    val maze = mutableListOf<String>()
    while (line.isNotEmpty()) {
        maze.add(line)
        line = readln()
    }

    val convertor = "abcdefghijklmnopqrstuvwxyz"
    val distances = mutableMapOf<Node, Long>()
    var end: Node? = null
    for (r in maze.indices) {
        for (c in maze[r].indices) {
            when (maze[r][c]) {
                'S' -> {
                    distances[Node(c, r, 0)] = Long.MAX_VALUE - 1
                }

                'E' -> {
                    Node(c, r, 25).apply {
                        distances[this] = 0
                        end = this
                    }
                }

                else -> distances[Node(c, r, convertor.indexOf(maze[r][c]))] = Long.MAX_VALUE - 1
            }
        }
    }
    for (node in distances.keys) {
        node.edges.addAll(distances.keys.asSequence().filter { node.height <= it.height + 1 }
            .filter { (it.x == node.x && (it.y + 1 == node.y || it.y - 1 == node.y)) || (it.y == node.y && (it.x + 1 == node.x || it.x - 1 == node.x)) }
            .map { Edge(1, it) }
            .toList())
    }

    val remaining = distances.keys.toMutableList()
    while (remaining.isNotEmpty()) {
        val node: Node = remaining.minBy { distances[it]!! } //take the vertex with the shortest path to the start
        remaining.remove(node) //remove it from vertex to process
        for (v in node.edges) {
            // Relaxation
            //For each edges linked to this vertex check
            // if the current distance to start + edge length is lesser than the distance store to the destination vertex
            if (distances[v.to]!! > distances[node]!! + v.length) { //If so
                distances[v.to] = distances[node]!! + v.length // Update destination vertex with lesser value
            }
        }
    }
    println(distances[distances.keys.filter { it.height == 0 }.minBy { distances[it]!! }])
}

fun day121() {
    var line = readln()
    val maze = mutableListOf<String>()
    while (line.isNotEmpty()) {
        maze.add(line)
        line = readln()
    }

    val convertor = "abcdefghijklmnopqrstuvwxyz"
    val distances = mutableMapOf<Node, Long>()
    var end: Node? = null
    for (r in maze.indices) {
        for (c in maze[r].indices) {
            when (maze[r][c]) {
                'S' -> {
                    distances[Node(c, r, 0)] = 0
                }

                'E' -> {
                    Node(c, r, 25).apply {
                        distances[this] = Long.MAX_VALUE - 1
                        end = this
                    }
                }

                else -> distances[Node(c, r, convertor.indexOf(maze[r][c]))] = Long.MAX_VALUE - 1
            }
        }
    }
    for (node in distances.keys) {
        node.edges.addAll(distances.keys.asSequence().filter { it.height <= node.height + 1 }
            .filter { (it.x == node.x && (it.y + 1 == node.y || it.y - 1 == node.y)) || (it.y == node.y && (it.x + 1 == node.x || it.x - 1 == node.x)) }
            .map { Edge(1, it) }
            .toList())
    }

    val remaining = distances.keys.toMutableList()
    while (remaining.isNotEmpty()) {
        val node: Node = remaining.minBy { distances[it]!! } //take the vertex with the shortest path to the start
        remaining.remove(node) //remove it from vertex to process
        for (v in node.edges) {
            // Relaxation
            //For each edges linked to this vertex check
            // if the current distance to start + edge length is lesser than the distance store to the destination vertex
            if (distances[v.to]!! > distances[node]!! + v.length) { //If so
                distances[v.to] = distances[node]!! + v.length // Update destination vertex with lesser value
            }
        }
    }
    println(distances[end])
}

fun day112() {
    data class Monkey(
        val items: MutableList<Long>,
        val operation: (worry: Long) -> Long,
        val test: Long,
        val first: Int,
        val second: Int,
        var counter: Int = 0
    ) {
        fun test(worry: Long) = if (worry % test == 0L) first else second
    }


    val monkeys = mutableListOf<Monkey>().apply {
        add(Monkey(mutableListOf(85, 77, 77), { worry ->
            worry * 7
        }, 19, 6, 7))
        add(Monkey(mutableListOf(80, 99), { worry ->
            worry * 11
        }, 3, 3, 5))
        add(Monkey(mutableListOf(74, 60, 74, 63, 86, 92, 80), { worry ->
            worry + 8
        }, 13, 0, 6))
        add(Monkey(mutableListOf(71, 58, 93, 65, 80, 68, 54, 71), { worry ->
            worry + 7
        }, 7, 2, 4))

        add(Monkey(mutableListOf(97, 56, 79, 65, 58), { worry ->
            worry + 5
        }, 5, 2, 0))

        add(Monkey(mutableListOf(77), { worry ->
            worry + 4
        }, 11, 4, 3))

        add(Monkey(mutableListOf(99, 90, 84, 50), { worry ->
            worry * worry
        }, 17, 7, 1))

        add(Monkey(mutableListOf(50, 66, 61, 92, 64, 78), { worry ->
            worry + 3
        }, 2, 5, 1))
    }
    val reducer = monkeys.map { it.test }.reduce(Long::times)
    repeat(10000) {
        for (monkey in monkeys) {
            for (item in monkey.items) {
                monkey.counter++
                val tmp = monkey.operation(item) % reducer
                monkeys[monkey.test(tmp)].items.apply {
                    add(this.lastIndex + 1, tmp)
                }
            }
            monkey.items.clear()
        }
    }

    monkeys.sortByDescending { it.counter }
    println(BigInteger.valueOf(monkeys[0].counter.toLong()).multiply(BigInteger.valueOf(monkeys[1].counter.toLong())))

}

fun day111() {
    data class Monkey(
        val items: MutableList<Int>,
        val operation: (worry: Int) -> Int,
        val test: (worry: Int) -> Int,
        var counter: Int = 0
    )


    /*val monkeys = mutableListOf<Monkey>().apply {
        add(Monkey(mutableListOf(79, 98), { worry ->
            worry * 19
        }, { worry -> if (worry % 23 == 0) 2 else 3 }))
        add(Monkey(mutableListOf(54, 65, 75, 74), { worry ->
            worry + 6
        }, { worry -> if (worry % 19 == 0) 2 else 0 }))
        add(Monkey(mutableListOf(79, 60, 97), { worry ->
            worry * worry
        }, { worry -> if (worry % 13 == 0) 1 else 3 }))
        add(Monkey(mutableListOf(74), { worry ->
            worry + 3
        }, { worry -> if (worry % 17 == 0) 0 else 1 }))
    }*/

    val monkeys = mutableListOf<Monkey>().apply {
        add(Monkey(mutableListOf(85, 77, 77), { worry ->
            worry * 7
        }, { worry -> if (worry % 19 == 0) 6 else 7 }))
        add(Monkey(mutableListOf(80, 99), { worry ->
            worry * 11
        }, { worry -> if (worry % 3 == 0) 3 else 5 }))
        add(Monkey(mutableListOf(74, 60, 74, 63, 86, 92, 80), { worry ->
            worry + 8
        }, { worry -> if (worry % 13 == 0) 0 else 6 }))
        add(Monkey(mutableListOf(71, 58, 93, 65, 80, 68, 54, 71), { worry ->
            worry + 7
        }, { worry -> if (worry % 7 == 0) 2 else 4 }))

        add(Monkey(mutableListOf(97, 56, 79, 65, 58), { worry ->
            worry + 5
        }, { worry -> if (worry % 5 == 0) 2 else 0 }))

        add(Monkey(mutableListOf(77), { worry ->
            worry + 4
        }, { worry -> if (worry % 11 == 0) 4 else 3 }))

        add(Monkey(mutableListOf(99, 90, 84, 50), { worry ->
            worry * worry
        }, { worry -> if (worry % 17 == 0) 7 else 1 }))

        add(Monkey(mutableListOf(50, 66, 61, 92, 64, 78), { worry ->
            worry + 3
        }, { worry -> if (worry % 2 == 0) 5 else 1 }))

    }

    repeat(20) {
        for (monkey in monkeys) {
            for (item in monkey.items) {
                monkey.counter++
                val tmp = monkey.operation(item)
                monkeys[monkey.test(tmp)].items.apply {
                    add(this.lastIndex + 1, tmp)
                }
            }
            monkey.items.clear()
        }
    }

    monkeys.sortByDescending { it.counter }
    println(monkeys[0].counter * monkeys[1].counter)

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

    fun Int.visibleTreeOnLeft(r: Int, c: Int, currentScore: Int = 1): Int = when {
        c < 0 -> currentScore - 1
        trees[r][c] >= this -> currentScore
        else -> visibleTreeOnLeft(r, c - 1, currentScore + 1)
    }

    fun Int.visibleTreeOnRight(r: Int, c: Int, currentScore: Int = 1): Int = when {
        c > trees.first().size - 1 -> currentScore - 1
        trees[r][c] >= this -> currentScore
        else -> visibleTreeOnRight(r, c + 1, currentScore + 1)
    }


    fun Int.visibleTreeOnTop(r: Int, c: Int, currentScore: Int = 1): Int = when {
        r < 0 -> currentScore - 1
        trees[r][c] >= this -> currentScore
        else -> visibleTreeOnTop(r - 1, c, currentScore + 1)
    }

    fun Int.visibleTreeButtom(r: Int, c: Int, currentScore: Int = 1): Int = when {
        r > trees.size - 1 -> currentScore - 1
        trees[r][c] >= this -> currentScore
        else -> visibleTreeButtom(r + 1, c, currentScore + 1)
    }

    var visibleInside = 0
    for (column in 1..trees.first().size - 2) {
        for (row in 1..trees.size - 2) {
            visibleInside = maxOf(visibleInside, trees[row][column].run {
                visibleTreeOnLeft(row, column - 1) * visibleTreeOnRight(row, column + 1) * visibleTreeOnTop(
                    row - 1, column
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
    fun Int.visibleOnLeftTree(r: Int, c: Int): Boolean = when {
        c < 0 -> true
        trees[r][c] >= this -> false
        else -> visibleOnLeftTree(r, c - 1)
    }

    fun Int.visibleOnRightTree(r: Int, c: Int): Boolean = when {
        c > trees.first().size - 1 -> true
        trees[r][c] >= this -> false
        else -> visibleOnRightTree(r, c + 1)
    }


    fun Int.visibleOnTopTree(r: Int, c: Int): Boolean = when {
        r < 0 -> true
        trees[r][c] >= this -> false
        else -> visibleOnTopTree(r - 1, c)
    }

    fun Int.visibleOnBottomTree(r: Int, c: Int): Boolean = when {
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
                    row, column + 1
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


fun day21() {/*
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

fun day22() {/*
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
