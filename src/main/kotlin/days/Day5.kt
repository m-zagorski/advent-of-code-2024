package org.example.days

import org.example.utils.AdventDay

object Day5 : AdventDay {

    data class Instruction(
        val before: List<Long>,
        val after: List<Long>
    ) {
        fun addAfter(i: Long): Instruction = copy(after = after.plus(i))
        fun addBefore(i: Long): Instruction = copy(before = before.plus(i))
    }

    override fun part2(input: String) {
        val (rules, game) = input.split("\n\n")
        val instructions = createInstructions(rules)

        val result = game.split("\n")
            .map { line -> line.split(",").map { it.toLong() } }
            .filter { numbers -> !numbers.inValidOrder(instructions) }
            .map { fixIncorrectRule(it, instructions, false) }
            .sumOf(::middleItem)

        print(result)

    }

    override fun part1(input: String) {
        val (rules, game) = input.split("\n\n")
        val instructions = createInstructions(rules)
        val result = game.split("\n")
            .sumOf { line ->
                val numbers = line.split(",").map { it.toLong() }
                if (numbers.inValidOrder(instructions)) middleItem(numbers)
                else 0L
            }
        print(result)
    }

    private fun createInstructions(rules: String): Map<Long, Instruction> {
        val instructions = mutableMapOf<Long, Instruction>()

        rules.split("\n")
            .forEach { line ->
                val (l, r) = line.split("|").map { it.toLong() }
                val lci = instructions.getOrDefault(l, Instruction(emptyList(), emptyList()))
                instructions += l to lci.addAfter(r)

                val rci = instructions.getOrDefault(r, Instruction(emptyList(), emptyList()))
                instructions += r to rci.addBefore(l)
            }

        return instructions
    }

    private fun List<Long>.inValidOrder(instructions: Map<Long, Instruction>): Boolean {
        var valid = true
        for (i in indices) {
            val number = this[i]
            if (!isCurrentNumberValid(number, i, this, instructions)) {
                valid = false
            }
        }
        return valid
    }

    private fun fixIncorrectRule(
        numbers: List<Long>,
        instructions: Map<Long, Instruction>,
        isCorrect: Boolean
    ): List<Long> {
        if (isCorrect) return numbers

        for (i in numbers.indices) {
            val currentNumber = numbers[i]
            val (instructionsBefore, instructionsAfter) = instructions.getOrDefault(
                currentNumber,
                Instruction(emptyList(), emptyList())
            )

            if (!isCurrentNumberValid(currentNumber, i, numbers, instructions)) {
                val commonBefore = numbers.intersect(instructionsBefore.toSet())
                val commonAfter = numbers.intersect(instructionsAfter.toSet())

                val shuffledList = buildList {
                    addAll(commonBefore)
                    add(currentNumber)
                    addAll(commonAfter)
                }
                return fixIncorrectRule(shuffledList, instructions, false)
            }
        }
        return fixIncorrectRule(numbers, instructions, true)
    }

    private fun isCurrentNumberValid(
        number: Long,
        index: Int,
        numbers: List<Long>,
        instructions: Map<Long, Instruction>
    ): Boolean {
        val after = numbers.subList(index, numbers.indices.last + 1).drop(1)
        val before = numbers.subList(0, index)
        val (instructionsBefore, instructionsAfter) = instructions.getOrDefault(
            number,
            Instruction(emptyList(), emptyList())
        )
        return instructionsBefore.containsAll(before) && instructionsAfter.containsAll(after)
    }

    private fun middleItem(numbers: List<Long>): Long {
        val middleIndex = numbers.size / 2
        return if (numbers.size % 2 == 0) numbers[middleIndex - 1]
        else numbers[middleIndex]
    }
}