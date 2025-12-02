import java.io.File
import kotlin.math.abs
import kotlin.math.sign

val path = "./src/main/kotlin/2025/day2"
fun main() {
    val regex1 =  Regex("""^(\d+)\1$""")
    val regex2 =  Regex("""^(\d+)\1+$""")
   File("$path/test").readLines().asSequence().compute2(regex1).also { println(it) }//1227775554
    File("$path/1").readLines().asSequence().compute2(regex1).also { println(it) } //38437576669
    File("$path/test").readLines().asSequence().compute2(regex2).also { println(it) }//4174379265
    File("$path/1").readLines().asSequence().compute2(regex2).also { println(it) }//49046150754
}

fun Sequence<String>.compute2(regex: Regex): Long =
    this.first().split(",").flatMap { range ->
        range.split("-").let { ids ->
            val numbers = mutableListOf<Long>()
           for(i in ids.first().toLong()..ids.last().toLong()){
               if(regex.matches(i.toString())){
                   numbers.add(i)
               }
           }
            numbers
        }
    }.sum()



