package org.example.days

import org.example.utils.AdventDay

object Day9 : AdventDay {
    interface FileSystem
    data class F(val value: Int) : FileSystem
    object Free : FileSystem

    override fun part1(input: String) {
        var currentIndex = 0
        val output = mutableListOf<FileSystem>()
        input.forEachIndexed { index, c ->
            val i = c.toString().toInt()
            if (index % 2 == 0) {
                val mapped = (0 until i).map {
                    F(currentIndex)
                }
                output.addAll(mapped)
                currentIndex++
            } else {
                output.addAll((0 until i).map { Free })
            }
        }


        val indexesToGo = output.mapIndexed { index, archive -> if (archive is F) index else null }.filterNotNull()
            .reversed()

        indexesToGo.forEach { index ->
            val currentFile = output[index]
            val indexOfFirst = output.indexOfFirst { it is Free }
            if (indexOfFirst != -1 && indexOfFirst <= index) {
                output[indexOfFirst] = currentFile
                output.removeAt(index)
            }
        }

        var result = 0L
        output.filterIsInstance<F>()
            .mapIndexed { index, fileSystem ->
                result += index * fileSystem.value
            }

        print(result)
    }

    private fun part1Raw(input: String) {
        var ci = 0
        val output = mutableListOf<String>()

        input.forEachIndexed { index, c ->
            val i = c.toString().toInt()
            if (index % 2 == 0) {
                output.addAll((0 until i).map { ci.toString() })
                ci++
            } else {
                output.addAll((0 until i).map { "." })
            }
        }
        var stillGo = true
        var replacedIndex = output.indexOfFirst { it == "." }
        val arr: Array<String> = output.toTypedArray()
        while (stillGo) {
            val last = output.last()
            if (last == ".") {
                output.removeLast()
                continue
            }
            arr[replacedIndex] = last
            output.removeLast()
            replacedIndex = arr.indexOfFirst { it == "." }
            arr[output.size] = "."
            val rrrr = arr.toList().subList(replacedIndex, arr.size).any { it.toLongOrNull() != null }
            stillGo = rrrr
        }

        var count = 0L
        arr.forEachIndexed { index, s ->
            if (s.toLongOrNull() != null) {
                count += index * s.toLong()
            }
        }
        println(count)
    }

    interface Archive
    data class File(
        val index: Int,
        val length: Int
    ) : Archive

    data class FreeSpace(
        val length: Int,
        val filesInside: List<File>
    ) : Archive

    override fun part2(input: String) {
        var currentIndex = 0
        val archive = mutableListOf<Archive>()
        input.forEachIndexed { index, c ->
            val i = c.toString().toInt()
            if (index % 2 == 0) {
                archive.add(File(currentIndex, i))
                currentIndex++
            } else {
                archive.add(FreeSpace(i, emptyList()))
            }
        }
        val indexesToGo = archive.mapIndexed { index, element -> if (element is File) index else null }.filterNotNull()
            .reversed()

        indexesToGo.forEach { index ->
            val currentFile = archive[index] as File
            val idx = archive.subList(0, index).indexOfFirst { it is FreeSpace && it.length >= currentFile.length }
            if (idx != -1) {
                val freeSpace = archive[idx] as FreeSpace
                val leftFreeSpace = freeSpace.length - currentFile.length
                archive[idx] = freeSpace.copy(
                    length = leftFreeSpace,
                    filesInside = freeSpace.filesInside.plus(currentFile)
                )
                archive[index] = FreeSpace(currentFile.length, emptyList())
            }
        }

        var countIndex = 0
        var result = 0L
        archive.forEach { cf ->
            if (cf is File) {
                repeat((0 until cf.length).count()) {
                    result += cf.index * countIndex
                    countIndex++
                }
            }
            if (cf is FreeSpace) {
                cf.filesInside.forEach { cff ->
                    repeat((0 until cff.length).count()) {
                        result += cff.index * countIndex
                        countIndex++
                    }
                }
                countIndex += cf.length
            }
        }

        print(result)
    }
}