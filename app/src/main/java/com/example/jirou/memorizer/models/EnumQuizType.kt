package com.example.jirou.memorizer.models

enum class EnumQuizType {
    HAND_ACTION;
    companion object {
        fun fromString(str: String): EnumQuizType {
            val result = EnumQuizType.values().filter { it.toString() == str }
            assert(result.size == 1)
            return result.first()
        }
    }
}