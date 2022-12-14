import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {

    data class Graph<T>(
        val vertices: MutableSet<T> = mutableSetOf(),
        val edges: MutableMap<T, Set<T>> = mutableMapOf(),
        val weights: MutableMap<Pair<T, T>, Int> = mutableMapOf()
    )

    fun Char.toElevation() = when(this) {
        'S' -> 'a'.code
        'E' -> 'z'.code
        else -> this.code
    }

    data class Point(
        val x: Int,
        val y: Int,
        val elevation: Int
    )

    fun List<String>.getPoint(x: Int, y: Int): Point {
        return Point(x, y, this[y][x].toElevation())
    }

    fun buildGraph(input: List<String>, remove: (current: Point, next: Point) -> Boolean): Graph<Point> {
        val graph = Graph<Point>()

        input.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                val point = Point(x, y, c.toElevation())

                val edges = mutableSetOf<Point>()

                if(x != input.first().lastIndex) edges.add(input.getPoint(x+1, y))
                if(x != 0) edges.add(input.getPoint(x-1, y))
                if(y != input.lastIndex) edges.add(input.getPoint(x, y+1))
                if(y != 0) edges.add(input.getPoint(x, y-1))

                edges.removeIf { dest -> remove(point, dest)}

                graph.vertices.add(point)
                graph.edges[point] = edges
                edges.forEach { e ->
                    graph.weights[point to e] = 1
                }
            }
        }

        return graph
    }

    fun <T> dijkstra(graph: Graph<T>, start: T): Map<T, T?> {
        val s = mutableSetOf<T>()

        val delta = graph.vertices.associateWith { Int.MAX_VALUE }.toMutableMap()
        delta[start] = 0

        val previous: MutableMap<T, T?> = graph.vertices.associateWith { null }.toMutableMap()

        while (s != graph.vertices) {
            val v: T = delta
                .filter { !s.contains(it.key) }
                .minBy { it.value }
                .key

            graph.edges.getValue(v).minus(s).forEach { neighbor ->
                val newPath = delta.getValue(v) + graph.weights.getValue(Pair(v, neighbor))

                if(newPath < delta.getValue(neighbor)) {
                    delta[neighbor] = newPath
                    previous[neighbor] = v
                }
            }

            s.add(v)
        }

        return previous.toMap()
    }

    fun <T> shortestPath(tree: Map<T, T?>, start: T, end: T): List<T> {
        fun pathTo(start: T, end: T): List<T> {
            if (tree[end] == null) return listOf(end)
            return listOf(pathTo(start, tree[end]!!), listOf(end)).flatten()
        }

        return pathTo(start, end)
    }

    fun part1(input: List<String>): Int {
        val graph = buildGraph(input) { current, next ->
            next.elevation - current.elevation > 1
        }
        val startY = input.indexOfFirst { it.contains('S') }
        val startX = input[startY].toCharArray().indexOfFirst { it == 'S' }
        val start = input.getPoint(startX, startY)
        val tree = dijkstra(graph, start)
        val endY = input.indexOfFirst { it.contains('E') }
        val endX = input[endY].toCharArray().indexOfFirst { it == 'E' }
        val end = input.getPoint(endX, endY)
        val path = shortestPath(tree, start, end)
        return path.size - 1
    }

    fun part2(input: List<String>): Int {
        val graph = buildGraph(input) { current, next ->
            current.elevation - next.elevation > 1
        }
        val endY = input.indexOfFirst { it.contains('E') }
        val endX = input[endY].toCharArray().indexOfFirst { it == 'E' }
        val end = input.getPoint(endX, endY)

        val tree = dijkstra(graph, end)

        val candidates = graph.vertices
            .filter { it.elevation == 'a'.code }

        val paths = candidates
            .mapNotNull { p ->
                val path = shortestPath(tree, end, p)
                if(path.containsAll(listOf(p, end))) {
                    path
                } else {
                    null
                }
            }

        return paths.minOf { it.size - 1 }
    }

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
