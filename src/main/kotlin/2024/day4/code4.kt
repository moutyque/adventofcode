package `2024`.day4

import java.io.File

const val day = 4
val prefix = "./src/main/kotlin/2024/day$day"
const val EXPECT_WORD = "XMAS"
fun main() {

    val actual = File("$prefix/test").readLines().asSequence().filter { it.isNotBlank() }.compute41()
    require(actual == 18)
    File("${prefix}/1").readLines().asSequence().compute41().let { println(it) }
    val actual2 = File("${prefix}/test").readLines().asSequence().filter { it.isNotBlank() }.compute42()
    println(actual2)
    require(actual2 == 9)
    File("${prefix}/1").readLines().asSequence().compute42().let { println(it) }
}

//Upper x - 1
//Lower x + 1
//Left y - 1
//Right y + 1
fun Sequence<String>.compute42(): Int {
    val grid = this.map { it.map { c -> c } }.toList()
    var sum = 0
    //Check 4 direction
    for (i in grid.indices) {
        for (j in grid[0].indices) {
            if (grid[i][j] == 'A') {
                //Upper right &  Lower left
                sum += when {
                    checkCase(grid, i - 1, j + 1, 'M') == 1 && checkCase(grid, i + 1, j - 1, 'S') == 1 -> 1
                    checkCase(grid, i - 1, j + 1, 'S') == 1 && checkCase(grid, i + 1, j - 1, 'M') == 1 -> 1
                    else -> 0
                } *
                        when {
                            checkCase(grid, i - 1, j - 1, 'M') == 1 && checkCase(grid, i + 1, j + 1, 'S') == 1 -> 1
                            checkCase(grid, i - 1, j - 1, 'S') == 1 && checkCase(grid, i + 1, j + 1, 'M') == 1 -> 1
                            else -> 0
                        }
            }
        }
    }
    return sum
}

fun checkCase(
    grid: List<List<Char>>,
    x: Int,
    y: Int,
    expectedChar: Char
): Int {
    //Reach limit
    if (grid.size == x || x < 0) {
        return 0
    }
    if (grid[x].size == y || y < 0) {
        return 0
    }
    //Do check
    return if (grid[x][y] == expectedChar) {
        1

    } else {
        0
    }

}

//Grid is line then column
//So X is to Down
//Y is to right
fun Sequence<String>.compute41(): Int {
    val grid = this.map { it.map { c -> c } }.toList()
    var sum = 0
    //Check 8 direction
    for (i in grid.indices) {
        for (j in grid[0].indices) {
            //Rigth
            sum += checkDirection(grid, i, j, 0, { return@checkDirection it }, { return@checkDirection it + 1 }) +
                    //Left
                    checkDirection(grid, i, j, 0, { return@checkDirection it }, { return@checkDirection it - 1 }) +
                    //Up
                    checkDirection(grid, i, j, 0, { return@checkDirection it - 1 }, { return@checkDirection it }) +
                    //Down
                    checkDirection(grid, i, j, 0, { return@checkDirection it + 1 }, { return@checkDirection it }) +

                    //Upper right
                    checkDirection(grid, i, j, 0, { return@checkDirection it - 1 }, { return@checkDirection it + 1 }) +
                    //Lower right
                    checkDirection(grid, i, j, 0, { return@checkDirection it + 1 }, { return@checkDirection it + 1 }) +
                    //Upper left
                    checkDirection(grid, i, j, 0, { return@checkDirection it - 1 }, { return@checkDirection it - 1 }) +
                    //Lower left
                    checkDirection(grid, i, j, 0, { return@checkDirection it + 1 }, { return@checkDirection it - 1 })
        }
    }
    return sum
}

fun checkDirection(
    grid: List<List<Char>>,
    x: Int,
    y: Int,
    expectedCharIdx: Int,
    xTransform: (x: Int) -> Int,
    yTransform: (y: Int) -> Int,
): Int {
    //Word found
    if (expectedCharIdx >= EXPECT_WORD.length) {
        return 1
    }
    //Reach limit
    if (grid.size == x || x < 0) {
        return 0
    }
    if (grid[x].size == y || y < 0) {
        return 0
    }
    //Do check
    val expectedChar = EXPECT_WORD[expectedCharIdx]
    return if (grid[x][y] == expectedChar) {
        checkDirection(grid, xTransform.invoke(x), yTransform.invoke(y), expectedCharIdx + 1, xTransform, yTransform)
    } else {
        0
    }

}
