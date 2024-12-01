package org.example.utils

interface AdventDay {
    operator fun invoke() {
        val currentDay = numbersRegex.find(javaClass.name)?.value ?: error("Day not specified")
        println("🎄Day $currentDay 🎄")
        print("Part 1: ")
        generatePart1Answer(currentDay)
        println()
        print("Part 2: ")
        generatePart2Answer(currentDay)
    }

    private fun generatePart1Answer(currentDay: String) {
        part1(readLines(currentDay))
        part1(readFile(currentDay))
    }

    private fun generatePart2Answer(currentDay: String) {
        part2(readLines(currentDay))
        part2(readFile(currentDay))
    }

    fun part1(input: List<String>) {}
    fun part1(input: String) {}
    fun part2(input: List<String>) {}
    fun part2(input: String) {}
}