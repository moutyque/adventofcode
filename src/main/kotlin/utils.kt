import java.io.File

fun File.prepare() = this.readLines().asSequence().filter { it.isNotBlank() }
fun Pair<Int, Int>.plus(b: Pair<Int, Int>) = Pair(this.first + b.first, this.second + b.second)
typealias Line = String
fun Sequence<String>.buildGrid(): List<List<String>> = this.map { line ->
    line.split("").filter { it.isNotBlank() }.toList()
}.toList()

fun Sequence<String>.buildIntGrid(): MutableList<MutableList<Int>> = this.map { line ->
    line.split("").filter { it.isNotBlank() }.map { it.toInt() }.toMutableList()
}.toMutableList()


val up = Pair(-1, 0)
val down = Pair(1, 0)
val right = Pair(0, 1)
val left = Pair(0, -1)
val directions = listOf(up, right, down, left)

fun <T: Any> List<List<T>>.getValue(position: Pair<Int, Int>)= this[position.first][position.second]

class LinkedListK<T:Any> {
    var head : NodeDataClass<T>? = null
    var tail : NodeDataClass<T>? = null
    var size = 0


    private fun isEmpty(): Boolean = size == 0

    override fun toString(): String {
        return if (isEmpty()){
            "Empty List"
        }else{
            head.toString()
        }
    }

    fun push(value : T):LinkedListK<T> = apply{
        head = NodeDataClass(value = value, nextNode = head)
        if (tail == null){
            tail = head
        }
        size ++
    }

    fun append(value: T):LinkedListK<T> = apply{
        if (isEmpty()){
            push(value)
            return this
        }
        val newNode = NodeDataClass(value = value)
        tail!!.nextNode = newNode
        tail= newNode
        size++
    }

    fun nodeAt(index:Int): NodeDataClass<T>?{
        var currentNode = head
        var currentIndex = 0
        while (currentNode != null && currentIndex <index){
            currentNode= currentNode.nextNode
            currentIndex++
        }
        return currentNode
    }

    fun insert(value : T , afterNode: NodeDataClass<T>): NodeDataClass<T>{
        if (tail == afterNode){
            append(value)
            return tail!!
        }
        val newNode = NodeDataClass(value = value , nextNode = afterNode.nextNode)
        afterNode.nextNode = newNode
        size++

        return newNode
    }
}

//here we are trying to print the linked list in arrow format for better
// readability
data class NodeDataClass<T:Any>(
    var value: T,
    var nextNode: NodeDataClass<T>? = null
){
    /*
    This function overrides the default toString method provided by Kotlin data classes.

     It recursively generates a string representation of the linked list,
     starting from the current node. If the nextNode is not null,
     it appends the value of the current node followed by an arrow (->) and then calls toString on the nextNode.
     If nextNode is null, it simply returns the value of the current node.
    */
    override fun toString(): String {
        return if (nextNode != null){
            "$value -> ${nextNode.toString()}"
        }else{
            "$value"
        }
    }
}
