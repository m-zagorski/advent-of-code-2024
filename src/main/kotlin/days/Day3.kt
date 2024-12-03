package org.example.days

import org.example.utils.AdventDay
import org.example.utils.numbersRegex

object Day3 : AdventDay {
    override fun part1(input: String) {
        print(calculateMul(input))
    }

    override fun part2(input: String) {
        var startRange = 0
        val result = """do\(\)|don't\(\)""".toRegex().findAll(input).mapNotNull { current ->
            when {
                current.value == "don\'t()" && startRange != -1 -> {
                    input.substring(startRange, current.range.first).also { startRange = -1 }
                }

                current.value == "do()" && startRange == -1 -> {
                    startRange = current.range.first
                    null
                }

                else -> null
            }
        }
            .toMutableList()
            .apply { if (startRange != -1) add(input.substring(startRange, input.length)) }
            .sumOf(::calculateMul)

        print(result)
    }

    private fun calculateMul(input: String): Long {
        return """mul\(\d{1,3},\d{1,3}\)""".toRegex().findAll(input).toList()
            .sumOf {
                val (l, r) = numbersRegex.findAll(it.value).toList().map { it.value.toLong() }
                l * r
            }
    }
}