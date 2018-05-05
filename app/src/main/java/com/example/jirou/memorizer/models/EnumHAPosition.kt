package com.example.jirou.memorizer.models

enum class EnumHAPosition() {
    UTG,
    HJ,
    CO,
    BTN,
    SB,
    BB,
    NULL;

    companion object {
        fun toHandActionPosition(str : String) : EnumHAPosition{
            return when (str) {
                "UTG" -> { UTG }
                "HJ" -> { HJ }
                "CO" -> { CO }
                "BTN" -> { BTN }
                "SB" -> { SB }
                "BB" -> { BB }
                "NULL" -> { NULL }
                else -> {
                    throw Exception()
                }
            }
        }
    }
}