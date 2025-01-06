package leetcode

import kotlin.io.path.Path
import kotlin.io.path.readText

fun readLeetCodeInput(name: String) = Path("src/leetcode/resources/$name.txt").readText().trim().lines()
