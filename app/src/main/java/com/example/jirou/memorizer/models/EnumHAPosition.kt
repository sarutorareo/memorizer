package com.example.jirou.memorizer.models

enum class EnumHAPosition(val rawValue: Int) {
    UTG(0),
    HJ(1),
    CO(2),
    BTN(3),
    SB(4),
    BB(5),
    NULL(6);

    companion object {
        fun fromString(str : String) : EnumHAPosition{
            val result = EnumHAPosition.values().filter { it.toString() == str }
            assert(result.size == 1)
            return result.first()
        }

        fun fromInt(int :Int) : EnumHAPosition{
            val result = EnumHAPosition.values().filter { it.rawValue == int }
            assert(result.size == 1)
            return result.first()
        }
    }
}