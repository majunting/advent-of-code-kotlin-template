package aoc2024

import AocApplication

class Day25 : AocApplication {
    override fun run(fileName: String): Pair<Long, Long> {
        val input = readInput(fileName).partition { it == "" }.second.chunked(7)
        val rawLocks = input.filter { it[0] == "#####" }
        val rawKeys = input.filterNot { it[0] == "#####" }
        val locks = computeHeights(rawLocks)
        val keys = computeHeights(rawKeys)
        val res1 = this.part1(locks, keys)
        val res2 = this.part2()
        return res1 to res2
    }

    private fun part1(locks: List<List<Int>>, keys: List<List<Int>>): Long {
        var res = 0L
        locks.forEach {
            res += computeUniquePairs(it, keys)
        }
        return res
    }

    private fun part2(): Long {
        return 0
    }

    private fun computeHeights(input: List<List<String>>): List<List<Int>> =
        input.map {
            val res = mutableListOf<Int>()
            for (i in 0..4) {
                res.add(i, it.fold(0) { acc, s -> if (s[i] == '#') acc + 1 else acc } - 1)
            }
            res
        }

    private fun computeUniquePairs(lock: List<Int>, keys: List<List<Int>>): Int =
        keys.map { keyLockMatches(it, lock) }.filter { it }.size


    private fun keyLockMatches(key: List<Int>, lock: List<Int>): Boolean {
        for (i in 0..4) {
            if (key[i] + lock[i] >= 6) return false
        }
        return true
    }
}