package com.example.jirou.memorizer.models

enum class EnumRequestCodes(val rawValue: Int) {
    TRAINING(1000),
    TRAINING_HAND_ACTION(1001),
    TRAINING_TEXT(1002),
    EDIT_HAND_ACTION(1011),
    EDIT_HAND_ACTION_SITUATION(1012),
    EDIT_TEXT(1013),
    ;
}