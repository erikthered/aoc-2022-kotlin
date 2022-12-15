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

    fun part1(input: List<String>): Int {
        val packets = input.windowed(2, 3).mapIndexed { packet, (left, right) ->
            packet to (parsePacketData(left).first to parsePacketData(right).first)
        }.toMap()

        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
//    check(part2(testInput) == 70)

//    val input = readInput("Day13")
//    println(part1(input))
//    println(part2(input))
}

