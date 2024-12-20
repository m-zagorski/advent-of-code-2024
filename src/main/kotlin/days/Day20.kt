package org.example.days

import org.example.utils.*
import org.example.utils.inRange
import java.util.*
import kotlin.math.abs

object Day20 : AdventDay {
    override fun part1(input: List<String>) {
        val matrix: Array<Array<Char>> = Array(input.size) { Array(input.first().length) { '.' } }
        var sp = Position(0, 0)
        input.mapIndexed { y, s ->
            s.forEachIndexed { x, c ->
                if (c == '#' || c == 'E') {
                    matrix[y][x] = c
                }
                if (c == 'S') {
                    sp = Position(x, y)
                }
            }
        }

        val result = findPath(sp, matrix)
            .mapIndexed { index, position -> index to position }
            .findCheats(2)

        println(result)
    }

    override fun part2(input: List<String>) {
        val matrix: Array<Array<Char>> = Array(input.size) { Array(input.first().length) { '.' } }
        var sp = Position(0, 0)
        input.mapIndexed { y, s ->
            s.forEachIndexed { x, c ->
                if (c == '#' || c == 'E') {
                    matrix[y][x] = c
                }
                if (c == 'S') {
                    sp = Position(x, y)
                }
            }
        }

        val result = findPath(sp, matrix)
            .mapIndexed { index, position -> index to position }
            .findCheats(20)

        println(result)
    }

    private fun List<Pair<Int, Position>>.findCheats(maxSteps: Int): Int {
        return sumOf { posWithDistance ->
            val (currentDistance, position) = posWithDistance
            val reachableDistances = subList(currentDistance + 1, size)
                .filter { (distance, pos) ->
                    val cellDistance = calculateManhattanDistance(position, pos)
                    val distanceDiff = (distance - currentDistance) - cellDistance

                    cellDistance <= maxSteps && distanceDiff >= 100
                }
                .size

            reachableDistances
        }
    }

    private fun calculateManhattanDistance(p1: Position, p2: Position): Int {
        return abs(p2.x - p1.x) + abs(p2.y - p1.y)
    }

    private fun findPath(start: Position, matrix: Array<Array<Char>>): List<Position> {
        val directions = listOf(
            Direction.UP,
            Direction.DOWN,
            Direction.LEFT,
            Direction.RIGHT
        )
        val visited = mutableSetOf<Position>()
        val queue: Queue<Cell> = LinkedList()
        queue.add(Cell(start, listOf(start)))

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            val (position, path) = current
            val (x, y) = position

            if (matrix[y][x] == 'E') {
                visited.add(Position(x, y))
                return path
            }

            for (direction in directions) {
                val newX = x + direction.ox
                val newY = y + direction.oy
                val np = position.withOffset(direction.ox, direction.oy)

                if (matrix.inRange(newX, newY) && matrix[newY][newX] != '#' && !visited.contains(np)) {
                    queue.add(Cell(np, path + np))
                    visited.add(np)
                }
            }
        }
        error("Wrong.")
    }
}