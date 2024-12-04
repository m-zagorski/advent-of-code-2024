package org.example.utils

internal sealed class Direction {
    abstract fun offset(): Pair<Int, Int>

    data object South : Direction() {
        override fun offset(): Pair<Int, Int> = Pair(0, 1)
    }

    data object SouthWest : Direction() {
        override fun offset(): Pair<Int, Int> = Pair(-1, 1)
    }

    data object SouthEast : Direction() {
        override fun offset(): Pair<Int, Int> = Pair(1, 1)
    }

    data object North : Direction() {
        override fun offset(): Pair<Int, Int> = Pair(0, -1)
    }

    data object NorthWest : Direction() {
        override fun offset(): Pair<Int, Int> = Pair(-1, -1)
    }

    data object NorthEast : Direction() {
        override fun offset(): Pair<Int, Int> = Pair(1, -1)
    }

    data object East : Direction() {
        override fun offset(): Pair<Int, Int> = Pair(1, 0)
    }

    data object West : Direction() {
        override fun offset(): Pair<Int, Int> = Pair(-1, 0)
    }

    fun toNewCoordinate(x: Int, y: Int, offset: Int? = null): Pair<Int, Int> {
        return if (offset == null) {
            x + offset().first to y + offset().second
        } else {
            return x + (offset().first * offset) to y + (offset().second * offset)
        }
    }

    companion object {
        val WITH_DIAGONAL = listOf(
            North, NorthEast, NorthWest,
            South, SouthEast, SouthWest,
            West, East
        )
        val ALL = listOf(North, South, West, East)
    }
}