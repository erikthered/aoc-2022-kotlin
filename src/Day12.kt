import java.lang.IllegalArgumentException
import kotlin.math.absoluteValue

fun main() {


    fun part1(input: List<String>): Int {
        val grid: List<CharArray> = input.map { it.toCharArray() }

//        grid.forEach { r ->
//            println(r.joinToString(""))
//        }

        var y = grid.indexOfFirst { it.contains('S') }
        var x = grid[y].indexOfFirst { it == 'S' }

        val visited = mutableListOf<Loc>()

        var elevation = 'a'.code

        println("Starting location: ($x, $y)")

        var done = false

        while(!done) {
            visited.add(x to y)

            val candidates = mutableListOf<Pair<Loc, Int>>()

            if(x != grid.first().lastIndex) candidates.add((x+1 to y) to grid[y][x+1].code)
            if(x != 0) candidates.add((x-1 to y) to grid[y][x-1].code)
            if(y != grid.lastIndex) candidates.add((x to y+1) to grid[y+1][x].code)
            if(y != 0) candidates.add((x to y-1) to grid[y-1][x].code)

            println(candidates)

            if(candidates.any { it.second  == 'E'.code} && (elevation == 'y'.code || elevation == 'z'.code)) {
                done = true
            } else {

                candidates.removeIf { visited.contains(it.first) || (elevation - it.second).absoluteValue > 1 }

                x = candidates.first().first.first
                y = candidates.first().first.second

                println("Move to: ($x, $y)")

                elevation = grid[y][x].code

                println("New elevation: $elevation")

                candidates.clear()
            }
        }

        println("Visited: ${visited.size}")


        return visited.size+1
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)

//    val input = readInput("Day12")
//    println(part1(input))
//    println(part2(input))
}

typealias Loc = Pair<Int, Int>
