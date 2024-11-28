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