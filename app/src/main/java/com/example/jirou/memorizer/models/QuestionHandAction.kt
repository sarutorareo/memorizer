package com.example.jirou.memorizer.models

import android.content.Context
import android.database.sqlite.SQLiteException
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper
import org.jetbrains.anko.db.replaceOrThrow
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.transaction

open class QuestionHandAction(quizId: Int, situation: EnumHASituation,
                              heroPos: EnumHAPosition,
                              opponentPos: EnumHAPosition) : Question(quizId), Parcelable {

    private var mSituation: EnumHASituation = situation
    private var mHeroPos: EnumHAPosition = heroPos
    private var mOpponentPos: EnumHAPosition = opponentPos

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<QuestionHandAction> = object : Parcelable.Creator<QuestionHandAction> {
            override fun createFromParcel(`in`: Parcel): QuestionHandAction {
                return QuestionHandAction(`in`.readInt(),
                        EnumHASituation.fromString(`in`.readString()),
                        EnumHAPosition.fromString(`in`.readString()),
                        EnumHAPosition.fromString(`in`.readString()))
            }

            override fun newArray(size: Int): Array<QuestionHandAction?> {
                return arrayOfNulls(size)
            }
        }
    }

    init {
    }

    constructor(quizId: Int) : this(quizId, EnumHASituation.OPEN, EnumHAPosition.BTN, EnumHAPosition.NULL)

    var situation: EnumHASituation
        get() = mSituation
        set(value) {
            mSituation = value
        }

    var heroPosition: EnumHAPosition
        get() = mHeroPos
        set(value) {
            mHeroPos = value
        }

    var opponentPosition: EnumHAPosition
        get() = mOpponentPos
        set(value) {
            mOpponentPos = value
        }

    override fun save(context: Context, dbName: String) {
        Log.e("QuestionHandAction.save", "start save 1")

        Log.e("QuestionHandAction.save", "start save 2")
        val thisInstance = this
        Log.e("QuestionHandAction.save", "start save 3")
        Log.e("QuestionHandAction.save", String.format("save [%s]", thisInstance.toString()))
        Log.e("QuestionHandAction.save", "start save 4")
        val helper = MemorizeDBOpenHelper.getInstance(context, dbName)
        Log.e("QuestionHandAction.save", "before helper.use")
        helper.use {
            Log.e("QuestionHandAction.save", "before transaction")
            transaction {
                Log.e("QuestionHandAction.save", "before replaceOrThrow")
                replaceOrThrow(
                        MemorizeDBOpenHelper.TABLE_NAME_QST_HAND_ACTION,
                        *MemorizeDBOpenHelper.addUpdateDate(arrayOf(
                                "quiz_id" to quizId.toString(),
                                "situation" to situation.toString(),
                                "hero_position" to heroPosition.toString(),
                                "opponent_position" to opponentPosition.toString()))
                )
            }
        }
        Log.e("QuestionHandAction.save", "end save")
    }

    override fun toString() : String
    {
        return String.format("quizId[%d], situation[%s], heroPos[%s], opponentPos[%s]", quizId, situation.toString(), heroPosition.toString(), opponentPosition.toString())
    }

    override fun load(context: Context, dbName: String) {
        val helper = MemorizeDBOpenHelper.getInstance(context, dbName)
        helper.use {
            try {
                val resultQstHandAction: QuestionHandAction = select(MemorizeDBOpenHelper.TABLE_NAME_QST_HAND_ACTION,
                        "situation", "hero_position", "opponent_position")
                        .whereArgs("quiz_id = {quizId}", "quizId" to quizId.toString())
                        .parseSingle(
                                rowParser { situation: String, heroPos: String, opponentPos: String ->
                                    QuestionHandAction(quizId,
                                            EnumHASituation.fromString(situation),
                                            EnumHAPosition.fromString(heroPos),
                                            EnumHAPosition.fromString(opponentPos)
                                    )
                                }
                        )
                copyFrom(resultQstHandAction)
            }
            // 該当レコードが無い場合
            catch (e : SQLiteException) {
                // 何もしない
            }
        }
    }

    fun copyFrom(src : QuestionHandAction) {
        situation = src.situation
        heroPosition = src.heroPosition
        opponentPosition = src.opponentPosition
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(quizId)
        dest?.writeString(situation.toString())
        dest?.writeString(heroPosition.toString())
        dest?.writeString(opponentPosition.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    val title : String
        get() = String.format("%s-%s-%s", situation.toString(), heroPosition.toString(), opponentPosition.toString())
}
