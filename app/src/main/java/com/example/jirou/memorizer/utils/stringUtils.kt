package com.example.jirou.memorizer.utils

import java.text.SimpleDateFormat
import java.util.*

fun dateToString(date: Date): String {

    val df = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

    return df.format(date)
}