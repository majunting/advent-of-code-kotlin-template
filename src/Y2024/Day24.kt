package Y2024

import Application

class Day24 : Application {
    var knowns = mutableMapOf<String, Int>()
    var equations = mutableMapOf<Int, Pair<String, Triple<String, String, String>>>()
    var unknowns = mutableListOf<String>()

    override fun run(fileName: String): Pair<Any, Any> {
        val input = readInput(fileName)
        knowns =
            input.filter { it.contains(':') }.map { it.split(':')[0] to it.split(": ")[1].toInt() }.toMap()
                .toMutableMap()
        equations = input.filter { it.contains("->") }.mapIndexed { index, it ->
            val parts = it.split(" -> ")
            index to (parts[0].split(' ')[1] to Triple(parts[0].split(' ')[0], parts[0].split(' ')[2], parts[1]))
        }.toMap().toMutableMap()
        unknowns = input.filter { it.contains("->") }.fold(mutableListOf<String>()) { list, it ->
            list.add(it.split(" -> ")[1])
            list.add(it.split(" -> ")[0].split(' ')[0])
            list.add(it.split(" -> ")[0].split(' ')[2])
            list
        }
        val initialEquations = equations
        unknowns.minus(knowns.keys)
        val res1 = this.part1()
        val res2 = this.part2(initialEquations)
        return res1 to res2
    }

    private fun part1(): Any {
        while (equations.size > 0) {
            var solved = mutableListOf<Int>()
            for (kv in equations) {
                if (knowns.containsKey(kv.value.second.first) && knowns.containsKey(kv.value.second.second)) {
                    val res = computeLogic(
                        kv.value.first,
                        kv.value.second.first,
                        kv.value.second.second,
                        1
                    )
                    if (res != -1) {
                        knowns[kv.value.second.third] = res
                        unknowns.remove(kv.value.second.third)
                        solved.add(kv.key)
                    }
                } else {
                    if (knowns.containsKey(kv.value.second.first) && knowns.containsKey(kv.value.second.third)) {
                        val res = computeLogic(kv.value.first, kv.value.second.first, kv.value.second.third, 2)
                        if (res != -1) {
                            knowns[kv.value.second.second] = res
                            unknowns.remove(kv.value.second.second)
                            solved.add(kv.key)
                        }
                    }
                    if (knowns.containsKey(kv.value.second.second) && knowns.containsKey(kv.value.second.third)) {
                        val res = computeLogic(kv.value.first, kv.value.second.second, kv.value.second.third, 2)
                        if (res != -1) {
                            knowns[kv.value.second.first] = res
                            unknowns.remove(kv.value.second.first)
                            solved.add(kv.key)
                        }
                    }
                }

            }
            for (i in solved) equations.remove(i)
        }
        val res = knowns.keys.filter { it.startsWith('z') }.sortedDescending().map { knowns[it] }
            .fold(StringBuilder()) { acc: StringBuilder, i: Int? -> acc.append(i) }.toString()
        val zs = res
        val xs = knowns.keys.filter { it.startsWith('x') }.sortedDescending().map { knowns[it] }
            .fold(StringBuilder()) { acc: StringBuilder, i: Int? -> acc.append(i) }.toString().toLong(2)
        val ys = knowns.keys.filter { it.startsWith('y') }.sortedDescending().map { knowns[it] }
            .fold(StringBuilder()) { acc: StringBuilder, i: Int? -> acc.append(i) }.toString().toLong(2)

        xs.println()
        ys.println()
        (xs+ys).toString(2).println()
        zs.println()
        return res.toLong(2)
    }

    private fun part2(equations: MutableMap<Int, Pair<String, Triple<String, String, String>>>): Any {
        return 0
    }

    private fun computeLogic(operation: String, first: String, second: String, type: Int): Int {
        val a = knowns[first]!!
        val b = knowns[second]!!
        return when (type) {
            1 -> when (operation) {
                "AND" -> a and b
                "XOR" -> a xor b
                "OR" -> a or b
                else -> -1
            }

            2 -> when {
                operation == "AND" && a == 1 && b == 0 -> 0
                operation == "AND" && a == 1 && b == 1 -> 1
                operation == "OR" && a == 0 && b == 1 -> 1
                operation == "XOR" && a == 0 && b == 0 -> 0
                operation == "XOR" && a == 0 && b == 1 -> 1
                operation == "XOR" && a == 1 && b == 0 -> 1
                operation == "XOR" && a == 1 && b == 1 -> 0
                else -> -1
            }

            else -> -1
        }
    }
}
