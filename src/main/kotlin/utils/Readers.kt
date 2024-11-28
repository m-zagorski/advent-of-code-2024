package org.example.utils

import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun readLines(dayNumber: String): List<String> {
    return Path("src/input/day_$dayNumber.txt").readLines()
}

fun readFile(dayNumber: String): String {
    return File("src/input/day_$dayNumber.txt").readText()
}