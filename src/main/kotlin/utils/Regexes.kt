package org.example.utils

/**
 * Regex to find all single digits in a string so 123abc would find 1 2 and 3
 */
val singleDigits  = "[0-9]".toRegex()

/**
 * Regex to find all numbers in a string so 123abc would give us 123
 * Before we had "-?\\d+" but this matched -123
 */
val numbersRegex = "\\d+".toRegex()

/**
 * Regex to find all numbers positive and negative
 */
val numbersWithSignRegex = """-?\d+""".toRegex()

/**
 * Regex to find single digits and their string representation
 * This also handles the case of overlapping because of ?=
 * But then the result is not a match but a group of matches instead so we have to do:
 * val results = reg.findAll(l)
 * results.map { it.groupValues[1] }.toList()
 */
val singleDigitsOrStringRepresentationsOverlapping = "(?=([0-9]|one|two|three|four|five|six|seven|eight|nine))".toRegex()

/**
 * Regex to match `2 red roses are blue` this matches 2 and the rest
 * ^ Anchors the match at the beginning of the string.
 * (\d+) Capturing group for one or more digits (numeric value).
 * \s* Matches zero or more whitespace characters.
 * (.*) Capturing group for any remaining characters.
 */
val numberWithWordsMatcher = "^(\\d+)\\s*(.*)".toRegex()