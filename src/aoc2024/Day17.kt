package aoc2024

import AocApplication
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.truncate

class Day17 : AocApplication {
    private val registers = mutableMapOf<Int, Int>()
    private var programs = mutableListOf<Int>()
    private var instructionPointer = 0
    var res = mutableListOf<Int>()
    override fun run(fileName: String): Pair<Any, Any> {
        val input = readInput(fileName)
        registers.put(0, input.filter { it.startsWith("Register A:") }[0].split(": ")[1].toInt())
        registers.put(1, input.filter { it.startsWith("Register B:") }[0].split(": ")[1].toInt())
        registers.put(2, input.filter { it.startsWith("Register C:") }[0].split(": ")[1].toInt())
        programs = input.filter { it.startsWith("Program") }[0].split((": "))[1].split(",").map { it.toInt() }
            .toMutableList()

        val res1 = this.part1()

        val res2 = this.part2()
        return res1 to res2
    }

    private fun part1(): Any {
        while (instructionPointer < programs.size) {
            val opcode = programs[instructionPointer].toInt()
            val operand = programs[instructionPointer + 1].toInt()
            when (opcode) {
                0 -> dvFunc(operand, 0)
                1 -> xlFunc(operand)
                2 -> stFunc(operand)
                3 -> jnzFunc(operand)
                4 -> xcFunc()
                5 -> outFunc(operand)
                6 -> dvFunc(operand, 1)
                7 -> dvFunc(operand, 2)
            }
        }
        return res.joinToString(",")
    }

    private fun dvFunc(operand: Int, targetIdx: Int) {
        val comboOperand = toComboOperand(operand)
        val res = registers[0]!! / 2.0.pow(comboOperand)
        registers[targetIdx] = truncate(res).roundToInt()
        instructionPointer += 2
    }

    private fun xlFunc(operand: Int) {
        val res = registers[1]!!.xor(operand)
        registers[1] = res
        instructionPointer += 2
    }

    private fun stFunc(operand: Int) {
        val comboOperand = toComboOperand(operand)
        val res = comboOperand % 8
        registers[1] = res
        instructionPointer += 2
    }

    private fun jnzFunc(operand: Int) {
        if (registers[0] != 0) {
            instructionPointer = operand
        } else {
            instructionPointer += 2
        }
    }

    private fun xcFunc() {
        val res = registers[1]!! xor registers[2]!!
        registers[1] = res
        instructionPointer += 2
    }

    private fun outFunc(operand: Int) {
        val comboOperand = toComboOperand(operand)
        res.add(comboOperand % 8)
        instructionPointer += 2
    }
    
    private fun toComboOperand(operand: Int): Int = when(operand) {
        4 -> registers[0]!!
        5 -> registers[1]!!
        6 -> registers[2]!!
        else -> operand
    }

//    Not able to find a generic solution
//    Instead analyzed given program and reverse engineered the solution
//    Compute backwards from the end of program, and concat to get A
    private fun part2(): Any {
        var possibleA = mutableListOf(7L)
        for (program in programs.dropLast(1).reversed()) {
            val newAs = mutableListOf<Long>()
            for (A in possibleA) {
                for (i in 0L..7L) {
                    val newA = A * 8 + i
                    val B: Long = i xor 7
                    val C: Long = newA.ushr(B.toInt())
                    if (((B xor C) xor 7) % 8 == program.toLong()) {
                        newAs.add(newA)
                    }
                }
            }
            if (newAs.isEmpty()) {
                "error for $program".println()
            }
            else {
                newAs.println()
            }
            possibleA = newAs
        }
        return possibleA.sorted()
    }
}