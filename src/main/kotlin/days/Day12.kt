package org.example.days

import org.example.utils.AdventDay
import org.example.utils.Direction
import org.example.utils.inRange
import org.example.utils.toCharMatrix
import java.util.*

object Day12 : AdventDay {
    data class Position(val x: Int, val y: Int)
    data class PointWithDirection(val pos: Position, val direction: Direction)

    private val directions = listOf(
        Direction.UP,
        Direction.DOWN,
        Direction.LEFT,
        Direction.RIGHT
    )

    override fun part1(input: List<String>) {
        val matrix: Array<Array<Char>> = input.toCharMatrix()
        println(matrix.findGroups(false))
    }

    override fun part2(input: List<String>) {
        val matrix: Array<Array<Char>> = input.toCharMatrix()
        println(matrix.findGroups(true))
    }

    private fun Array<Array<Char>>.findGroups(withFullFence: Boolean): Long {
        val visited = mutableSetOf<Position>()
        var sum = 0L

        fun bfs(matrix: Array<Array<Char>>, start: Position) {
            val queue = ArrayDeque<Position>()
            val group = mutableSetOf<Position>()
            val char = matrix[start.y][start.x]

            val tmp = mutableSetOf<Position>()
            tmp.add(start)
            queue.add(start)
            visited.add(start)

            val perimeter = mutableListOf<PointWithDirection>()

            while (queue.isNotEmpty()) {
                val (x, y) = queue.removeFirst()
                group.add(Position(x, y))

                for (direction in directions) {
                    val nx = x + direction.ox
                    val ny = y + direction.oy
                    val n = Position(nx, ny)

                    if (matrix.inRange(nx, ny) && !visited.contains(n) && matrix[ny][nx] == char) {
                        visited.add(n)
                        queue.add(n)
                        tmp.add(n)
                    } else {
                        if (!tmp.contains(n)) {
                            val pd = if (x == nx) {
                                val yDiff = ny - y
                                if (yDiff < 0) Direction.UP
                                else Direction.DOWN
                            } else if (y == ny) {
                                val xDiff = nx - x
                                if (xDiff < 0) Direction.LEFT
                                else Direction.RIGHT
                            } else {
                                error("Should not happen")
                            }
                            perimeter.add(PointWithDirection(n, pd))
                        }
                    }
                }
            }

            if (withFullFence) {
                val afterSort = perimeter.sortedWith(compareBy({ it.pos.x }, { it.pos.y }))
                val fence = calculateFence(afterSort)
                sum += group.size * fence
            } else {
                sum += group.size * perimeter.size
            }
        }

        forEachIndexed { y, row ->
            for (x in row.indices) {
                val startPosition = Position(x, y)
                if (startPosition !in visited) {
                    bfs(this, startPosition)
                }
            }
        }

        return sum
    }

    private fun calculateFence(points: List<PointWithDirection>): Int {
        fun travelFence(start: PointWithDirection, points: List<PointWithDirection>): Set<PointWithDirection> {
            val queue = ArrayDeque<Position>()
            val path = mutableSetOf<PointWithDirection>()
            path.add(start)
            queue.add(start.pos)

            val pathDirection = directions.firstOrNull { direction ->
                val (x, y) = start.pos
                val nx = x + direction.ox
                val ny = y + direction.oy
                val np = Position(nx, ny)
                points.contains(PointWithDirection(np, start.direction))
            }

            pathDirection?.let { direction ->
                var go = true
                var (x, y) = start.pos
                while (go) {
                    val nx = x + direction.ox
                    val ny = y + direction.oy
                    val np = Position(nx, ny)
                    val pwd = PointWithDirection(np, start.direction)
                    if (points.contains(pwd)) {
                        path.add(pwd)
                        x = nx
                        y = ny
                    } else {
                        go = false
                    }
                }
            }
            return path
        }


        val currentPoints: MutableList<PointWithDirection> = points.toMutableList()
        var count = 0
        while (currentPoints.isNotEmpty()) {
            val cp = travelFence(currentPoints.first(), currentPoints)
            cp.forEach {
                currentPoints.remove(it)
            }
            count++
        }

        return count
    }
}