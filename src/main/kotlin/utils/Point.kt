package org.example.utils

data class Point(val x: Int, val y: Int) {
    fun inBoard(array: Array<Array<Char>>): Boolean {
        return x >= 0 && x <= array.first().size - 1 && y >= 0 && y <= array.size - 1
    }

    fun applyOffset(offset: Point): Point {
        return Point(
            x = this.x + offset.x,
            y = this.y + offset.y
        )
    }
}