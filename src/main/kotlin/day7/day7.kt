package day7

import java.io.File

fun main() {
    val iterator = File("./src/main/kotlin/day7/input").readLines().iterator()

    day71(iterator)
    day72(iterator)
}

data class Dir(val name: String, val size: Int = 0, val subDirs: MutableCollection<Dir> = mutableListOf()) {
    fun computeSize(): Int = size + subDirs.sumOf { it.computeSize() }
}

fun day72(iterator: Iterator<String>) {
    val dirs =  compute(iterator)
    val freeSpace = 70_000_000 - dirs["/"]!!.computeSize()
    println(dirs.values.filter { freeSpace + it.computeSize() > 30_000_000 }.minBy { it.computeSize() }.computeSize())
}

fun day71(iterator: Iterator<String>) {

    val dirs =  compute(iterator)
    println(dirs.values.filter { it.computeSize() <= 100_000 }.sumOf { it.computeSize() })
}

private fun compute(
    iterator: Iterator<String>

): HashMap<String, Dir> {
    val dirs = HashMap<String, Dir>()
    var currentKey = ""
    while (iterator.hasNext()) {
        val line = iterator.next()
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
    }
    return dirs
}

