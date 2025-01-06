package aoc2024

import AocApplication

class Day19 : AocApplication {
    var minLength = 10
    private val memoPart1 = mutableMapOf<String, Boolean>()
    private val memoPart2 = mutableMapOf<String, Long>()
    override fun run(fileName: String): Pair<Long, Long> {
        val input = readInput(fileName)
        val towels = input[0].split(", ")
        towels.forEach {
            memoPart1[it] = true
            minLength = minOf(minLength, it.length)
        }
        val patterns = input.subList(2, input.size)

        val res1 = this.part1(towels = towels, patterns = patterns)
        val res2 = this.part2(towels = towels, patterns = patterns)
        return res1 to res2
    }

    private fun part1(towels: List<String>, patterns: List<String>): Long {
        var res = 0L
        patterns.forEach {
            "Matching $it".println()
            if (matchPattern(it, towels)) res++
        }
        return res
    }

    private fun part2(towels: List<String>, patterns: List<String>): Long {
        towels.forEach {
            findAllCombinations(it, towels)
        }
        patterns.forEach {
            memoPart2[it] = findAllCombinations(it, towels)
        }
        return patterns.fold(0L) { acc, it ->
            acc + memoPart2[it]!!
        }
    }

    private fun matchPattern(pattern: String, towels: List<String>): Boolean {
        if (memoPart1.contains(pattern)) return memoPart1[pattern]!!
        if (pattern.length <= minLength) {
            memoPart1[pattern] = false
            return false
        }
        towels.forEach { towel ->
            if (pattern.startsWith(towel)) {
                val newPattern = pattern.substringAfter(towel)
                if (matchPattern(newPattern, towels)) {
                    memoPart1[newPattern] = true
                    return true
                }
                memoPart1[newPattern] = false
            }
        }
        return false
    }

    private fun findAllCombinations(pattern: String, towels: List<String>): Long {
        if (memoPart2.containsKey(pattern)) return memoPart2[pattern]!!
        if (pattern.length <= minLength) {
            if (towels.contains(pattern)) {
                memoPart2[pattern] = memoPart2[pattern]?.plus(1) ?: 1
            } else memoPart2[pattern] = 0
        }
        var res = 0L
        towels.forEach { towel ->
            if (pattern.startsWith(towel)) {
                if (pattern == towel) res += memoPart2[towel] ?: 1
                else {
                    val newPattern = pattern.substringAfter(towel)
                    val combinations = findAllCombinations(newPattern, towels)
                    memoPart2[newPattern] = combinations
                    res += combinations
                }
//                return combinations
            }
        }
        memoPart2[pattern] = res
        return res
    }
}