import java.lang.IllegalArgumentException
import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        var cycle = 0
        var register = 1

        var observe = 20
        var total = 0

        fun checkRegister() {
            if(cycle == observe) {
                total += (cycle * register)
                observe += 40
            }
        }

        for(line in input) {
            val segments = line.split(" ")
            when(val op = segments.first()) {
                "noop" -> {
                    cycle += 1
                    checkRegister()
                }

                "addx" -> {
                    val v = segments[1].toInt()
                    repeat(2) {
                        cycle += 1
                        checkRegister()
                    }
                    register += v
                }

                else -> {
                    throw IllegalArgumentException("Invalid operation: $op")
                }
            }
        }
        return total
    }

    fun part2(input: List<String>) {
        var cycle = 0
        var register = 1

        val display = mutableListOf<Char>()

        fun tick() {
            if((register - cycle).absoluteValue <= 1) {
                display.add('#')
            } else {
                display.add('.')
            }
            if(display.size % 40 == 0) {
                cycle = 0
            } else {
                cycle++
            }
        }

        for(line in input) {
            val segments = line.split(" ")
            when(val op = segments.first()) {
                "noop" -> {
                    tick()
                }

                "addx" -> {
                    val v = segments[1].toInt()
                    repeat(2) {
                        tick()
                    }
                    register += v
                }

                else -> {
                    throw IllegalArgumentException("Invalid operation: $op")
                }
            }
        }

        display.chunked(40).forEach { println(it.joinToString("")) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
    part2(testInput)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}