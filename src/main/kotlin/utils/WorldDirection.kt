package org.example.utils

internal sealed class WorldDirection {
    abstract fun offset(): Pair<Int, Int>

    data object South : WorldDirection() {
        override fun offset(): Pair<Int, Int> = Pair(0, 1)
    }

    data object SouthWest : WorldDirection() {
        override fun offset(): Pair<Int, Int> = Pair(-1, 1)
    }

    data object SouthEast : WorldDirection() {
        override fun offset(): Pair<Int, Int> = Pair(1, 1)
    }

    data object North : WorldDirection() {
        override fun offset(): Pair<Int, Int> = Pair(0, -1)
    }

    data object NorthWest : WorldDirection() {
        override fun offset(): Pair<Int, Int> = Pair(-1, -1)
    }

    data object NorthEast : WorldDirection() {
        override fun offset(): Pair<Int, Int> = Pair(1, -1)
    }

    data object East : WorldDirection() {
        override fun offset(): Pair<Int, Int> = Pair(1, 0)
    }

    data object West : WorldDirection() {
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