package com.example.jirou.memorizer.utils

import org.jetbrains.anko.collections.forEachWithIndex
import java.text.SimpleDateFormat
import java.util.*

fun stringListWithoutBlankOf(vararg ar: String?) : List<String> {
    val srcList = ar.toMutableList()
    val tmpList = mutableListOf<String>()
    srcList.forEach {
        if ((it != null) && (it.isNotBlank())) {
            tmpList.add(it)
        }
    }

    return tmpList.toList()
}

fun copyStringListToFixedSizeList(src: List<String>, size: Int) : List<String>
{
    val qArray = arrayListOf("", "", "", "", "")
    src.forEachWithIndex { i, s ->
        qArray[i] = s
    }
    return qArray
}
