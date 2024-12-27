package Y2024

import Application

class Day23 : Application {
    var computerLinks = mutableMapOf<String, List<String>>()
    var interconnectedComputersPart1 = mutableListOf<Triple<String, String, String>>()
    val interconnectedComputers = mutableListOf<MutableList<String>>()
    override fun run(fileName: String): Pair<Any, Any> {
        val input = readInput(fileName)
        val links = input.map { Pair(it.split("-")[0], it.split("-")[1]) }
        val res1 = this.part1(links)
        val res2 = this.part2(links)
        return res1 to res2
    }

    private fun part1(input: List<Pair<String, String>>): Any {
        input.forEach(::computeLink)
        val res = interconnectedComputersPart1.distinct().filter {
            it.first.startsWith('t') ||
                    it.second.startsWith('t') ||
                    it.third.startsWith('t')
        }.size
        return res
    }

    private fun part2(input: List<Pair<String, String>>): Any {
        input.forEach(::computeCluster)
        var max = 0
        var maxIndex = 0
        interconnectedComputers.forEachIndexed { index, it ->
            if (it.size > max) {
                max = it.size
                maxIndex = index
            }
        }
        return interconnectedComputers[maxIndex].sorted().joinToString(",")
    }

    private fun computeLink(pair: Pair<String, String>) {
        computerLinks[pair.first] = computerLinks.getOrDefault(pair.first, listOf()).plus(pair.second)
        computerLinks[pair.second] = computerLinks.getOrDefault(pair.second, listOf()).plus(pair.first)
        for (i in computerLinks[pair.first]!!) {
            if (computerLinks.getOrDefault(i, listOf()).contains(pair.second)) {
                val tmpList = listOf(pair.first, pair.second, i).sorted()
                interconnectedComputersPart1.add(Triple(tmpList[0], tmpList[1], tmpList[2]))
            }
        }
    }

    private fun computeCluster(input: Pair<String, String>) {
        var addedToACluster = false
        val firstCluster = interconnectedComputers.filter { it.contains(input.first) }
        val secondCluster = interconnectedComputers.filter { it.contains(input.second) }
        for (lst in firstCluster) {
            var allConnected = true
            for (i in lst) {
                if (!computerLinks[input.second]!!.contains(i)) {
                    allConnected = false
                    break
                }
            }
            if (allConnected) {
                addedToACluster = true
                lst.add(input.second)
            }
        }
        for (lst in secondCluster) {
            var allConnected = true
            if (lst.contains(input.first)) continue
            for (i in lst) {
                if (!computerLinks[input.first]!!.contains(i)) {
                    allConnected = false
                    break
                }
            }
            if (allConnected) {
                addedToACluster = true
                lst.add(input.first)
            }
        }
        if (!addedToACluster) interconnectedComputers.add(input.toList().toMutableList())
    }
}