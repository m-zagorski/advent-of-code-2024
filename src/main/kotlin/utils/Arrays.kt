package org.example.utils

internal fun List<String>.toArray(): Array<IntArray> {
    val arr: Array<IntArray> = Array(size) { IntArray(this[0].length) }

    forEachIndexed { y, row ->
        row.asIterable().forEachIndexed { x, c ->
            arr[y][x] = c.digitToInt()
        }
    }

    return arr
}

internal fun <T> print2DArray(array2D: Array<Array<T>>) {
    for (row in array2D) {
        println(row.joinToString(separator = " "))
    }
}

internal fun print2DInArray(array2D: Array<IntArray>) {
    for (row in array2D) {
        println(row.joinToString(separator = " "))
    }
}

internal fun List<String>.toCharMatrix(): Array<Array<Char>> {
    val matrix = Array(size) { Array(first().length) { '.' } }
    forEachIndexed { y, l ->
        l.forEachIndexed { x, c ->
            matrix[y][x] = c
        }
    }
    return matrix
}

internal fun <T> Array<Array<T>>.inRange(x: Int, y: Int): Boolean {
    return !(x < 0 || x >= first().size || y < 0 || y >= size)
}

internal fun <T> Array<Array<T>>.getSafe(x: Int, y: Int, default: T? = null): T? {
    return if (inRange(x, y)) this[y][x]
    else default
}

internal fun <T> Array<Array<T>>.getSafe(coordinates: Pair<Int, Int>, default: T? = null): T? {
    val (x, y) = coordinates
    return getSafe(x, y, default)
}