package com.example.jirou.memorizer.models

enum class EnumHASituation(val rawValue: Int) {
    OPEN(0),
    VS_RAISE(1),
    VS_3BET(2),
    VS_4BET(3);

    companion object {
        fun fromString(str : String) :  EnumHASituation {
            val result = EnumHASituation.values().filter { it.toString() == str }
            assert(result.size == 1)
            return result.first()
        }

        fun fromInt(int : Int) :  EnumHASituation {
            val result = EnumHASituation.values().filter { it.rawValue == int }
            assert(result.size == 1)
            return result.first()
        }

    }
}