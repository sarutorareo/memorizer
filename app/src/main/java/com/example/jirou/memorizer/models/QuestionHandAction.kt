package com.example.jirou.memorizer.models

import android.content.Context
import android.util.Log
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper
import org.jetbrains.anko.db.replaceOrThrow
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.transaction

open class QuestionHandAction(quizId: Int, situation: EnumHASituation,
                              heroPos: EnumHAPosition,
                              opponentPos: EnumHAPosition) : Question(quizId) {

    private var mSituation: EnumHASituation = situation
    private var mHeroPos: EnumHAPosition = heroPos
    private var mOpponentPos: EnumHAPosition = opponentPos

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
        val helper = MemorizeDBOpenHelper.getInstance(context, dbName)
        helper.use {
            transaction {
                Log.d("QuestionHandAction.save", String.format("save [%s]", this.toString()))
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
    }

    override fun toString() : String
    {
        return String.format("quizId[%d], situation[%s], heroPos[%s], opponentPos[%s]", quizId, situation.toString(), heroPosition.toString(), opponentPosition.toString())
    }

    override fun load(context: Context, dbName: String) {
        val helper = MemorizeDBOpenHelper.getInstance(context, dbName)
        helper.use {
            val resultQstHandAction : QuestionHandAction = select(MemorizeDBOpenHelper.TABLE_NAME_QST_HAND_ACTION,
                    "situation", "hero_position", "opponent_position")
                    .whereArgs("quiz_id = {quizId}", "quizId" to quizId.toString())
                    .parseSingle(
                            rowParser {
                                situation: String, heroPos: String, opponentPos: String ->
                                QuestionHandAction(quizId,
                                        EnumHASituation.toHandActionSituation(situation),
                                        EnumHAPosition.toHandActionPosition(heroPos),
                                        EnumHAPosition.toHandActionPosition(opponentPos)
                                )
                            }
                    )
            copyFrom(resultQstHandAction)
        }
    }

    fun copyFrom(src : QuestionHandAction) {
        situation = src.situation
        heroPosition = src.heroPosition
        opponentPosition = src.opponentPosition
    }
}
