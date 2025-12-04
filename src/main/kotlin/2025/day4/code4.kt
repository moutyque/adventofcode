package `2025`.day4

import java.io.File


const val path = "./src/main/kotlin/2025/day4"
fun main() {
    File("$path/test").readLines().asSequence().compute41().also { println(it) }//13
    File("$path/input").readLines().asSequence().compute41().also { println(it) } //17535
    File("$path/test").readLines().asSequence().compute42().also { println(it) }//43
    File("$path/input").readLines().asSequence().compute42().also { println(it) }//43
}
fun Sequence<String>.compute41(): Int {
    val grid = this.map { line ->
        line.split("").drop(1).dropLast(1)
    }.toList()
    var ok = 0
    val rSize = grid.size
    val cSize = grid[0].size
    for(r in grid.indices){
        for(c in grid[r].indices){
            var counter = 0

            if (grid[r][c] != "@") continue

            if(c-1 >= 0 && grid[r][c-1] == "@") counter++ //R
            if(c+1 < cSize && grid[r][c+1] == "@") counter++ //L
            if(r-1 >= 0 && grid[r-1][c] == "@") counter++ //U
            if(r+1 < rSize && grid[r+1][c] == "@") counter++ //D

            if(c-1 >= 0 && r-1 >= 0 && grid[r-1][c-1] == "@") counter++ //UL
            if(c-1 >= 0 &&  r+1 < rSize && grid[r+1][c-1] == "@") counter++ //DL
            if(c+1 < cSize &&  r-1 >= 0 && grid[r-1][c+1] == "@") counter++ //UR
            if(c+1 < cSize && r+1 < rSize && grid[r+1][c+1] == "@") counter++ //DR

            if (counter < 4) ok++
        }
    }
    return ok
}

//Brut force
//Other is two have a second array to mark the number of adjacent stoll, when we remove one we drop adjacent by 1
fun Sequence<String>.compute42(): Int {
    val grid = this.map { line ->
        line.split("").drop(1).dropLast(1).toMutableList()
    }.toMutableList()
    var runCount = 0
    var overallCount = 0
    val rSize = grid.size
    val cSize = grid[0].size
    do {
        runCount = 0
        for(r in grid.indices){
            for(c in grid[r].indices){
                var counter = 0

                if (grid[r][c] != "@") continue

                if(c-1 >= 0 && grid[r][c-1] == "@") counter++ //R
                if(c+1 < cSize && grid[r][c+1] == "@") counter++ //L
                if(r-1 >= 0 && grid[r-1][c] == "@") counter++ //U
                if(r+1 < rSize && grid[r+1][c] == "@") counter++ //D

                if(c-1 >= 0 && r-1 >= 0 && grid[r-1][c-1] == "@") counter++ //UL
                if(c-1 >= 0 &&  r+1 < rSize && grid[r+1][c-1] == "@") counter++ //DL
                if(c+1 < cSize &&  r-1 >= 0 && grid[r-1][c+1] == "@") counter++ //UR
                if(c+1 < cSize && r+1 < rSize && grid[r+1][c+1] == "@") counter++ //DR

                if (counter < 4){
                    runCount++
                    grid[r][c] = "X"
                }
            }
        }
        overallCount += runCount
    }while (runCount > 0)

    return overallCount
}

