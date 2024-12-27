package Y2024

import Application
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

fun readInput(name: String) = Path("src/Y2024/resources/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun Pair<Int, Int>.plus(b: Pair<Int, Int>) = Pair(this.first + b.first, this.second + b.second)

val directions = listOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1))

val allDirections = directions.plus(listOf(Pair(-1, 1), Pair(1, 1), Pair(1, -1), Pair(-1, -1)))

fun chooseClassFromDay2024(day: Int): Application = when(day) {
    9 -> Day09()
    10 -> Day10()
    11 -> Day11()
    12 -> Day12()
    13 -> Day13()
    14 -> Day14()
    15 -> Day15()
    16 -> Day16()
    17 -> Day17()
    18 -> Day18()
    19 -> Day19()
    20 -> Day20()
    21 -> Day21()
    22 -> Day22()
    23 -> Day23()
    24 -> Day24()
    25 -> Day25()
    else -> throw IllegalArgumentException("Unknown day: $day")
}