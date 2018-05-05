package com.example.jirou.memorizer.models

enum class EnumHandActionPosition() {
    UTG,
    HJ,
    CO,
    BTN,
    SB,
    BB,
    NULL;

    companion object {
        fun toHandActionPosition(str : String) : EnumHandActionPosition{
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