package com.example.jirou.memorizer.models

import android.os.Parcel
import android.os.Parcelable

const val AV_FOLD_100 = -1
const val AV_RAISE_100 = 100
const val AV_CALL_100 = 0

// actionVal
//   null : fold
//   0 ～ 100 : callの比率 (100 - action = Raiseの比率)
open class HandAction(private val hand: String, private var actionVal : Int) : Parcelable {

    fun getHand(): String {
        return hand
    }

    fun setActionVal(value : Int) {
        actionVal = value
    }

    fun getActionVal(): Int {
        return actionVal
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(hand)
        dest?.writeInt(actionVal)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun copyFrom(other : HandAction)
    {
        assert(this.hand == other.hand)
        this.actionVal = other.actionVal
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<HandAction> = object : Parcelable.Creator<HandAction> {
            override fun createFromParcel(`in`: Parcel): HandAction {
                return HandAction(`in`.readString(), `in`.readInt())
            }

            override fun newArray(size: Int): Array<HandAction?> {
                return arrayOfNulls(size)
            }
        }
    }

}

