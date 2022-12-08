fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.map { line -> line.map { it.digitToInt() } }

        var visible = 0

        grid.forEachIndexed { y, row ->
            // Add all trees in first or last row
            if (y == 0 || y == grid.lastIndex) {
                visible += row.size
            } else {
                row.forEachIndexed { x, height ->
                    // Add all trees in first or last col
                    if (x == 0 || x == row.lastIndex) {
                        visible += 1
                    } else {
                        if (height > row.subList(0, x).max()
                            || height > row.subList(x + 1, row.lastIndex + 1).max()
                            || height > grid.map { row -> row[x] }.subList(0, y).max()
                            || height > grid.map { row -> row[x] }.subList(y + 1, grid.lastIndex + 1).max()
                        ) visible += 1
                    }
                }
            }
        }
        return visible
    }

    fun part2(input: List<String>): Int {
        val rows = input.map { line -> line.map { it.digitToInt() } }

        var maxScore = 0

        rows.forEachIndexed { y, row ->
            row.forEachIndexed { x, height ->
                val left = row.subList(0, x).reversed()
                val right = row.subList(x + 1, row.lastIndex + 1)
                val up = rows.map { row -> row[x] }.subList(0, y).reversed()
                val down = rows.map { row -> row[x] }.subList(y + 1, rows.lastIndex + 1)

                val score = arrayOf(left, right, up, down).map { direction ->
                    direction.indexOfFirst { it >= height }.let { if (it == -1) direction.size else it + 1 }
                }.reduce { a, b -> a * b }

                if (score > maxScore) maxScore = score
            }
        }

        return maxScore
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
