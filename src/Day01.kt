fun main() {
    fun part1(input: List<String>): Int {
        var elfCal = 0
        var totalCal = 0

        for(line in input) {
            val cal = line.toIntOrNull()
            if(cal == null) {
                if(elfCal > totalCal) totalCal = elfCal
                elfCal = 0
            } else {
                elfCal += cal
            }
        }

        return totalCal
    }

    fun part2(input: List<String>): Int {
        var elfCal = 0
        val calArray = arrayListOf<Int>()

        for(line in input) {
            val cal = line.toIntOrNull()
            if(cal == null) {
                calArray.add(elfCal)
                elfCal = 0
            } else {
                elfCal += cal
            }
        }

        calArray.sortDescending()

        return calArray[0] + calArray[1] + calArray[2]
    }

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
