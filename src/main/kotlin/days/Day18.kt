package org.example.days

import org.example.utils.*
import org.example.utils.inRange
import java.util.*

object Day18 : AdventDay {
    private const val KILOBYTE = 1024
    private const val SIZE = 70

    override fun part1(input: List<String>) {
        val matrix = Array(SIZE + 1) { Array(SIZE + 1) { '.' } }
        input.take(KILOBYTE).forEach { l ->
            val (x, y) = numbersRegex.findAll(l).map { it.value.toInt() }.toList()
            matrix[y][x] = '#'
        }

        val response = shortestPath(Position(0, 0), Position(SIZE, SIZE), matrix)
        print(response)
    }

    override fun part2(input: List<String>) {
        val matrix = Array(SIZE + 1) { Array(SIZE + 1) { '.' } }
        input.take(KILOBYTE).forEach { l ->
            val (x, y) = numbersRegex.findAll(l).map { it.value.toInt() }.toList()
            matrix[y][x] = '#'
        }
        val left = input.drop(KILOBYTE).map { l ->
            val (x, y) = numbersRegex.findAll(l).map { it.value.toInt() }.toList()
            Position(x, y)
        }.toMutableList()

        val blockingByte = findFirstBlockingByte(left, matrix)
        print("${blockingByte.x},${blockingByte.y}")
    }

    private fun findFirstBlockingByte(left: MutableList<Position>, matrix: Array<Array<Char>>): Position {
        var currentBytePosition: Position
        while (left.isNotEmpty()) {
            val nextBytePosition = left.removeFirst()
            currentBytePosition = nextBytePosition
            matrix[nextBytePosition.y][nextBytePosition.x] = '#'
            val hasPath = hasPath(Position(0, 0), Position(SIZE, SIZE), matrix)
            if (!hasPath) {
                return currentBytePosition
            }
        }
        error("Wrong.")
    }

    private fun shortestPath(start: Position, end: Position, matrix: Array<Array<Char>>): Int {
        return paths(start, end, matrix).minOf { it.size } - 1
    }

    private fun hasPath(start: Position, end: Position, matrix: Array<Array<Char>>): Boolean {
        return paths(start, end, matrix).isNotEmpty()
    }

    private fun paths(start: Position, end: Position, matrix: Array<Array<Char>>): List<List<Position>> {
        val directions = listOf(
            Direction.UP,
            Direction.DOWN,
            Direction.LEFT,
            Direction.RIGHT
        )

        val paths = mutableListOf<List<Position>>()
        val visited = mutableSetOf<Position>()
        val reached = mutableSetOf<Position>()

        val queue: Queue<Cell> = LinkedList()
        queue.add(Cell(start, listOf(start)))

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            val (position, path) = current
            val (x, y) = position

            if (position == end) {
                paths.add(path)
                reached.add(Position(x, y))
                continue
            }
            for (direction in directions) {
                val newX = x + direction.ox
                val newY = y + direction.oy
                val np = position.withOffset(direction.ox, direction.oy)

                if (matrix.inRange(newX, newY) && matrix[newY][newX] != '#' && !visited.contains(np)) {
                    queue.add(Cell(Position(newX, newY), path + Position(newX, newY)))
                    visited.add(np)
                }
            }
        }

        return paths
    }
}