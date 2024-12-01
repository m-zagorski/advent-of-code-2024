package org.example.days

import org.example.utils.AdventDay
import kotlin.math.abs

internal object Day1 : AdventDay {
    override fun part1(input: List<String>) {
        val l = mutableListOf<Int>()
        val r = mutableListOf<Int>()
        input.forEach { line ->
            line.split(' ').let {
                l.add(it.first().toInt())
                r.add(it.last().toInt())
            }
        }

        val ls = l.sorted()
        val rs = r.sorted()
        val result = ls.zip(rs).sumOf { (first, second) ->
            abs(first - second)
        }
        print(result)
    }

    override fun part2(input: List<String>) {
        val l = mutableListOf<Int>()
        val r = mutableListOf<Int>()
        input.forEach { line ->
            line.split(' ').let {
                l.add(it.first().toInt())
                r.add(it.last().toInt())
            }
        }

        val freq = r.groupingBy { it }.eachCount()

        val result = l.sumOf { x ->
            x * freq.getOrDefault(x, 0)
        }

        print(result)
    }
}