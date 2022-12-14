
enum class Order {
    CORRECT,
    INCORRECT,
    UNDECIDED
}

fun main() {
    fun parsePacketData(line: String): Pair<List<Any>, Int> {
        val data = mutableListOf<Any>()
        var idx = 1
        var buffer = ""
        var done = false
        while(!done) {
            when (val c = line[idx]) {
                '[' -> {
                    val (nested, offset) = parsePacketData(line.substring(idx))
                    data.add(nested)
                    idx += offset
                }
                ']' -> {
                    if(buffer.isNotEmpty()) data.add(buffer.toInt())
                    buffer = ""
                    idx++
                    done = true
                }
                ',' -> {
                    if(buffer.isNotEmpty()) data.add(buffer.toInt())
                    buffer = ""
                    idx++
                }
                else -> {
                    buffer += c
                    idx++
                }
            }
        }
        return data to idx
    }


    fun checkOrder(left: Any, right: Any) : Order {
        when {
            left is Int && right is Int -> {
                return when {
                    left < right -> Order.CORRECT
                    left > right -> Order.INCORRECT
                    else -> Order.UNDECIDED
                }
            }
            left is List<*> && right is List<*> -> {
                left.mapIndexed { index, value ->
                    if (index > right.lastIndex) return Order.INCORRECT
                    val order = checkOrder(value!!, right[index]!!)
                    if(order != Order.UNDECIDED) return order
                }
                if(left.size < right.size) return Order.CORRECT
                return Order.UNDECIDED
            }
            left is Int && right is List<*> -> return checkOrder(listOf(left), right)
            left is List<*> && right is Int -> return checkOrder(left, listOf(right))
        }
        return Order.UNDECIDED
    }

    fun part1(input: List<String>): Int {
        val packets = input.windowed(2, 3).mapIndexed { packet, (left, right) ->
            packet to (parsePacketData(left).first to parsePacketData(right).first)
        }.toMap()

        val ordering = packets.mapValues { e -> checkOrder(e.value.first, e.value.second) }

        return ordering.filter { it.value == Order.CORRECT }.keys.sumOf { it + 1 }
    }

    class PacketComparator: Comparator<Any> {
        override fun compare(o1: Any?, o2: Any?): Int {
            if(o1 == null || o2 == null) return 0
            return when(checkOrder(o1, o2)) {
                Order.CORRECT -> -1
                Order.INCORRECT -> 1
                else -> 0
            }
        }
    }

    fun part2(input: List<String>): Int {
        val packets = input
            .asSequence()
            .plus("[[2]]").plus("[[6]]")
            .filter { it.isNotBlank() }
            .map { parsePacketData(it).first }
            .sortedWith(PacketComparator())
            .map { it.toString() }
            .toList()

        val div1 = packets.indexOfFirst { it == "[[2]]" } + 1
        val div2 = packets.indexOfFirst { it == "[[6]]" } + 1

        return div1 * div2
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}

