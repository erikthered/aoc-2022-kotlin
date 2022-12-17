import kotlin.math.absoluteValue

fun main() {
    data class Coordinate(val x: Int, val y: Int)

    val regex = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()

    fun parseInput(input: List<String>): Map<Coordinate, Coordinate> {
        return input.associate {
            val match = regex.find(it)
            val (sx, sy, bx, by) = match!!.destructured
            Coordinate(sx.toInt(), sy.toInt()) to Coordinate(bx.toInt(), by.toInt())
        }
    }

    fun part1(input: List<String>, row: Int): Int {
        val data = parseInput(input)

        val emptyCoords = mutableSetOf<Coordinate>()

        data.entries.forEach { (sensor, beacon) ->
            val beaconDistance = (sensor.x - beacon.x).absoluteValue + (sensor.y - beacon.y).absoluteValue

            val distToTargetRow = (row - sensor.y).absoluteValue

            if(distToTargetRow < beaconDistance) {
                val remainder = beaconDistance - distToTargetRow
                ((sensor.x-remainder)..(sensor.x+remainder)).forEach {
                    emptyCoords.add(Coordinate(it, row))
                }
            }
        }

        emptyCoords.removeAll(data.values.toSet())

        return emptyCoords.size
    }

    // TODO change to check the tiles just outside the range of each sensor
    fun part2(input: List<String>, lowerBound: Int, upperBound: Int): Int {
        val data = parseInput(input)

        val sensors = data.entries.associate { (sensor, beacon) ->
            sensor to ((sensor.x - beacon.x).absoluteValue + (sensor.y - beacon.y).absoluteValue)
        }

        (lowerBound..upperBound).forEach { row ->
            val candidates = (lowerBound..upperBound).toMutableSet()
            sensors.forEach { (sensor, dist) ->
                val distToTargetRow = (row - sensor.y).absoluteValue
                if(distToTargetRow < dist) {
                    val remainder = dist - distToTargetRow
                    candidates.removeIf { it >= sensor.x-remainder && it <= sensor.x+remainder }
                }
            }
            // TODO make and intersect int ranges from each sensor
            candidates.removeAll(data.values.map { it.x }.toSet())
            if(candidates.isNotEmpty()) {
                return candidates.single() * 4000000 + row
            }
        }

        return 0
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput, 10) == 26)
    check(part2(testInput, 0, 20) == 56000011)

    val input = readInput("Day15")
    println(part1(input, 2000000))
    println(part2(input, 0, 4000000))
}
