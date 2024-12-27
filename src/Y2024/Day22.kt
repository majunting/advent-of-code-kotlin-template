package Y2024

import Application
import kotlin.math.abs

class Day22 : Application {
    var secretNumberList = mutableListOf<MutableList<Int>>()
    val diffMaps = mutableListOf<MutableMap<Int, Int>>()
    override fun run(fileName: String): Pair<Any, Any> {
        val input = readInput(fileName).map { it.toLong() }

        val res1 = this.part1(input)
//        same test input will give 24 for part 2 (sequence: -9 9 -1 0)
        val res2 = this.part2(fileName.contains("test"))
        return res1 to res2
    }

    private fun part1(input: List<Long>): Long {
        var res = 0L
        input.map {
            val lst = mutableListOf<Int>()
            var currSecretNum = it
            for (i in 0..1999) {
                currSecretNum = computeSecretNum(currSecretNum)
                lst.add(currSecretNum.toInt())
            }

            secretNumberList.add(lst.map { it % 10 }.toMutableList())
            res += lst.last()
        }
        return res
    }

    private fun part2(isTest: Boolean): Int {
        if (isTest) {
            secretNumberList = mutableListOf()
            part1(listOf(1L, 2L, 3L, 2024L))
        }
        computeDiffs()
        val combinedMap = mutableMapOf<Int, Int>()
        diffMaps.forEach { map ->
//            if(map.containsKey(48014903)) map[48014903].println()
            for (kvPair in map) {
                combinedMap[kvPair.key] = combinedMap.getOrDefault(kvPair.key, 0) + kvPair.value
            }

        }
        "${
            combinedMap.filter {
                it.value == combinedMap.values.max()
            }
        }".println()
//        "${combinedMap[48014903]}".println()
        return combinedMap.values.max()
    }

    private fun computeDiffs() {
        secretNumberList.forEach {
            val diffList = it.mapIndexedNotNull{ index, i ->
                if (index == 0) null else i - it[index - 1]
            }
            val diffMap = mutableMapOf<Int, Int>()
            for (i in 1..< it.size - 4) {
                val encodedSec = encodeToInt(diffList.subList(i - 1, i + 3))
                if (!diffMap.containsKey(encodedSec)) {
//                    if(encodedSec == 48014903) {
//                        "${it[i - 2]}, ${it[i - 1]}, ${it[i]}, ${it[i + 1]}, ${it[i + 2]}, ${it[i + 3]}, ${it[i + 4]}, ${it[i + 5]}, ${it[i + 6]}".println()
//                    }
//                    The correct index to put in is i + 3, for some reason
                    diffMap[encodedSec] = it[i + 3]
                }
            }
            diffMaps.add(diffMap)
        }
    }

    private fun encodeToInt(input: List<Int>): Int {
        var res = 0
        res += (if (input[0] < 0) (input[0] + 50) else input[0]) * 1000000
        res += (if (input[1] < 0) (input[1] + 50) else input[1]) * 10000
        res += (if (input[2] < 0) (input[2] + 50) else input[2]) * 100
        res += (if (input[3] < 0) (input[3] + 50) else input[3])
        return res
    }

    private fun computeSecretNum(a: Long): Long {
        var res = step1(a)
        res = step2(res)
        return step3(res)
    }

    private fun step1(a: Long): Long {
        var res = a shl 6
        res = mix(res, a)
        res = prune(res)
        return res
    }

    private fun step2(a: Long): Long {
        var res = a shr 5
        res = mix(res, a)
        res = prune(res)
        return res
    }

    private fun step3(a: Long): Long {
        var res = a shl 11
        res = mix(res, a)
        res = prune(res)
        return res
    }

    private fun mix(a: Long, b: Long): Long = a xor b

    private fun prune(a: Long): Long = a % 16777216
}