import kotlin.math.absoluteValue

fun main() {
    data class Coordinate(var x: Int, var y: Int) {
        fun manhattanDistance(other: Coordinate): Int {
            return (this.x - other.x).absoluteValue + (this.y - other.y).absoluteValue
        }
    }

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
            val beaconDistance = sensor.manhattanDistance(beacon)

            val distToTargetRow = (row - sensor.y).absoluteValue

            if (distToTargetRow < beaconDistance) {
                val remainder = beaconDistance - distToTargetRow
                ((sensor.x - remainder)..(sensor.x + remainder)).forEach {
                    emptyCoords.add(Coordinate(it, row))
                }
            }
        }

        emptyCoords.removeAll(data.values.toSet())

        return emptyCoords.size
    }

    fun generatePointsImmediatelyOutsideRadius(sensor: Coordinate, range: Int): Set<Coordinate> {
        val points = mutableSetOf<Coordinate>()

        val top = Coordinate(sensor.x, sensor.y + (range + 1))
        val bottom = Coordinate(sensor.x, sensor.y - (range + 1))
        val left = Coordinate(sensor.x - (range + 1), sensor.y)
        val right = Coordinate(sensor.x + (range + 1), sensor.y)

        val point = top.copy()

        while (point != right) {
            point.x++
            point.y--
            points.add(point.copy())
        }

        while (point != bottom) {
            point.x--
            point.y--
            points.add(point.copy())
        }

        while (point != left) {
            point.x--
            point.y++
            points.add(point.copy())
        }

        while (point != top) {
            point.x++
            point.y++
            points.add(point.copy())
        }

        return points
    }

    // TODO change to check the tiles just outside the range of each sensor
    fun part2(input: List<String>, lowerBound: Int, upperBound: Int): Long {
        val data = parseInput(input)

        val sensors = data.entries.associate { (sensor, beacon) ->
            sensor to ((sensor.x - beacon.x).absoluteValue + (sensor.y - beacon.y).absoluteValue)
        }

        sensors.forEach { (sensor, range) ->
            generatePointsImmediatelyOutsideRadius(sensor, range)
                .filter { it.x in lowerBound..upperBound && it.y in lowerBound..upperBound }
                .forEach { c ->
                    if (sensors.all { (s, r) -> s.manhattanDistance(c) > r }) {
                        return c.x.toLong() * 4000000L + c.y.toLong()
                    }
                }
        }

        return 0
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput, 10) == 26)
    check(
        generatePointsImmediatelyOutsideRadius(Coordinate(5, 5), 2).containsAll(
            setOf(
                Coordinate(5, 2),
                Coordinate(6, 3),
                Coordinate(7, 4),
                Coordinate(8, 5),
                Coordinate(7, 6),
                Coordinate(6, 7),
                Coordinate(5, 8),
                Coordinate(4, 7),
                Coordinate(3, 6),
                Coordinate(2, 5),
                Coordinate(3, 4),
                Coordinate(4, 3)
            )
        )
    )
    check(part2(testInput, 0, 20) == 56000011L)

    val input = readInput("Day15")
    println(part1(input, 2000000))
    println(part2(input, 0, 4000000))
}
