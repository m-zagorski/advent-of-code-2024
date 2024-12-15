package org.example.days

import org.example.utils.AdventDay
import org.example.utils.Position

object Day15 : AdventDay {

    override fun part1(input: String) {
        val (board, moves) = input.split("\n\n")
        val boardSplit = board.split("\n")

        var currentPosition = Position(0, 0)
        val walls = mutableListOf<Position>()
        val boxes = mutableListOf<Position>()

        boardSplit.forEachIndexed { y, l ->
            l.forEachIndexed { x, c ->
                when (c) {
                    '@' -> currentPosition = Position(x, y)
                    'O' -> boxes.add(Position(x, y))
                    '#' -> walls.add(Position(x, y))
                }
            }
        }

        for (move in moves.replace("\n", "")) {
            val (ox, oy) = getOffsetBasedOnMovement(move)

            val newPosition = currentPosition.withOffset(ox, oy)

            if (walls.contains(newPosition)) continue

            if (boxes.contains(newPosition)) {
                val movedBoxes = boxes.getMovedBoxes(newPosition, walls, ox to oy)
                if (movedBoxes.isNotEmpty()) {
                    currentPosition = newPosition
                    boxes.removeAll(movedBoxes)
                    boxes.addAll(movedBoxes.map { block -> block.withOffset(ox, oy) })
                }
            } else {
                currentPosition = newPosition
            }
        }

        val sumOf = boxes.sumOf { box ->
            box.y * 100 + box.x
        }
        print(sumOf)
    }

    private fun MutableList<Position>.getMovedBoxes(
        np: Position,
        walls: List<Position>,
        offset: Pair<Int, Int>
    ): List<Position> {
        val movedBlocks = mutableListOf(np)
        val (ox, oy) = offset
        var currentPosition = np

        while (true) {
            val nextPosition = currentPosition.withOffset(ox, oy)
            if (walls.contains(nextPosition)) {
                return emptyList()
            }

            if (contains(nextPosition)) {
                movedBlocks.add(nextPosition)
                currentPosition = nextPosition
            } else {
                return movedBlocks
            }
        }
    }


    data class Box(
        val sp: Position,
        val ep: Position
    ) {
        fun move(ox: Int, oy: Int): Box {
            return copy(
                sp = Position(sp.x + ox, sp.y + oy),
                ep = Position(ep.x + ox, ep.y + oy)
            )
        }
    }

    override fun part2(input: String) {
        val (board, moves) = input.split("\n\n")
        val boardSplit = board.split("\n")
        val matrix = Array(boardSplit.size) { Array(boardSplit.first().length * 2) { '.' } }

        boardSplit.forEachIndexed { y, l ->
            var xOffset = 0
            l.forEachIndexed { x, c ->
                val nx = x + xOffset
                when (c) {
                    '#' -> {
                        matrix[y][nx] = '#'
                        matrix[y][nx + 1] = '#'
                    }

                    '.' -> {
                        matrix[y][nx] = '.'
                        matrix[y][nx + 1] = '.'
                    }

                    '@' -> {
                        matrix[y][nx] = '@'
                        matrix[y][nx + 1] = '.'
                    }

                    'O' -> {
                        matrix[y][nx] = '['
                        matrix[y][nx + 1] = ']'
                    }
                }
                xOffset++
            }
        }

        var startPosition = Position(0, 0)
        val walls = mutableListOf<Position>()
        val tmpBoxes = mutableSetOf<Box>()

        matrix.forEachIndexed { y, l ->
            l.forEachIndexed { x, c ->
                when (c) {
                    '@' -> startPosition = Position(x, y)
                    '#' -> walls.add(Position(x, y))
                    '[' -> {
                        tmpBoxes.add(
                            Box(
                                sp = Position(x, y),
                                ep = Position(x + 1, y)
                            )
                        )
                    }

                    ']' -> {
                        tmpBoxes.add(
                            Box(
                                sp = Position(x - 1, y),
                                ep = Position(x, y)
                            )
                        )
                    }
                }
            }
        }

        val boxes = tmpBoxes.toMutableList()

        for (move in moves.replace("\n", "")) {
            val (ox, oy) = getOffsetBasedOnMovement(move)

            val newPosition = startPosition.withOffset(ox, oy)

            if (walls.contains(newPosition)) continue

            if (boxes.isBox(newPosition)) {
                val boxesMoved = boxes.getMovedBoxes(newPosition, walls, ox to oy, move)
                if (boxesMoved.isNotEmpty()) {
                    startPosition = newPosition
                    boxes.removeAll(boxesMoved)
                    boxes.addAll(boxesMoved.map { box -> box.move(ox, oy) })
                }
            } else {
                startPosition = newPosition
            }
        }

        val sumOf = boxes.sumOf { box ->
            box.sp.y * 100 + box.sp.x
        }
        print(sumOf)
    }

