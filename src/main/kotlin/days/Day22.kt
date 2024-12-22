package org.example.days

import org.example.utils.AdventDay

object Day22 : AdventDay {
    override fun part1(input: List<String>) {
        val output = input.sumOf { c ->
            var current = c.toLong()
            repeat(2000) {
                current = current.calculate()
            }
            current

        }
        print(output)
    }

    data class Prices(
        val prices: List<Long>,
        val changes: List<Long>
    )

    override fun part2(input: List<String>) {
        val priceChanges = mutableListOf<Prices>()

        input.forEach { c ->
            var current = c.toLong()
            val localPrices = mutableListOf(current % 10)
            val localChanges = mutableListOf(0L)
            var previous = current % 10
            repeat(2000) {
                current = current.calculate()
                val lastDigit = current % 10
                localPrices.add(lastDigit)
                localChanges.add(lastDigit - previous)
                previous = lastDigit
            }
            priceChanges.add(Prices(localPrices, localChanges))
        }

        var totalBananas = 0L
        val alreadyChecked = mutableSetOf<List<Long>>()
        priceChanges.forEachIndexed { index, u ->
            val remaining = priceChanges.filterIndexed { i, prices -> i != index }
            val output = u.changes.drop(1).windowed(4)
            for (sublist in output) {
                if (alreadyChecked.contains(sublist)) continue
                val idx = findIndexOfPrice(u.changes, sublist)
                val value = if (idx != null) u.prices[idx] else 0
                var maxBananas = value
                alreadyChecked.add(sublist)

                remaining.forEach { (prices, changes) ->
                    val i = findIndexOfPrice(changes, sublist)
                    val v = if (i != null) prices[i] else 0L
                    if (v != 0L) {
                        maxBananas += v
                    }
                }

                if (maxBananas > totalBananas) {
                    totalBananas = maxBananas
                }
            }
        }
        print(totalBananas)
    }

    private fun findIndexOfPrice(mainList: List<Long>, subList: List<Long>): Int? {
        val subListSize = subList.size
        for (i in 0..mainList.size - subListSize) {
            if (mainList.subList(i, i + subListSize) == subList) {
                return i + subList.size - 1
            }
        }
        return null
    }

    private fun Long.calculate(): Long {
        var current = this
        current = (current * 64).mix(current).prune()
        current = (current / 32).mix(current).prune()
        current = (current * 2048).mix(current).prune()
        return current
    }

    private fun Long.mix(currentValue: Long): Long {
        return currentValue.xor(this)
    }

    private fun Long.prune(): Long {
        return this % 16777216
    }
}