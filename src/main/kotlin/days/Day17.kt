package org.example.days

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.example.utils.AdventDay
import org.example.utils.numbersRegex
import kotlin.math.pow

object Day17 : AdventDay {
    override fun part1(input: String) {
        val (registers, program) = input.split("\n\n")
        var (regA, regB, regC) = registers.split("\n")
            .map { numbersRegex.findAll(it).toList().first().value.toInt() }
        val instructions = program.substringAfter(": ").split(",").map { it.toInt() }


        val jump = 2
        var currentInstructionIndex = 0

        fun Int.comboOperand(): Int {
            return when (this) {
                0, 1, 2, 3 -> this
                4 -> regA
                5 -> regB
                6 -> regC
                else -> error("DONT.")
            }
        }

        val output = mutableListOf<Int>()
        while (currentInstructionIndex < instructions.size) {
            val value = instructions[currentInstructionIndex + 1]
            when (instructions[currentInstructionIndex]) {
                0 -> {
                    regA = (regA / 2.0.pow(value.comboOperand())).toInt()
                    currentInstructionIndex += jump
                }

                1 -> {
                    regB = regB xor value
                    currentInstructionIndex += jump
                }

                2 -> {
                    regB = value.comboOperand() % 8
                    currentInstructionIndex += jump
                }

                3 -> {
                    if (regA == 0) {
                        currentInstructionIndex += jump
                        continue
                    }
                    currentInstructionIndex = value
                }

                4 -> {
                    regB = regB xor regC
                    currentInstructionIndex += jump
                }

                5 -> {
                    output.add(value.comboOperand() % 8)
                    currentInstructionIndex += jump
                }

                6 -> {
                    regB = (regA / 2.0.pow(value.comboOperand())).toInt()
                    currentInstructionIndex += jump
                }

                7 -> {
                    regC = (regA / 2.0.pow(value.comboOperand())).toInt()
                    currentInstructionIndex += jump
                }
            }


        }

        print(output.joinToString(","))
    }


    override fun part2(input: String) {
        print("Elves went to build toys. Will be back shortly.")
    }
}