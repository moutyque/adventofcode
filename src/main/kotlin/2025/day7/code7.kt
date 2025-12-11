package `2025`.day7

import java.io.File


const val path = "./src/main/kotlin/2025/day7"

fun main() {
    File("$path/test").readLines().asSequence().compute71().also { println(it) } //21
    File("$path/input").readLines().asSequence().compute71().also { println(it) } //1609
    File("$path/test").readLines().asSequence().compute72().also { println(it) }  //40
    File("$path/input").readLines().asSequence().compute72().also { println(it) }  //11744693538946
}
fun Sequence<String>.compute71(): Int {
    val grid = this.map { it.toCharArray().toMutableList() }.toMutableList()
    var col = 0
    for(c in 0 until grid[0].size) {
        if(grid[0][c] == 'S'){
            col = c
            break
        }
    }
    return findSplit(grid,col,1,grid[0].indices,grid.indices)
    }

fun findSplit(grid: MutableList<MutableList<Char>>, c: Int, r: Int, cRange: IntRange, rRange: IntRange) :Int{
    if(c !in cRange) return 0
    if(r !in rRange) return 0
    return when(grid[r][c]){
        '.' -> {
            grid[r][c] =  '|'
            findSplit(grid,c,r+1,cRange,rRange)
        }
        '^' -> {
            findSplit(grid,c-1,r,cRange,rRange) + findSplit(grid,c+1,r,cRange,rRange) + 1
        }
        '|' -> 0
        else ->  0
    }
}

fun Sequence<String>.compute72(): Long {
    val grid = this.map { it.toCharArray().map { it.toString() }.toTypedArray() }.toList().toTypedArray()
    var col = 0
    for(c in 0 until grid[0].size) {
        if(grid[0][c] == "S"){
            col = c
            break
        }
    }
    return  findPaths(grid,col,1,grid[0].indices,grid.indices)
}

fun findPaths(
    grid: Array<Array<String>>,
    c: Int,
    r: Int,
    cRange: IntRange,
    rRange: IntRange
): Long {
    if(c !in cRange) return 1
    if(r !in rRange) return 1
    return when(val cell = grid[r][c]){
        "." -> {
            grid[r][c] =  "|"
            findPaths(grid,c,r+1,cRange,rRange).also {
                grid[r][c] = "$it"
            }

        }
        "^" -> {
            (findPaths(grid,c-1,r,cRange,rRange) + findPaths(grid,c+1,r,cRange,rRange)).also {
                grid[r][c] = "$it"
            }
        }
        "|" -> 0
        else ->  cell.toLong()
    }
}




