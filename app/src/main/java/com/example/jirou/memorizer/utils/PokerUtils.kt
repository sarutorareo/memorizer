package com.example.jirou.memorizer.utils

fun numToStr(num: Int): String {
    if (num < 10) {
        return num.toString()
    }
    return when (num) {
        10 -> "T"
        11 -> "J"
        12 -> "Q"
        13 -> "K"
        else -> "X"
    }
}