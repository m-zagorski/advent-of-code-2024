package org.example.utils

data class Position(val x: Int, val y: Int) {

    fun withOffset(ox: Int, oy: Int): Position {
        return Position(
            x + ox,
            y + oy
        )
    }
}

data class Cell(val pos: Position, val points: List<Position>)