package org.example.days

import org.example.utils.AdventDay

object Day11 : AdventDay {
    override fun part1(input: String) {
        var output = input.split(" ").map { it.toLong() }
        repeat(25) {
            output = output.map(::stoneRules).flatten()
        }
        print(output.size)
    }


    override fun part2(input: String) {
        val stones = input.split(" ").map { it.toLong() }
        var result = stones.associateWith { 1L }.toMutableMap()
        repeat(75) {
            val nc = mutableMapOf<Long, Long>()
            result.forEach { (k, v) ->
                val nextStones = stoneRules(k)
                nextStones.forEach {
                    nc[it] = nc.getOrDefault(it, 0L) + v
                }
            }
            result = nc
        }
        print(result.values.sum())
    }

    private fun stoneRules(stone: Long): List<Long> {
        val stringRepresentation = stone.toString()
        return when {
            stone == 0L -> listOf(1)
            stringRepresentation.length % 2 == 0 -> {
                val middle = stringRepresentation.length / 2
                val left = stringRepresentation.substring(0, middle).toLong()
                val right = stringRepresentation.substring(middle, stringRepresentation.length).toLong()
                listOf(left, right)
            }

            else -> listOf(stone * 2024)
        }
    }
}