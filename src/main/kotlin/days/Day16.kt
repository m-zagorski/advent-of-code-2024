package org.example.days

import org.example.utils.AdventDay
import org.example.utils.Direction
import org.example.utils.Position
import java.util.*
import kotlin.collections.HashMap

object Day16 : AdventDay {

    override fun part1(input: List<String>) {
        var sp = Position(0, 0)
        val matrix = Array(input.size) { Array(input.first().length) { '.' } }
        input.forEachIndexed { y, l ->
            l.forEachIndexed { x, c ->
                matrix[y][x] = c
                if (c == 'S') {
                    sp = Position(x, y)
                }
            }
        }
        print(bestPath(matrix, sp))
    }

    override fun part2(input: List<String>) {
        var sp = Position(0, 0)
        val matrix = Array(input.size) { Array(input.first().length) { '.' } }
        input.forEachIndexed { y, l ->
            l.forEachIndexed { x, c ->
                matrix[y][x] = c
                if (c == 'S') {
                    sp = Position(x, y)
                }
            }
        }
        print(allPaths(matrix, sp).flatten().map { it.position }.toSet().size + 1) // +1 because of initial position
    }

    data class CellState(
        val position: Position,
        val direction: Direction,
        val cost: Long,
        val path: List<PositionWithDirection>,
    )

    data class PositionWithDirection(val position: Position, val direction: Direction)

    private fun bestPath(matrix: Array<Array<Char>>, sp: Position): Long {
        val visited = mutableSetOf<Position>()
        val queue = PriorityQueue<CellState>(compareBy { it.cost })
        val initialState = CellState(sp, Direction.RIGHT, 0, emptyList())

        queue.add(initialState)
        visited.add(initialState.position)

        while (queue.isNotEmpty()) {
            val cell = queue.poll()
            val char = matrix[cell.position.y][cell.position.x]
            if (char == 'E') {
                return cell.cost
            }

            cell.direction.nextDirectionsWithCost().forEach { (direction, cost) ->
                val np = cell.position.withOffset(direction.ox, direction.oy)
                if (matrix[np.y][np.x] != '#') {
                    val newCell =
                        CellState(np, direction, cell.cost + cost, cell.path.plus(PositionWithDirection(np, direction)))
                    if (!visited.contains(newCell.position)) {
                        queue.add(newCell)
                        visited.add(newCell.position)
                    }
                }
            }
        }
        error("DONT.")
    }

    private fun allPaths(matrix: Array<Array<Char>>, sp: Position): MutableList<List<PositionWithDirection>> {
        val visited = HashMap<PositionWithDirection, Long>()
        val queue = PriorityQueue<CellState>(compareBy { it.cost })

        val initialState = CellState(sp, Direction.RIGHT, 0, emptyList())
        queue.add(initialState)

        val foundPaths = mutableListOf<List<PositionWithDirection>>()

        while (queue.isNotEmpty()) {
            val cell = queue.poll()
            val char = matrix[cell.position.y][cell.position.x]

            if (char == 'E') {
                foundPaths.add(cell.path)
                continue
            }

            val alreadyVisitedCost = visited.getOrDefault(PositionWithDirection(cell.position, cell.direction), Long.MAX_VALUE)
            if (alreadyVisitedCost < cell.cost) continue

            visited[PositionWithDirection(cell.position, cell.direction)] = cell.cost

            cell.direction.nextDirectionsWithCost().forEach { (direction, cost) ->
                val np = cell.position.withOffset(direction.ox, direction.oy)
                if (matrix[np.y][np.x] != '#') {
                    CellState(np, direction, cell.cost + cost, cell.path.plus(PositionWithDirection(np, direction)))
                        .also(queue::add)
                }
            }
        }
        return foundPaths
    }

    private fun Direction.nextDirectionsWithCost(): List<Pair<Direction, Int>> {
        return when (this) {
            Direction.UP -> listOf(Direction.UP to 1, Direction.RIGHT to 1001, Direction.LEFT to 1001)
            Direction.DOWN -> listOf(Direction.DOWN to 1, Direction.RIGHT to 1001, Direction.LEFT to 1001)
            Direction.RIGHT -> listOf(Direction.RIGHT to 1, Direction.UP to 1001, Direction.DOWN to 1001)
            Direction.LEFT -> listOf(Direction.LEFT to 1, Direction.UP to 1001, Direction.DOWN to 1001)
        }
    }
}