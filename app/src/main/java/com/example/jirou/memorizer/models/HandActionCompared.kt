package com.example.jirou.memorizer.models

class HandActionCompared(paramHand: String, paramActionVal : Int) : HandAction(paramHand, paramActionVal) {
    private var compared : Int? = null

    fun getCompared() : Int?
    {
        return compared
    }

    fun compare(ha : HandAction)
    {
        compared = this.getActionVal() -  ha.getActionVal()
    }
}