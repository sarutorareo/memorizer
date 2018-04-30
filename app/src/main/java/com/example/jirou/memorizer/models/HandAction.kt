package com.example.jirou.memorizer.models

import android.os.Parcel
import android.os.Parcelable

const val AV_FOLD_100 = -1
const val AV_RAISE_100 = 100
const val AV_CALL_100 = 0

// actionVal
//   null : fold
//   0 ～ 100 : callの比率 (100 - action = Raiseの比率)
open class HandAction(private val mHand: String, private var mActionVal : Int) : Parcelable {

    val hand : String
        get() = mHand

    fun setActionVal(value : Int) {
        mActionVal = value
    }

    val actionVal: Int
        get() = mActionVal

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(mHand)
        dest?.writeInt(mActionVal)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun copyFrom(other : HandAction)
    {
        assert(this.mHand == other.mHand)
        this.mActionVal = other.mActionVal
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

