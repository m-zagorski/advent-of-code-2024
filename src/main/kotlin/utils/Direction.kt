package org.example.utils

enum class Direction(val ox: Int, val oy: Int) {
    UP(0, -1), DOWN(0, 1), RIGHT(1, 0), LEFT(-1, 0);

    fun nextDirection(): Direction {
        return when (this) {
            UP -> RIGHT
            DOWN -> LEFT
            RIGHT -> DOWN
            LEFT -> UP
        }
    }
}