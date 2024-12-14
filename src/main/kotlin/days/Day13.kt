package org.example.days

import org.example.utils.AdventDay
import org.example.utils.numbersRegex

object Day13 : AdventDay {
    override fun part1(input: String) {
        val games = input.split("\n\n")
        println(winPrizes(
            games = games,
            maxMoves = 100,
            prizeValueMapper = { value -> value }
        ))
    }

    override fun part2(input: String) {
        val games = input.split("\n\n")
        println(winPrizes(
            games = games,
            maxMoves = Long.MAX_VALUE,
            prizeValueMapper = { value -> value + 10000000000000L }
        ))
    }

    private fun winPrizes(
        games: List<String>,
        maxMoves: Long,
        prizeValueMapper: (Long) -> Long
    ): Long {
        return games.sumOf { game ->
            val (a, b, prize) = game.split("\n")
            val (ax, ay) = numbersRegex.findAll(a).map { it.value.toLong() }.toList()
            val (bx, by) = numbersRegex.findAll(b).map { it.value.toLong() }.toList()
            val (px, py) = numbersRegex.findAll(prize).map { prizeValueMapper(it.value.toLong()) }.toList()

            val aPressed = ((bx * py) - (by * px)) / ((ay * bx) - (ax * by))
            val bPressed = (py - (ay * aPressed)) / by
            val aResult = aPressed * ax + bPressed * bx
            val bResult = aPressed * ay + bPressed * by

            when {
                aPressed > maxMoves || bPressed > maxMoves -> 0
                aResult == px && bResult == py -> (aPressed * 3) + bPressed
                else -> 0
            }
        }
    }
}