    private fun MutableList<Box>.getMovedBoxes(
        np: Position,
        walls: List<Position>,
        offset: Pair<Int, Int>,
        direction: Char
    ): List<Box> {
        return when (direction) {
            '<', '>' -> getAllBoxesHorizontal(np, walls, offset)
            else -> getAllBoxesVertical(np, walls, offset)
        }
    }

    private fun MutableList<Box>.getAllBoxesVertical(
        np: Position,
        walls: List<Position>,
        offset: Pair<Int, Int>
    ): List<Box> {
        val box = boxAt(np)
        val movedBoxes = mutableListOf(box)
        val (ox, oy) = offset
        var nextBoxes = listOf(box.move(ox, oy))

        while (true) {
            if (walls.containsBox(nextBoxes)) return emptyList()

            val adjacentBoxes: List<Box> = nextBoxesOrEmpty(nextBoxes)
            if (adjacentBoxes.isNotEmpty()) {
                movedBoxes.addAll(adjacentBoxes)
                nextBoxes = adjacentBoxes.map { it.move(ox, oy) }
            } else {
                return movedBoxes
            }
        }
    }

    private fun MutableList<Box>.getAllBoxesHorizontal(
        np: Position,
        walls: List<Position>,
        offset: Pair<Int, Int>,
    ): List<Box> {
        val box = boxAt(np)
        val movedBoxes = mutableListOf(box)
        val (ox, _) = offset

        fun movePosition(b: Box): Position {
            return if (ox == -1) {
                b.sp.copy(x = b.sp.x - 1)
            } else {
                b.ep.copy(x = b.ep.x + 1)
            }
        }

        var position = movePosition(box)

        while (true) {
            if (walls.contains(position)) return emptyList()

            val nextBox = nextBoxOrNull(position)
            if (nextBox != null) {
                movedBoxes.add(nextBox)
                position = movePosition(nextBox)
            } else {
                return movedBoxes
            }
        }
    }

    private fun List<Box>.isBox(p: Position): Boolean {
        return any { box ->
            box.sp == p || box.ep == p
        }
    }

    private fun List<Position>.containsBox(boxes: List<Box>): Boolean {
        return any { wall ->
            boxes.any { box -> box.sp == wall || box.ep == wall }
        }
    }

    private fun List<Box>.nextBoxesOrEmpty(boxes: List<Box>): List<Box> {
        return filter {
            boxes.any { box ->
                it.sp == box.sp || it.sp == box.ep || it.ep == box.sp || it.ep == box.ep
            }
        }
    }

    private fun List<Box>.nextBoxOrNull(p: Position): Box? {
        return firstOrNull { box ->
            box.sp == p || box.ep == p
        }
    }

    private fun List<Box>.boxAt(p: Position): Box {
        return first { it.ep == p || it.sp == p }
    }

    private fun getOffsetBasedOnMovement(move: Char) = when (move) {
        '>' -> Pair(1, 0)
        '<' -> Pair(-1, 0)
        '^' -> Pair(0, -1)
        'v' -> Pair(0, 1)
        else -> error("Should not happen $move")
    }
}