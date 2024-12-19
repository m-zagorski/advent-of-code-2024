package org.example.days

import org.example.utils.AdventDay

object Day19 : AdventDay {
    override fun part1(input: String) {
        val (patterns, designs) = input.split("\n\n")
        val p = patterns.split(", ").toSet()

        val result = designs.split("\n").count { design ->
            canBuild(design, p)
        }
        print(result)
    }

    override fun part2(input: String) {
        val (patterns, designs) = input.split("\n\n")
        val p = patterns.split(", ").toSet()

        val result = designs.split("\n").sumOf { design ->
            findOccurrences(design, p, mutableMapOf())
        }
        print(result)
    }

    private fun findOccurrences(w: String, p: Set<String>, cache: MutableMap<String, Long>): Long {
        if (w.isEmpty()) return 1L
        val inCache = cache.contains(w)
        return if (inCache) {
            cache.getValue(w)
        } else {
            val operators = p.filter { w.startsWith(it) }
            val result = operators.sumOf { findOccurrences(w.removePrefix(it), p, cache) }
            return result.also { cache[w] = it }
        }
    }

    private fun canBuild(word: String, substrings: Set<String>): Boolean {
        val validMap = mutableMapOf<Int, Boolean>()
        validMap[0] = true
        for (i in 1..word.length) {
            for (substring in substrings) {
                if (i >= substring.length && word.substring(i - substring.length, i) == substring) {
                    validMap[i] = validMap.getOrDefault(i, false) || validMap.getOrDefault(i - substring.length, false)
                }
            }
        }

        return validMap.getOrDefault(word.length, false)
    }
}