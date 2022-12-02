fun main() {
    fun part1(input: List<String>): Int {
        var score = 0
        for(line in input) {
            val opponent = OpponentMove.valueOf(line[0].toString())
            val player = PlayerMove.valueOf(line[2].toString())

            score += player.points

            score += determineOutcome(opponent, player).points
        }
        return score
    }

    fun part2(input: List<String>): Int {
        var score = 0

        for(line in input) {
            val opponent = OpponentMove.valueOf(line[0].toString())
            val desiredOutcome = when(line[2]) {
                'X' -> Outcome.LOSS
                'Y' -> Outcome.DRAW
                'Z' -> Outcome.WIN
                else -> throw IllegalArgumentException()
            }
            val player = determinePlay(opponent, desiredOutcome)
            score += player.points
            score += desiredOutcome.points
        }

        return score
    }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

fun determineOutcome(op: OpponentMove, pl: PlayerMove): Outcome {
    return when(op) {
        OpponentMove.A -> {
            when(pl) {
                PlayerMove.X -> Outcome.DRAW
                PlayerMove.Y -> Outcome.WIN
                PlayerMove.Z -> Outcome.LOSS
            }
        }
        OpponentMove.B -> {
            when(pl) {
                PlayerMove.X -> Outcome.LOSS
                PlayerMove.Y -> Outcome.DRAW
                PlayerMove.Z -> Outcome.WIN
            }
        }
        OpponentMove.C -> {
            when(pl) {
                PlayerMove.X -> Outcome.WIN
                PlayerMove.Y -> Outcome.LOSS
                PlayerMove.Z -> Outcome.DRAW
            }
        }
    }
}

fun determinePlay(op: OpponentMove, out: Outcome): PlayerMove {
    return when(op) {
        OpponentMove.A -> {
            when(out) {
                Outcome.WIN -> PlayerMove.Y
                Outcome.DRAW -> PlayerMove.X
                Outcome.LOSS -> PlayerMove.Z
            }
        }
        OpponentMove.B -> {
            when(out) {
                Outcome.WIN -> PlayerMove.Z
                Outcome.DRAW -> PlayerMove.Y
                Outcome.LOSS -> PlayerMove.X
            }
        }
        OpponentMove.C -> {
            when(out) {
                Outcome.WIN -> PlayerMove.X
                Outcome.DRAW -> PlayerMove.Z
                Outcome.LOSS -> PlayerMove.Y
            }
        }
    }
}

enum class OpponentMove {
    A,
    B,
    C
}

enum class PlayerMove(val points: Int) {
    X(1),
    Y(2),
    Z(3)
}

enum class Outcome(val points: Int) {
    WIN(6),
    DRAW(3),
    LOSS(0)
}