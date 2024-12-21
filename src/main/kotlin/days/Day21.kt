package org.example.days

import org.example.utils.AdventDay
import org.example.utils.Direction
import org.example.utils.Position
import java.util.*

object Day21 : AdventDay {
    val keyboard = mapOf(
        "7" to Position(0,0),
        "8" to Position(1, 0),
        "9" to Position(2,0),
        "4" to Position(0, 1),
        "5" to Position(1, 1),
        "6" to Position(2, 1),
        "1" to Position(0, 2),
        "2" to Position(1, 2),
        "3" to Position(2, 2),
        " " to Position(0, 3),
        "0" to Position(1, 3),
        "A" to Position(2, 3)
    )

    val directionalKeyboard = mapOf(
        " " to Position(0, 0),
        "^" to Position(1, 0),
        "A" to Position(2, 0),
        "<" to Position(0, 1),
        "v" to Position(1,1),
        ">" to Position(2,1)
    )


    val directionsToSign = mapOf(
        Direction.UP to "^",
        Direction.LEFT to "<" ,
        Direction.RIGHT to ">",
        Direction.DOWN to "v"
    )

    override fun part1(input: List<String>) {
        val c =  input.sumOf { l ->
            val number = l.replace("A", "").toLong()
            val re = calculateShortest(l)
            (number * re)
        }
        println(c)
    }

    private fun calculateShortest(input: String): Long {
        var sp = keyboard.getValue("A")
        val wrong = keyboard.getValue(" ")

        val cache = mutableMapOf<String, List<List<String>>>()
        val total = input.sumOf { current ->
            val ep = keyboard.getValue(current.toString())
            val results = all(sp, ep, wrong) { pos -> pos.keyboardInRange() }
            val shortestPath = results.minBy { it.points.size }
            sp = shortestPath.points.last()
            val fr: List<CellWithDirections> = results.filter { it.points.size == shortestPath.points.size }

            fr.minOf{ r ->
                var result: MutableMap<List<String>, Long> = mutableMapOf()
                result[r.directions] = 1L

                repeat(2) {
                    val nc = mutableMapOf<List<String>, Long>()
                    result.forEach { (k, v) ->
                        val nextRules = calculateForInstruction(k, cache)
                        nextRules.forEach {
                            nc[it] = nc.getOrDefault(it, 0L) + v
                        }
                    }
                    result = nc
                }

                result.map { (k, v) ->
                    k.size.toLong() * v
                }.sum()
            }
        }
        return total
    }

    val bestRoutes = listOf(
        listOf("v", "<", "<", "A"),
        listOf(">", ">", "^", "A")
    )


    private fun calculateForInstruction(s: List<String>, cache: MutableMap<String, List<List<String>>>): List<List<String>> {
        val returnValue = cache.getOrPut(s.joinToString("")) {
            var sp = directionalKeyboard.getValue("A")
            val wrong = directionalKeyboard.getValue(" ")
            val result =  s.map {
                val ep = directionalKeyboard.getValue(it)
                val results = all(sp, ep, wrong) { pos -> pos.numericKeyboardInRange() }
                val shortestPath = results.minBy { it.points.size }
                sp = shortestPath.points.last()
                val filtered = results.filter { it.points.size == shortestPath.points.size }.map { it.directions }
                filtered.first {
                    if (it.size == 4) {
                        bestRoutes.contains(it)
                    } else {
                        true
                    }
                }
            }
            result
        }

        return returnValue
    }


    data class CellWithDirections(val pos: Position, val points: List<Position>, val directions: List<String>, val visited: List<Position> = emptyList())

    private fun all(start: Position, end: Position, wrong: Position, inRange: (Position) -> Boolean): List<CellWithDirections> {

        if(start == end) {
            return listOf(CellWithDirections(start, listOf(end), listOf("A"), listOf(start)))
        }

        val directions = listOf(
            Direction.UP,
            Direction.DOWN,
            Direction.LEFT,
            Direction.RIGHT
        )

        val found = mutableListOf<CellWithDirections>()

        val queue: Queue<CellWithDirections> = LinkedList()
        queue.add(CellWithDirections(start, listOf(), emptyList(), listOf(start)))

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            val (position, path, currentDirections, visited) = current

            if (position == end) {
                found.add(current.copy(directions = currentDirections.plus("A")))
                continue
            }
            for (direction in directions) {
                val np = position.withOffset(direction.ox, direction.oy)

                if (inRange(np) && np != wrong && !visited.contains(np)) {
                    queue.add(CellWithDirections(
                        pos = np,
                        points = path + np,
                        directions = currentDirections + directionsToSign.getValue(direction),
                        visited = visited.plus(np)
                    ))
                }
            }
        }

        return found
    }

    private fun Position.keyboardInRange(): Boolean {
        return x in 0..2 && y >=0 && y < 4
    }

    private fun Position.numericKeyboardInRange(): Boolean {
        return x in 0..2 && y >= 0 && y < 2
    }

    override fun part2(input: List<String>) {
    }
}