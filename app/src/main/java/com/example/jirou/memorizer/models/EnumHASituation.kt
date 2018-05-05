package com.example.jirou.memorizer.models

enum class EnumHandActionSituation() {
    OPEN,
    FACING_A_RAISE,
    FACING_A_3BET,
    FACING_A_4BET;

    companion object {
        fun toHandActionSituation(str : String) :  EnumHandActionSituation {
            return when (str) {
                "OPEN" -> { OPEN }
                "FACING_A_RAISE" -> { FACING_A_RAISE }
                "FACING_A_3BET" -> { FACING_A_3BET }
                "FACING_A_4BET" -> { FACING_A_4BET }
                else -> {
                    throw Exception()
                }
            }
        }
    }
}