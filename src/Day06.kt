fun main() {
    fun markerLocation(input: String, size: Int): Int {
        input.windowed(size).forEachIndexed { index, s ->
            if(s.toCharArray().toSet().size == size) {
                return index + s.length
            }
        }
        throw IllegalArgumentException("Marker not found")
    }

    fun part1(input: String) = markerLocation(input, 4)
    fun part2(input: String) = markerLocation(input, 14)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    testInput.zip(arrayOf(7, 5, 6, 10, 11)).forEach { (line, expected) ->
        check(part1(line) == expected)
    }
    testInput.zip(arrayOf(19, 23, 23, 29, 26)).forEach { (line, expected) ->
        check(part2(line) == expected)
    }

    val input = readInput("Day06")
    println(part1(input.first()))
    println(part2(input.first()))
}
