package aoc2024

import AocApplication

// 139866567425678 too low
// 340577114764646 too high

// https://www.reddit.com/r/adventofcode/comments/1hjgyps/2024_day_21_part_2_i_got_greedyish/
class Day21 : AocApplication {
	val numberPad = listOf("789", "456", "123", " 0A")
	val directionPad = listOf(" ^A", "<v>")
	val pairConversionMap = mutableMapOf<Pair<Pair<Int, Int>, Pair<Int, Int>>, Map<Pair<Pair<Int, Int>, Pair<Int, Int>>, Long>>(Pair(Pair(0, 2), Pair(0, 2)) to mapOf(Pair(Pair(0, 2), Pair(0, 2)) to 1))
	override fun run(fileName: String): Pair<Long, Long> {
		val input = readInput(fileName)

		if (fileName.contains("test")) {
			"Sample part 2 result: 154115708116294".println()
		}
		val res1 = this.part1(input)
		val res2 = this.part2(input)
		return res1 to res2
	}

	private fun part1(input: List<String>): Long {
		val res = input.fold(0L) { acc, str ->
			val num = str.dropLast(1).toLong()
			val initialStr = processInitialStr(str)
//			str.println()
//			initialStr.println()
			val steps = processPart1(initialStr)
			acc + steps * num
		}
		val newRes = input.fold(0L) { acc, str ->
			val num = str.dropLast(1).toLong()
			val initialStr = processInitialStr(str)
//			str.println()
			val initialFreq = processFreqFromStr(initialStr)
//			initialStr.println()
			val steps = processPart2(initialFreq, 2)
			acc + steps * num
		}
		"Part 1 result with part 2 logic: $newRes".println()
		return res
	}

	private fun part2(input: List<String>): Long {
		val res = input.fold(0L) { acc, str ->
			val num = str.dropLast(1).toLong()
			val initialStr = processInitialStr(str)
//			str.println()
			val initialFreq = processFreqFromStr(initialStr)
//			initialStr.println()
			val steps = processPart2(initialFreq, 25)
			acc + steps * num
		}
		return res
	}

	private fun processInitialStr(input: String): String {
		val stringBuilder = StringBuilder()
		var pos = findNumOnPad('A')
		input.forEach { it ->
			val newPos = findNumOnPad(it)
			val processedStr = processStrFromPos(pos, newPos, false)
			stringBuilder.append(processedStr)
			pos = newPos
		}
		return stringBuilder.toString()
	}

	private fun processOneItr(input: String): String {
		val stringBuilder = StringBuilder()
		var pos = findDirOnPad('A')
		input.forEach { it ->
			val newPos = findDirOnPad(it)
			val processedStr = processStrFromPos(pos, newPos, true)
			stringBuilder.append(processedStr)
			pos = newPos
		}
		return stringBuilder.toString()
	}

	private fun processPart1(input: String): Long {
		var str = input
		for (i in 1 ..2) {
			str = processOneItr(str)
//			str.println()
		}
//		str.length.println()
		return str.length.toLong()
	}

	private fun processFreqFromStr(input: String): Map<Pair<Pair<Int, Int>, Pair<Int, Int>>, Long> {
		val res = mutableMapOf<Pair<Pair<Int, Int>, Pair<Int, Int>>, Long>()
		input.forEachIndexed { index, c ->
//			if (index != 0) {
				val prevChar = if (index == 0) 'A' else input[index - 1]
				res[Pair(findDirOnPad(prevChar), findDirOnPad(c))] =
					res.getOrDefault(Pair(findDirOnPad(prevChar), findDirOnPad(c)), 0) + 1
//			}
		}
		return res
	}

	private fun processPart2(initialFreq: Map<Pair<Pair<Int, Int>, Pair<Int, Int>>, Long>, iterations: Int): Long {
		var freq = initialFreq
		for (i in 1 ..iterations) {
			freq = processFreq(freq)
//			freq.println()
		}
		return freq.values.sum()
	}

