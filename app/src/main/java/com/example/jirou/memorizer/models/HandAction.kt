package com.example.jirou.memorizer.models

import android.os.Parcel
import android.os.Parcelable
import com.example.jirou.memorizer.utils.getMaxChar
import com.example.jirou.memorizer.utils.getMinChar

const val AV_FOLD_100 = 0
const val AV_RAISE_100 = 200
const val AV_RAISE_OR_FOLD_50 = 50
const val AV_CALL_100 = 100

// actionVal
//   null : fold
//   0 ～ 100 : callの比率 (100 - action = Raiseの比率)
open class HandAction(private val mHand: String, private var mActionVal : Int) : Parcelable {

    private var mHandDispStr : String = mGetDispStr()
    val hand : String
        get() = mHand
    val handDispStr : String
        get() {
            return mHandDispStr
        }

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

    private fun mGetDispStr() : String
    {
        return String.format("%c%c", getMaxChar(mHand), getMinChar(mHand))
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

