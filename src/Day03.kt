fun main() {
    fun part1(input: List<String>): Int {
        val pointMap = pointValues()
        var points = 0
        for(line in input) {
            val h1 = line.substring(0..line.lastIndex/2)
            val h2 = line.substring(line.lastIndex/2 + 1)
            val overlap = h1.toCharArray().intersect(h2.toCharArray().asIterable().toSet())
            points += overlap.sumOf { pointMap[it]!! }
        }
        return points
    }

    fun part2(input: List<String>): Int {
        val pointMap = pointValues()
        var points = 0
        input.windowed(3, 3).forEach { group ->
            val badge = group[0].toCharArray().intersect(group[1].toSet()intersect(group[2].toSet())).single()
            points += pointMap[badge]!!
        }

        return points
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

private fun pointValues(): Map<Char, Int> {
    var points = 1
    return ('a'.rangeTo('z') + 'A'.rangeTo('Z')).associate {
        val pair = it to points
        points += 1
        pair
    }
}

