fun main() {
    fun part1(input: List<String>): Int {
        var contained = 0
        for(line in input) {
            val (a, b) = line.split(",").map { getSections(it) }
            if(a.containsAll(b) || b.containsAll(a))
                contained += 1
        }

        return contained
    }

    fun part2(input: List<String>): Int {
        var overlap = 0
        for(line in input) {
            val (a, b) = line.split(",").map { getSections(it) }
            if(a.intersect(b).isNotEmpty())
                overlap += 1
        }

        return overlap
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

fun getSections(range: String): Set<Int> {
    val (lower, upper) = range.split("-").map { it.toInt() }
    return lower.rangeTo(upper).toSet()
}
