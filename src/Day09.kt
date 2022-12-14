import kotlin.math.absoluteValue

fun main() {
    data class Point(var x: Int = 0, var y: Int = 0) {
        fun isTouching(other: Point) =
            ((this.x - other.x).absoluteValue <=1 && (this.y - other.y).absoluteValue <=1)
    }

    fun follow(curr: Point, next: Point) {
        if(!next.isTouching(curr)) {
            when {
                curr.x == next.x && curr.y > next.y -> next.y++
                curr.x == next.x && curr.y < next.y -> next.y--
                curr.x > next.x && curr.y == next.y  -> next.x++
                curr.x < next.x && curr.y == next.y -> next.x--
                else -> {
                    when {
                        curr.x > next.x && curr.y > next.y -> {
                            next.x++
                            next.y++
                        }
                        curr.x > next.x && curr.y < next.y -> {
                            next.x++
                            next.y--
                        }
                        curr.x < next.x && curr.y > next.y -> {
                            next.x--
                            next.y++
                        }
                        curr.x < next.x && curr.y < next.y -> {
                            next.x--
                            next.y--
                        }
                    }
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val tailVisited = mutableSetOf<Point>()

        val head = Point(0, 0)
        val tail = Point(0, 0)

        tailVisited.add(tail.copy())

        var totalMoves = 0

        for(move in input) {
            val (dir, amt) = move.split(" ").let { (d, a) -> Direction.valueOf(d) to a.toInt()}

            totalMoves += amt

            repeat(amt) {
                when(dir) {
                    Direction.R -> head.x++
                    Direction.L -> head.x--
                    Direction.U -> head.y++
                    Direction.D -> head.y--
                }
                follow(head, tail)
                tailVisited.add(tail.copy())
            }
        }
        
        return tailVisited.size
    }

    fun part2(input: List<String>): Int {
        val ropeSize = 10
        val knots = arrayListOf<Point>()
        repeat(ropeSize){ knots.add(Point()) }

        val tailVisited = mutableSetOf<Point>()

        for(move in input) {
            val (dir, amt) = move.split(" ").let { (d, a) -> Direction.valueOf(d) to a.toInt() }

            repeat(amt) {
                when(dir) {
                    Direction.R -> knots.first().x++
                    Direction.L -> knots.first().x--
                    Direction.U -> knots.first().y++
                    Direction.D -> knots.first().y--
                }
                knots.windowed(2).forEach { (curr, next) ->
                    follow(curr, next)
                }
                tailVisited.add(knots.last().copy())
            }
        }

        return tailVisited.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    val test2Input = readInput("Day09_test2")
    check(part2(test2Input) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

enum class Direction{
    R,
    L,
    U,
    D
}
