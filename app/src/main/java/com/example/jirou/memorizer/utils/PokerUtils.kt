package com.example.jirou.memorizer.utils

fun numToChar(num: Int): Char {
    if (num < 10) {
        return num.toString()[0]
    }
    return when (num) {
        10 -> 'T'
        11 -> 'J'
        12 -> 'Q'
        13 -> 'K'
        14 -> 'A'
        else -> 'X'
    }
}

fun charToNum(ch: Char): Int {
    return when (ch) {
        '2' -> 2
        '3' -> 3
        '4' -> 4
        '5' -> 5
        '6' -> 6
        '7' -> 7
        '8' -> 8
        '9' -> 9
        'T' -> 10
        'J' -> 11
        'Q' -> 12
        'K' -> 13
        'A' -> 14
        else -> throw Exception(String.format("'%c' is not a card number", ch))
    }
}

fun getMaxChar(hand: String) : Char {
    assert(hand.length == 2)
    val char1 = hand[0]
    val char2 = hand[1]
    return if (charToNum(char1) > charToNum(char2)) char1 else char2
}

fun getMinChar(hand: String) : Char {
    assert(hand.length == 2)
    val char1 = hand[0]
    val char2 = hand[1]
    return if (charToNum(char1) < charToNum(char2)) char1 else char2
}