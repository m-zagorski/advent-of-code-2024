package org.example.days

import org.example.utils.AdventDay
import org.example.utils.Direction
import org.example.utils.getSafe
import org.example.utils.toCharMatrix

object Day4 : AdventDay {

    override fun part1(input: List<String>) {
        val matrix = input.toCharMatrix()
        val xmas = "XMAS".toCharArray()
        val xmasRev = "SAMX".toCharArray()

        var count = 0

        matrix.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == 'X') {
                    Direction.WITH_DIAGONAL.forEach { direction ->
                        val letters = CharArray(4) { '.' }
                        for (i in 0..3) {
                            val l = matrix.getSafe(direction.toNewCoordinate(x, y, i)) ?: break
                            letters[i] = l
                        }
                        if (letters.contentEquals(xmas) || letters.contentEquals(xmasRev)) count++
                    }
                }
            }
        }
        print(count)
    }

    override fun part2(input: List<String>) {
        val matrix = input.toCharMatrix()
        val xmas = listOf("MS", "MS".reversed())

        var count = 0

        matrix.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == 'A') {
                    val ne = matrix.getSafe(Direction.NorthEast.toNewCoordinate(x, y))
                    val nw = matrix.getSafe(Direction.NorthWest.toNewCoordinate(x, y))

                    if (ne != null && nw != null) {
                        val se = matrix.getSafe(Direction.SouthEast.toNewCoordinate(x, y))
                        val sw = matrix.getSafe(Direction.SouthWest.toNewCoordinate(x, y))

                        if (xmas.contains("$ne$sw") && xmas.contains("$nw$se")) {
                            count++
                        }
                    }
                }
            }
        }
        print(count)
    }
}