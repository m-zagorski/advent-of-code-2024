package org.example.days

import org.example.utils.AdventDay
import org.example.utils.WorldDirection
import org.example.utils.numbersWithSignRegex
import java.util.ArrayDeque

object Day14 : AdventDay {
    private const val WIDTH = 101
    private const val HEIGHT = 103

    data class Position(val x: Int, val y: Int)
    data class Robot(val currentPos: Position, val velocity: Position, val maxWidth: Int, val maxHeight: Int) {
        fun move(): Robot {
            val (cx, cy) = currentPos
            var nx = cx + velocity.x
            var ny = cy + velocity.y
            if (nx < 0) {
                nx += maxWidth
            } else if (nx >= maxWidth) {
                nx -= maxWidth
            }
            if (ny < 0) {
                ny += maxHeight
            } else if (ny >= maxHeight) {
                ny -= maxHeight
            }

            return copy(
                currentPos = Position(nx, ny),
                velocity = velocity
            )
        }
    }

    override fun part1(input: List<String>) {
        val robots = input.map { line ->
            val (p, v) = line.split(" ")
            val (px, py) = numbersWithSignRegex.findAll(p).map { it.value.toInt() }.toList()
            val (vx, vy) = numbersWithSignRegex.findAll(v).map { it.value.toInt() }.toList()
            Robot(
                currentPos = Position(px, py),
                velocity = Position(vx, vy),
                maxWidth = WIDTH,
                maxHeight = HEIGHT
            )
        }.map { robot ->
            var tmpRobot = robot
            repeat(100) {
                tmpRobot = tmpRobot.move()
            }
            tmpRobot
        }

        val quadrants = listOf(
            0..49 to 0..50,
            51..100 to 0..50,
            0..49 to 52..102,
            51..100 to 52..102
        )

        val result = quadrants.fold(1) { acc, range ->
            val (xRange, yRange) = range
            val inQuadrant = robots.filter { robot ->
                xRange.contains(robot.currentPos.x) && yRange.contains(robot.currentPos.y)
            }.size
            acc * inQuadrant
        }

        print(result)
    }

    override fun part2(input: List<String>) {
        val robots = input.map { line ->
            val (p, v) = line.split(" ")
            val (px, py) = numbersWithSignRegex.findAll(p).map { it.value.toInt() }.toList()
            val (vx, vy) = numbersWithSignRegex.findAll(v).map { it.value.toInt() }.toList()
            Robot(
                currentPos = Position(px, py),
                velocity = Position(vx, vy),
                maxWidth = WIDTH,
                maxHeight = HEIGHT
            )
        }

        val maxCombinations = WIDTH * HEIGHT
        var tmpRobots = robots
        var time = 1
        val repeating = mutableListOf<Int>()
        var biggestGroup = 0
        var biggestGroupTime = 0
        while (repeating.size != maxCombinations) {
            val output = tmpRobots.map { robot -> robot.move() }
            val outputHash = output.hashCode()

            if (repeating.contains(outputHash)) continue
            else {
                repeating.add(outputHash)
                val currentBiggestGroup = output.map { it.currentPos }.findGroups()
                if (currentBiggestGroup > biggestGroup) {
                    biggestGroup = currentBiggestGroup
                    biggestGroupTime = time
                }
            }

            tmpRobots = output
            time++
        }
        print(biggestGroupTime)
    }


    private fun List<Position>.findGroups(): Int {
        val visited = mutableListOf<Position>()
        val worldDirections = WorldDirection.WITH_DIAGONAL

        fun bfs(start: Position): Int {
            val queue = ArrayDeque<Position>()
            val group = mutableSetOf<Position>()

            queue.add(start)
            visited.add(start)

            while (queue.isNotEmpty()) {
                val (x, y) = queue.removeFirst()
                group.add(Position(x, y))

                for (direction in worldDirections) {
                    val (nx, ny) = direction.toNewCoordinate(x, y)
                    val np = Position(nx, ny)
                    if (contains(np) && !visited.contains(np)) {
                        queue.add(np)
                        visited.add(np)
                    }
                }
            }

            return group.size
        }

        var biggestGroup = 0
        forEach { position ->
            if (!visited.contains(position)) {
                val groupSize = bfs(position)
                if (groupSize > biggestGroup) {
                    biggestGroup = groupSize
                }
            }
        }
        return biggestGroup
    }
}