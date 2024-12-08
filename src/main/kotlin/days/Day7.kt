package org.example.days

import org.example.utils.AdventDay
import org.example.utils.numbersRegex

object Day7 : AdventDay {
    override fun part1(input: List<String>) {
        val signs = listOf("*", "+")

        val result = input
            .map { line ->
                val (r, values) = line.split(":")
                r.toLong() to numbersRegex.findAll(values).toList().map { it.value.toLong() }
            }
            .filter { (result, numbers) ->
                canCreateResult(numbers, result, signs)
            }
            .sumOf { (result, _) -> result }

        print(result)
    }

    override fun part2(input: List<String>) {
        val signs = listOf("*", "+", "|")
        val result = input
            .map { line ->
                val (r, values) = line.split(":")
                r.toLong() to numbersRegex.findAll(values).toList().map { it.value.toLong() }
            }
            .filter { (result, numbers) ->
                canCreateResult(numbers, result, signs)
            }
            .sumOf { (result, _) -> result }

        print(result)
    }

    private fun canCreateResult(numbers: List<Long>, result: Long, signs: List<String>): Boolean {
        val perms = permutations(signs, numbers.size - 1)
        for (currentPermutation in perms) {
            var operationIndex = 0
            val newResult = numbers.drop(1).fold(numbers.first()) { acc, l ->
                val s = currentPermutation[operationIndex].also { operationIndex++ }
                when (s) {
                    "*" -> acc * l
                    "+" -> acc + l
                    "|" -> "$acc$l".toLong()
                    else -> error("Wrong")
                }
            }
            if (newResult == result) {
                return true
            }
        }
        return false
    }

    private fun permutations(operators: List<String>, length: Int): List<List<String>> {
        if (length == 0) return listOf(emptyList())

        val permutations = mutableListOf<List<String>>()
        for (op in operators) {
            val subPermutations = permutations(operators, length - 1)
            for (perm in subPermutations) {
                permutations.add(listOf(op) + perm)
            }
        }
        return permutations
    }
}