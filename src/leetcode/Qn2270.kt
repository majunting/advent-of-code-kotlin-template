package leetcode

import LeetcodeApplication
import aoc2024.println

class Qn2270 : LeetcodeApplication {
    private val input1 = listOf(10, 4, -8, 7).toIntArray()
    private val input2 = listOf(2, 3, 1, 0).toIntArray()
    override fun run(): Any {
        return "${waysToSplitArray(input1)}, ${waysToSplitArray(input2)}"
    }

    override fun idx() = 2270

    private fun waysToSplitArray(nums: IntArray): Int {
//        val sums =
//            nums.fold(mutableListOf<Long>()) { acc, i ->
//                acc.add((acc.lastOrNull() ?: 0) + i)
//                acc
//            }
//        val sum = sums.last()
//
////        sums.println()
//        return sums.dropLast(1).filter { it * 2 >= sum }.size
        val sums =
            nums.scan(0L) {acc, i -> acc + i}.drop(1)
        val sum = sums.last()

//        sums.println()
        return sums.dropLast(1).filter { it >= sum -it }.size
    }
}
