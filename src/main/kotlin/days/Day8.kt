package org.example.days

import org.example.days.Day8.Position
import org.example.utils.AdventDay

object Day8 : AdventDay {
    data class Position(val x: Int, val y: Int)

    override fun part1(input: List<String>) {
        val matrix = Array(input.size) { Array(input.first().length) { '.' } }
        val antennas = mutableMapOf<Char, List<Position>>()
        input.forEachIndexed { y, l ->
            l.forEachIndexed { x, c ->
                if (c != '.') {
                    val currentCoords = antennas.getOrDefault(c, emptyList())
                    antennas += c to currentCoords.plus(Position(x, y))
                }
            }
        }
        val antinodes = mutableSetOf<Position>()
        antennas.forEach { (_, positions) ->
            val pos = createAllPairs(positions)
            pos.forEach { (p1, p2) ->
                val dx1 = p1.x - p2.x
                val dy1 = p1.y - p2.y

                Position(p1.x + dx1, p1.y + dy1)
                    .takeIf(matrix::inRange)?.let(antinodes::add)
                Position(p2.x - dx1, p2.y - dy1)
                    .takeIf(matrix::inRange)?.let(antinodes::add)
            }
        }
        print(antinodes.size)
    }

    override fun part2(input: List<String>) {
        val matrix = Array(input.size) { Array(input.first().length) { '.' } }
        val antennas = mutableMapOf<Char, List<Position>>()
        input.forEachIndexed { y, l ->
            l.forEachIndexed { x, c ->
                if (c != '.') {
                    val currentCoords = antennas.getOrDefault(c, emptyList())
                    antennas += c to currentCoords.plus(Position(x, y))
                }
            }
        }
        val antinodes = mutableSetOf<Position>()
        antennas.forEach { (_, positions) ->
            val pos = createAllPairs(positions)
            pos.forEach { (p1, p2) ->
                antinodes.add(p1)
                antinodes.add(p2)
                val dx1 = p1.x - p2.x
                val dy1 = p1.y - p2.y

                var nn1 = Position(p1.x + dx1, p1.y + dy1)
                while (matrix.inRange(nn1)) {
                    antinodes.add(nn1)
                    nn1 = Position(nn1.x + dx1, nn1.y + dy1)
                }

                var nn2 = Position(p1.x - dx1, p1.y - dy1)
                while (matrix.inRange(nn2)) {
                    antinodes.add(nn2)
                    nn2 = Position(nn2.x - dx1, nn2.y - dy1)
                }
            }
        }
        print(antinodes.size)
    }

    private fun createAllPairs(points: List<Position>): List<Pair<Position, Position>> {
        val pairs = mutableListOf<Pair<Position, Position>>()
        for (i in points.indices) {
            for (j in i + 1 until points.size) {
                pairs.add(Pair(points[i], points[j]))
            }
        }
        return pairs
    }
}

internal fun <T> Array<Array<T>>.inRange(position: Position): Boolean {
    return !(position.x < 0 || position.x >= first().size || position.y < 0 || position.y >= size)
}