package aoc2024

import AocApplication
import kotlin.math.abs

class Day20 : AocApplication {
    private var inputMap = mutableListOf<MutableList<Int>>()
    private val path = mutableListOf<Pair<Int, Int>>()
    private var initialPos = Pair(0, 0)
    private var finalPos = Pair(0, 0)
    private var isTest = false
    override fun run(fileName: String): Pair<Any, Any> {
        val input = readInput(fileName)
        if (fileName.contains("test")) {
            isTest = true
            val test = 32+31+29+39+25+23+20+19+12+14+12+22+4+3
            "test res for part 2: $test".println()
        }

        inputMap = input.mapIndexed { i, it ->
            it.mapIndexed { j, c ->
                when (c) {
                    '#' -> -1
                    'S' -> {
                        initialPos = Pair(i, j)
                        0
                    }

                    'E' -> {
                        finalPos = Pair(i, j)
                        input.fold(0) { acc, s -> acc + s.fold(0) { accum, c -> if (c == '.') accum + 1 else accum + 0 } } + 2
                    }

                    else -> 0
                }
            }.toMutableList()
        }.toMutableList()
        val res1 = this.part1()
        val res2 = this.part2()
        return res1 to res2
    }

    private fun part1(): Any {
        var res = 0L
        // first - loop through all 0s to add step count
        computeSteps()
        // second - loop through all -1s to find diff
        return find2StepGap()
    }

    private fun part2(): Any {
        var res = 0L
        var pos = initialPos
//        while (pos != finalPos) {
//            res += findAllPossiblePoints(pos).size
//            for (dir in directions) {
//                if (pos.plus(dir) == finalPos) return res
//                if (inputMap[pos.first + dir.first][pos.second + dir.second] > inputMap[pos.first][pos.second]) {
//                    pos = pos.plus(dir)
//                    break
//                }
//            }
//        }
        for (i in 0 ..< path.size) {
            for (j in i + 1 ..< path.size) {
                val distance = stepCount(path[i].first, path[i].second, path[j])
                if (distance > 20) continue
                if (j - i - distance >= (if(isTest) 50 else 100)) res++
            }
        }
        return res
    }

    private fun computeSteps() {
        var currPos = initialPos
        var currScore = 1
        while (currPos != finalPos) {
            path.add(currPos)
            inputMap[currPos.first][currPos.second] = currScore
            currScore++
            for (dir in directions) {
                if (currPos.plus(dir) == finalPos) {
                    path.add(finalPos)
                    return
                }
                if (inputMap[currPos.first + dir.first][currPos.second + dir.second] == 0) {
                    currPos = currPos.plus(dir)
                    break
                }
            }
        }
    }

    private fun find2StepGap(): Int {
        var res = 0
        for (i in 1..<inputMap.lastIndex) {
            for (j in 1..<inputMap.lastIndex) {
                if (inputMap[i][j] == -1) {
                    val top = try {
                        inputMap[i + directions[0].first][j + directions[0].second]
                    } catch (e: IndexOutOfBoundsException) {
                        -1
                    }
                    val right = try {
                        inputMap[i + directions[1].first][j + directions[1].second]
                    } catch (e: IndexOutOfBoundsException) {
                        -1
                    }
                    val bottom = try {
                        inputMap[i + directions[2].first][j + directions[2].second]
                    } catch (e: IndexOutOfBoundsException) {
                        -1
                    }
                    val left = try {
                        inputMap[i + directions[3].first][j + directions[3].second]
                    } catch (e: IndexOutOfBoundsException) {
                        -1
                    }
                    val min = findMin(top, right, bottom, left)
                    if (min > 2) {
//                        if (isTest) {
//                            "$min found at x: $i, y: $j".println()
//                        }
                        if (min > (if(isTest) 50 else 100) + 1) res++
                    }
                }
            }
        }
        return res
    }

    private fun findMin(a: Int, b: Int, c: Int, d: Int): Int {
        var min = Int.MAX_VALUE
        if (a != -1 && b != -1) min = minOf(min, abs(a - b))
        if (a != -1 && c != -1) min = minOf(min, abs(a - c))
        if (a != -1 && d != -1) min = minOf(min, abs(a - d))
        if (b != -1 && c != -1) min = minOf(min, abs(b - c))
        if (b != -1 && d != -1) min = minOf(min, abs(b - d))
        if (c != -1 && d != -1) min = minOf(min, abs(c - d))
        return if (min == Int.MAX_VALUE) -1 else min
    }

    private fun findAllPossiblePoints(pos: Pair<Int, Int>): List<Pair<Int, Int>> {
        val res = mutableListOf<Pair<Int, Int>>()
        for (i in pos.first - 20..pos.first + 20) {
            for (j in pos.second - (20 - abs(pos.first - i))..pos.second + (20 - abs(pos.first - i))) {
                try {
                    if (inputMap[i][j] - inputMap[pos.first][pos.second] > (if (isTest) 51 else 101) + stepCount(
                            i,
                            j,
                            pos
                        )
                    ) {
                        res.add(Pair(i, j))
                    }
                } catch (_: IndexOutOfBoundsException) {}
            }
        }
        return res
    }

    private fun stepCount(i: Int, j: Int, pos: Pair<Int, Int>): Int = abs(pos.first - i) + abs(pos.second - j)
}
