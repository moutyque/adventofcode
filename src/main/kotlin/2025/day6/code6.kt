package `2025`.day6

import java.io.File


const val path = "./src/main/kotlin/2025/day6"

fun main() {
    File("$path/test").readLines().asSequence().compute61().also { println(it) } //4277556
    File("$path/input").readLines().asSequence().compute61().also { println(it) } //5361735137219
    File("$path/test2").readLines().asSequence().compute62().also { println(it) } //3263827
    File("$path/input2").readLines().asSequence().compute62().also { println(it) } //11744693538946
}
fun Sequence<String>.compute61(): Long {
    val grid = this.map { it.split("\\s+".toRegex()).filter { it.isNotBlank() }.map { it.trim() } }.toList()
    var results = 0L
    for(c in 0 until grid[0].size){
        val symbol = grid[grid.indices.last][c]
        val numbers = mutableListOf<Long>()
        for(r in 0 until  grid.size - 1){
            numbers.add(grid[r][c].toLong())
        }
        results += numbers.reduce { a,b ->
            when{
                symbol == "*" -> a * b
                symbol == "+" -> a+b
                else -> throw IllegalStateException("Unknown symbol $symbol")
            }
        }
    }

    return results
    }



fun Sequence<String>.compute62(): Long {
    val grid = this.map { it.replace("\\s".toRegex(),"0") }.toList()
    var results = 0L
    var symbol = ""
    val numbers = mutableListOf<Long>()
    val sb = StringBuilder()
    for(c in 0 until grid[0].length){
        for(r in 0 until  grid.size){
            if(r < grid.size - 1){
                sb.append(grid[r][c].toString())
            }else if(r == grid.size - 1 && grid[r][c] != '0'){
                if(symbol != ""){
                    results += if(symbol =="+") numbers.sum() else numbers.reduce { a,b -> a * b }
                    numbers.clear()
                }
                symbol = grid[r][c].toString()
            }

        }
        sb.toString().trim { it == '0' }.takeIf { it.isNotBlank() }?.also { numbers.add(it.toLong()) }
        sb.clear()
    }
    results += if(symbol =="+") numbers.sum() else numbers.reduce { a,b -> a * b }
    return results
}

