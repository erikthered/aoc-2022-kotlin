fun main() {

    data class File(val name: String, val path: List<String>, val size: Int)

    data class Folder(val name: String, val path: List<String>, val files: List<File>)

    fun computeFolderSizes(input: List<String>): List<Pair<String, Int>> {
        val location = mutableListOf<String>()
        val folders = mutableListOf<Folder>()
        val files = mutableListOf<File>()

        folders.add(Folder("[root]", emptyList(), emptyList()))

        for (line in input) {
            // Command
            if (line.startsWith("$")) {
                val segments = line.split(" ")
                when (segments[1]) {
                    "ls" -> {

                    }

                    "cd" -> {
                        when (segments[2]) {
                            "/" -> {
                                location.clear()
                            }

                            ".." -> {
                                location.removeLast()
                            }

                            else -> {
                                location.add(segments[2])
                            }
                        }
                    }
                }
            } else { // Output
                val (first, name) = line.split(" ")
                when (first) {
                    "dir" -> {
                        folders.add(Folder(name, location.toList(), emptyList()))
                    }

                    else -> {
                        val size = first.toInt()
                        files.add(File(name, location.toList(), size))
                    }
                }
            }
        }

        return folders.map { folder ->
            val fullPath = if (folder.name == "[root]") {
                folder.path
            } else {
                folder.path + folder.name
            }
            "/" + fullPath.joinToString("/") to files.filter { file ->
                file.path.size >= fullPath.size && file.path.subList(0, fullPath.lastIndex + 1) == fullPath
            }.sumOf { it.size }
        }
    }

    fun part1(input: List<String>): Int {
        val sizeLimit = 100000

        val folderSizes = computeFolderSizes(input)

        return folderSizes
            .filter { (_, size) -> size <= sizeLimit }
            .sumOf { it.second }
    }

    fun part2(input: List<String>): Int {
        val totalDiskSpace = 70000000
        val spaceNeeded = 30000000

        val folderSizes = computeFolderSizes(input)

        val toFree = spaceNeeded - (totalDiskSpace - folderSizes.first().second)

        return folderSizes.sortedBy { it.second }.first { it.second > toFree }.second
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)


    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
