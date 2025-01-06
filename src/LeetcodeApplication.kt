import leetcode.Qn2270
import aoc2024.println

interface LeetcodeApplication {
    fun run(): Any
    fun idx(): Int
}

fun main() {
    runLeetCode()
}

fun runLeetCode() {
    val application =  Qn2270()
    "Question ${application.idx()} result: ".println()
    application.run().println()
}