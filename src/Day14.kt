fun main() {
    data class Point(var x: Int, var y: Int)

    fun generateRocks(input: List<String>): MutableSet<Pair<Int, Int>> {
        return input.flatMap { line ->
            line.split(" -> ")
                .map { it.split(",").let { (x, y) -> x.toInt() to y.toInt() } }
                .windowed(2, 1)
                .flatMap { (first, second) ->
                    if(first.first == second.first && first.second < second.second) {
                        (first.second..second.second).map { first.first to it }
                    } else if(first.first == second.first && first.second > second.second) {
                        (first.second downTo second.second).map { first.first to it }
                    } else if(first.second == second.second && first.first < second.first) {
                        (first.first..second.first).map { it to first.second}
                    } else {
                        (first.first downTo second.first).map { it to first.second}
                    }
                }
        }.toMutableSet()
    }

    fun part1(input: List<String>): Int {
        val occupied = generateRocks(input)

        val source = 500 to 0
        occupied.add(source)

        val leftBound = occupied.minBy { it.first }.first
        val rightBound = occupied.maxBy { it.first }.first
        val upperBound = occupied.maxBy { it.second }.second

        var sandAtRest = 0

        var done = false

        val sand = Point(500,0)

        while(!done) {

            // OOB
            if(sand.x < leftBound || sand.x > rightBound || sand.y > upperBound) {
                done = true
            }

            // At Rest
            if(occupied.containsAll(listOf(
                sand.x to sand.y+1,
                sand.x-1 to sand.y+1,
                sand.x+1 to sand.y+1
            ))) {
                occupied.add(sand.x to sand.y)
                sandAtRest++
                sand.x = 500
                sand.y = 0
            }

            // Down
            if(!occupied.contains(sand.x to sand.y+1)) {
                sand.y++
            } else if(!occupied.contains(sand.x-1 to sand.y+1)) {
                sand.y++
                sand.x--
            } else if(!occupied.contains(sand.x+1 to sand.y+1)) {
                sand.y++
                sand.x++
            }

        }

        return sandAtRest
    }

    fun part2(input: List<String>): Int {
        val occupied = generateRocks(input)

        val source = 500 to 0
        val upperBound = occupied.maxBy { it.second }.second + 2

        var sandAtRest = 0

        var done = false

        val sand = Point(500,0)

        while(!done) {

            if(occupied.contains(source)) {
                done = true
            }

            // At Rest
            if(occupied.containsAll(listOf(
                    sand.x to sand.y+1,
                    sand.x-1 to sand.y+1,
                    sand.x+1 to sand.y+1
                )) || sand.y == upperBound-1) {
                occupied.add(sand.x to sand.y)
                sandAtRest++
                sand.x = 500
                sand.y = 0
            } else {

                // Down
                if (!occupied.contains(sand.x to sand.y + 1)) {
                    sand.y++
                } else if (!occupied.contains(sand.x - 1 to sand.y + 1)) {
                    sand.y++
                    sand.x--
                } else if (!occupied.contains(sand.x + 1 to sand.y + 1)) {
                    sand.y++
                    sand.x++
                }
            }

        }

        return sandAtRest-1
    }

    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
