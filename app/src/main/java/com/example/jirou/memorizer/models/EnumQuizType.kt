package com.example.jirou.memorizer.models

enum class EnumQuizType(val rawValue: Int) {
    HAND_ACTION(0),
    TEXT(1);

    companion object {
        fun fromString(str: String): EnumQuizType {
            val result = EnumQuizType.values().filter { it.toString() == str }
            assert(result.size == 1)
            return result.first()
        }
        fun fromInt(int: Int): EnumQuizType {
            val result = EnumQuizType.values().filter { it.rawValue == int }
            assert(result.size == 1)
            return result.first()
        }
    }
}