package org.example.days

import org.example.utils.AdventDay
import org.example.utils.Direction
import org.example.utils.inRange
import java.util.*

object Day10 : AdventDay {
    data class Position(val x: Int, val y: Int)

    override fun part1(input: List<String>) {
        val (matrix, startPosition) = createMatrixAndStartingPoints(input)

        val result = startPosition.sumOf { pos ->
            bfs(pos, matrix, false)
        }

        print(result)
    }

    override fun part2(input: List<String>) {
        val (matrix, startPosition) = createMatrixAndStartingPoints(input)

        val result = startPosition.sumOf { pos ->
            bfs(pos, matrix, true)
        }

        print(result)
    }

    private fun createMatrixAndStartingPoints(input: List<String>): Pair<Array<Array<Int>>, List<Position>> {
        val matrix = Array(input.size) { Array(input.first().length) { -1 } }
        val startPositions = mutableListOf<Position>()
        input.mapIndexed { y, s ->
            s.forEachIndexed { x, c ->
                if (c != '.') {
                    matrix[y][x] = c.digitToInt()
                } else {
                    matrix[y][x] = -1
                }
                if (c == '0') {
                    startPositions.add(Position(x, y))
                }
            }
        }
        return matrix to startPositions
    }

    data class Cell(val pos: Position, val points: List<Position>)

    private fun bfs(start: Position, matrix: Array<Array<Int>>, returnAllPaths: Boolean): Int {
        val directions = listOf(
            Direction.UP,
            Direction.DOWN,
            Direction.LEFT,
            Direction.RIGHT
        )

        val paths = mutableListOf<List<Position>>()
        val reached = mutableSetOf<Position>()

        val queue: Queue<Cell> = LinkedList()
        queue.add(Cell(start, listOf(start)))

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            val (position, path) = current
            val (x, y) = position

            if (matrix[y][x] == 9) {
                paths.add(path)
                reached.add(Position(x, y))
                continue
            }
            for (direction in directions) {
                val newX = x + direction.ox
                val newY = y + direction.oy

                if (matrix.inRange(newX, newY) && matrix[newY][newX] == matrix[y][x] + 1) {
                    queue.add(Cell(Position(newX, newY), path + Position(newX, newY)))
                }
            }
        }

        return if (returnAllPaths) paths.size
        else reached.size
    }
}