package org.example.days

import org.example.utils.AdventDay
import org.example.utils.numbersRegex
import kotlin.math.abs

object Day2 : AdventDay {
    override fun part1(input: List<String>) {
        print(input.map(::lineToNumbers).count(::isSafe))
    }

    override fun part2(input: List<String>) {
        print(input.map(::lineToNumbers).count(::isLineSafeWithErrorTolerance))
    }

    private fun lineToNumbers(line: String): List<Long> {
        return numbersRegex.findAll(line).map { match -> match.value.toLong() }.toList()
    }

    private fun isLineSafeWithErrorTolerance(numbers: List<Long>): Boolean {
        if (isSafe(numbers)) return true

        numbers.indices.forEach { index ->
            val modifiedNumbers = numbers.toMutableList().also { it.removeAt(index) }
            if (isSafe(modifiedNumbers)) return true
        }

        return false
    }

    private fun isSafe(values: List<Long>): Boolean {
        var isIncreasing = false
        var isDecreasing = false

        values.zipWithNext().forEach { (prev, current) ->
            if (prev > current) isDecreasing = true
            if (current > prev) isIncreasing = true
            if (abs(prev - current) !in 1..3) return false
            if (isDecreasing && isIncreasing) return false
        }
        return true
    }
}