	private fun processFreq(initialFreq: Map<Pair<Pair<Int, Int>, Pair<Int, Int>>, Long>): Map<Pair<Pair<Int, Int>, Pair<Int, Int>>, Long> {
		val res = mutableMapOf<Pair<Pair<Int, Int>, Pair<Int, Int>>, Long>()
		initialFreq.forEach { (pair, num) ->
			if (pairConversionMap.containsKey(pair)) {
				val converted = pairConversionMap[pair]!!
				converted.forEach{
					res[it.key] = res.getOrDefault(it.key, 0) + num * it.value
				}
			} else {
				val newFreq =processFreqFromStr(processStrFromPos(pair.first, pair.second, true))
				pairConversionMap[pair] = newFreq
				newFreq.forEach {
					res[it.key] = res.getOrDefault(it.key, 0) + num * it.value
				}
			}
		}
		return res
	}

	private fun findNumOnPad(char: Char): Pair<Int, Int> = Pair(
		numberPad.indexOfFirst { it.contains(char) },
		numberPad.find { it.contains(char) }!!.indexOfFirst { it == char }
	)

	private fun findDirOnPad(char: Char): Pair<Int, Int> = when (char){
		'^' -> Pair(0, 1)
		'A' -> Pair(0, 2)
		'<' -> Pair(1, 0)
		'v' -> Pair(1, 1)
		'>' -> Pair(1, 2)
		else -> Pair(0, 0)
	}

	private fun processStrFromPos(pos: Pair<Int, Int>, newPos: Pair<Int, Int>, isDir: Boolean): String {
		val strBuilder = StringBuilder()
		if (pos.first == newPos.first && pos.second == newPos.second) {
			strBuilder.append('A')
			return strBuilder.toString()
		}
		if (pos.first == newPos.first) {
			if (pos.second > newPos.second) {
				for (i in pos.second - newPos.second downTo 1) strBuilder.append('<')
			} else {
				for (i in newPos.second - pos.second downTo 1) strBuilder.append('>')
			}
			strBuilder.append('A')
			return strBuilder.toString()
		}
		if (pos.second == newPos.second) {
			if (pos.first > newPos.first) {
				for (i in pos.first - newPos.first downTo 1) strBuilder.append('^')
			} else {
				for (i in newPos.first - pos.first downTo 1) strBuilder.append('v')
			}
			strBuilder.append('A')
			return strBuilder.toString()
		}


		if (pos.first < newPos.first) {
//		pos is on top
			if (pos.second > newPos.second) {
//				pos is on right
//				path is DOWN-LEFT
				if (isDir && pos.first == 0 && newPos.second == 0) {
					for (i in newPos.first - pos.first downTo 1) strBuilder.append('v')
					for (i in pos.second - newPos.second downTo 1) strBuilder.append('<')
				} else {
					for (i in pos.second - newPos.second downTo 1) strBuilder.append('<')
					for (i in newPos.first - pos.first downTo 1) strBuilder.append('v')
				}
			} else {
//				path is DOWN-RIGHT
				if (!isDir && pos.second == 0 && newPos.first == 3) {
					for (i in newPos.second - pos.second downTo 1) strBuilder.append('>')
					for (i in newPos.first - pos.first downTo 1) strBuilder.append('v')
				}
				else {
					for (i in newPos.first - pos.first downTo 1) strBuilder.append('v')
					for (i in newPos.second - pos.second downTo 1) strBuilder.append('>')
				}
			}
		} else {
			if (pos.second > newPos.second) {
//				path is UP-LEFT
				if (!isDir && pos.first == 3 && pos.second == 0) {
					for (i in pos.first - newPos.first downTo 1) strBuilder.append('^')
					for (i in pos.second - newPos.second downTo 1) strBuilder.append('<')
				} else {
					for (i in pos.second - newPos.second downTo 1) strBuilder.append('<')
					for (i in pos.first - newPos.first downTo 1) strBuilder.append('^')
				}
			} else {
//				path is UP-RIGHT
				if (isDir && pos.second == 0 && newPos.first == 0) {
					for (i in newPos.second - pos.second downTo 1) strBuilder.append('>')
					for (i in pos.first - newPos.first downTo 1) strBuilder.append('^')
				} else {
					for (i in pos.first - newPos.first downTo 1) strBuilder.append('^')
					for (i in newPos.second - pos.second downTo 1) strBuilder.append('>')
				}
			}
		}
		strBuilder.append('A')
		return strBuilder.toString()
	}
}