package day11

import java.math.BigInteger

fun main() {

    day111()
    day112()
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
