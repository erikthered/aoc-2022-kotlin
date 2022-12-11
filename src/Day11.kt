fun main() {
    fun part1(input: List<String>): Int {
        val instructionSets = input.windowed(6, 7)

        val monkeys = mutableListOf<Monkey>()
        instructionSets.forEach { i ->

            val (lhs, op, rhs) = i[2].substringAfter("= ").split(" ")
            val operation: (Long) -> Long = { old ->
                listOf(lhs, rhs).map { v ->
                    when (v) {
                        "old" -> old
                        else -> v.toLong()
                    }
                }.reduce { l, r ->
                    when (op) {
                        "*" -> l * r
                        "+" -> l + r
                        "-" -> l - r
                        "/" -> l / r
                        else -> throw RuntimeException()
                    }
                }
            }

            val testValue = i[3].substringAfterLast(" ").toLong()

            val throwObj: (Long) -> Unit = { t ->
                val dest = if (t % testValue == 0L) {
                    i[4].substringAfterLast(" ").toInt()
                } else {
                    i[5].substringAfterLast(" ").toInt()
                }
                monkeys[dest].items.addLast(t)
            }

            val initial = i[1].substringAfter(":").split(",").map { it.trim().toLong() }

            monkeys.add(Monkey(
                items = ArrayDeque(initial),
                operation = operation,
                throwObject = throwObj,
                test = testValue
            ))
        }

        val rounds = 20

        repeat(rounds) {
            monkeys.forEach { monkey ->
                while(monkey.items.size > 0) {
                    val o = monkey.items.removeFirst()
                    val new = monkey.operation(o)
                    monkey.inspections++
                    monkey.throwObject(new / 3)
                }
            }
        }

        monkeys.sortByDescending { it.inspections }

        return monkeys[0].inspections * monkeys[1].inspections
    }

    fun part2(input: List<String>): Long {
        val instructionSets = input.windowed(6, 7)

        val monkeys = mutableListOf<Monkey>()
        instructionSets.forEach { i ->

            val (lhs, op, rhs) = i[2].substringAfter("= ").split(" ")
            val operation: (Long) -> Long = { old ->
                listOf(lhs, rhs).map { v ->
                    when (v) {
                        "old" -> old
                        else -> v.toLong()
                    }
                }.reduce { l, r ->
                    when (op) {
                        "*" -> l * r
                        "+" -> l + r
                        "-" -> l - r
                        "/" -> l / r
                        else -> throw RuntimeException()
                    }
                }
            }

            val testValue = i[3].substringAfterLast(" ").toLong()

            val throwObj: (Long) -> Unit = { t ->
                val dest = if (t % testValue == 0L) {
                    i[4].substringAfterLast(" ").toInt()
                } else {
                    i[5].substringAfterLast(" ").toInt()
                }
                monkeys[dest].items.addLast(t)
            }

            val initial = i[1].substringAfter(":").split(",").map { it.trim().toLong() }

            monkeys.add(Monkey(
                items = ArrayDeque(initial),
                operation = operation,
                throwObject = throwObj,
                test = testValue
            ))
        }

        val rounds = 10000

        repeat(rounds) {
            monkeys.forEach { monkey ->
                val testProduct = monkeys.map { it.test }.reduce(Long::times)
                while(monkey.items.size > 0) {
                    val o = monkey.items.removeFirst()
                    val new = monkey.operation(o)
                    monkey.inspections++
                    monkey.throwObject(new % testProduct)
                }
            }
        }

        monkeys.sortByDescending { it.inspections }

        return monkeys[0].inspections.toLong() * monkeys[1].inspections.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605)
    check(part2(testInput) == 2713310158)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}

data class Monkey(
    val items: ArrayDeque<Long>,
    val test: Long,
    val operation: (Long) -> Long,
    val throwObject: (Long) -> Unit,
    var inspections: Int = 0
)

