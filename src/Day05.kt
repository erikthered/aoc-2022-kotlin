fun main() {
    fun part1(input: List<String>): String {
        var stacks = arrayListOf<ArrayDeque<Char>>()
        var layout = true
        for(line in input) {
            if(layout) {
                if(line.isBlank()) {
                    layout = false
                }
                val cols = line.windowed(3, 4)
                cols.forEachIndexed { idx, c ->
                    val index = idx + 1
                    if(index > stacks.size) {
                        stacks.add(ArrayDeque())
                    }
                    if (!c[1].isWhitespace() && !c[1].isDigit()) {
                        stacks[idx].addFirst(c[1])
                    }
                }
            } else {
                val segments = line.split(" ")
                val quantity = segments[1].toInt()
                val source = segments[3].toInt()
                val dest = segments[5].toInt()

                repeat(quantity) {
                    stacks[dest-1].addLast(stacks[source-1].removeLast())
                }
            }
        }

        return stacks.map { it.last() }.joinToString("")
    }

    fun part2(input: List<String>): String {
        var stacks = arrayListOf<ArrayDeque<Char>>()
        var layout = true
        for(line in input) {
            if(layout) {
                if(line.isBlank()) {
                    layout = false
                }
                val cols = line.windowed(3, 4)
                cols.forEachIndexed { idx, c ->
                    val index = idx + 1
                    if(index > stacks.size) {
                        stacks.add(ArrayDeque())
                    }
                    if (!c[1].isWhitespace() && !c[1].isDigit()) {
                        stacks[idx].addFirst(c[1])
                    }
                }
            } else {
                val segments = line.split(" ")
                val quantity = segments[1].toInt()
                val source = segments[3].toInt()
                val dest = segments[5].toInt()

                val payload = arrayListOf<Char>()
                repeat(quantity) {
                    payload.add(stacks[source-1].removeLast())
                }
                payload.reversed().forEach {
                    stacks[dest-1].addLast(it)
                }

            }
        }

        return stacks.map { it.last() }.joinToString("")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
