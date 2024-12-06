package org.example.days

import org.example.utils.AdventDay
import org.example.utils.Direction
import org.example.utils.inRange

object Day6 : AdventDay {

    data class Position(val x: Int, val y: Int)

    data class PositionWithDirection(val position: Position, val direction: Direction)

    override fun part1(input: List<String>) {
        val matrix = Array(input.size) { Array(input.first().length) { '.' } }
        var startPosition = PositionWithDirection(Position(0, 0), Direction.UP)
        input.forEachIndexed { y, l ->
            l.forEachIndexed { x, c ->
                matrix[y][x] = c
                when (c) {
                    '^' -> startPosition = PositionWithDirection(Position(x, y), Direction.UP)
                    '>' -> startPosition = PositionWithDirection(Position(x, y), Direction.RIGHT)
                    '<' -> startPosition = PositionWithDirection(Position(x, y), Direction.LEFT)
                    'V' -> startPosition = PositionWithDirection(Position(x, y), Direction.DOWN)
                }
            }
        }
        val visitedPositions = matrix.visitedPositionsAfterLeaving(startPosition) + 1

        print(visitedPositions.size)
    }

    override fun part2(input: List<String>) {
        val matrix = Array(input.size) { Array(input.first().length) { '.' } }
        var startPosition = PositionWithDirection(Position(0, 0), Direction.UP)
        input.forEachIndexed { y, l ->
            l.forEachIndexed { x, c ->
                matrix[y][x] = c
                when (c) {
                    '^' -> startPosition = PositionWithDirection(Position(x, y), Direction.UP)
                    '>' -> startPosition = PositionWithDirection(Position(x, y), Direction.RIGHT)
                    '<' -> startPosition = PositionWithDirection(Position(x, y), Direction.LEFT)
                    'V' -> startPosition = PositionWithDirection(Position(x, y), Direction.DOWN)
                }
            }
        }

        val visitedPositions = matrix.visitedPositionsAfterLeaving(startPosition)

        val result = visitedPositions.count { po ->
            matrix[po.y][po.x] = '#'
            isInLoop(startPosition, matrix).also { matrix[po.y][po.x] = '.' }
        }
        print(result)
    }

    private fun isInLoop(position: PositionWithDirection, matrix: Array<Array<Char>>): Boolean {
        val visitedPositions = mutableSetOf(position)
        var startPosition = position
        var stopWalking = false
        var inLoop = false

        while (!stopWalking) {
            val (coord, direction) = startPosition
            val (x, y) = coord
            val nx = x + direction.ox
            val ny = y + direction.oy

            if (matrix.inRange(nx, ny)) {
                if (matrix[ny][nx] == '#') {
                    startPosition = PositionWithDirection(coord, direction.nextDirection())
                } else {
                    startPosition = PositionWithDirection(Position(nx, ny), direction)
                    if (visitedPositions.contains(startPosition)) {
                        inLoop = true
                        stopWalking = true
                    }
                    visitedPositions.add(PositionWithDirection(Position(nx, ny), direction))
                }
            } else {
                stopWalking = true
            }
        }

        return inLoop
    }

    private fun Array<Array<Char>>.visitedPositionsAfterLeaving(position: PositionWithDirection): Set<Position> {
        var outsideOutMap = false
        var startPosition = position
        val visitedPositions = mutableSetOf<Position>()
        while (!outsideOutMap) {
            val (coord, direction) = startPosition
            val (x, y) = coord
            val nx = x + direction.ox
            val ny = y + direction.oy
            if (inRange(nx, ny)) {
                if (this[ny][nx] == '#') {
                    startPosition = PositionWithDirection(coord, direction.nextDirection())
                } else {
                    startPosition = PositionWithDirection(Position(nx, ny), direction)
                    visitedPositions.add(Position(nx, ny))
                }
            } else {
                outsideOutMap = true
            }
        }

        return visitedPositions
    }
}