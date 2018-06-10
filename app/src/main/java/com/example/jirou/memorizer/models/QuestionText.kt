package com.example.jirou.memorizer.models

import android.content.Context
import android.database.sqlite.SQLiteException
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.example.jirou.memorizer.db.MemorizeDBOpenHelper
import com.example.jirou.memorizer.utils.copyStringListToFixedSizeList
import com.example.jirou.memorizer.utils.stringArrayListWithoutBlankOf
import org.jetbrains.anko.db.replaceOrThrow
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.transaction

open class QuestionText(quizId: Int, txtMain: String,
                        questionList : List<String>
                        ) : Question(quizId), Parcelable {

    private var mQuestionMain : String = txtMain
    private var mQuestionList : List<String> = questionList

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<QuestionText> = object : Parcelable.Creator<QuestionText> {
            override fun createFromParcel(`in`: Parcel): QuestionText {
                fun toStr(int : Int) : String { return int.toString() }
                val id = `in`.readInt()
                val main =  `in`.readString()
                val size = `in`.readInt()
                val ar = List<String>(size, ::toStr)
                `in`.readStringList(ar)
                return QuestionText(id, main, ar)
            }

            override fun newArray(size: Int): Array<QuestionText?> {
                return arrayOfNulls(size)
            }
        }
    }

    init {
    }

    val questionMain : String
        get() = mQuestionMain

    val questionList : List<String>
        get() = mQuestionList

    constructor(quizId: Int) : this(quizId, "main", ArrayList<String>())

    override fun save(context: Context, dbName: String) {
        val thisInstance = this
        Log.e("QuestionText.save", String.format("save [%s]", thisInstance.toString()))
        val helper = MemorizeDBOpenHelper.getInstance(context, dbName)
        val qArray = copyStringListToFixedSizeList(questionList, 5)
        helper.use {
            transaction {
                replaceOrThrow(
                        MemorizeDBOpenHelper.TABLE_NAME_QST_TEXT,
                        *MemorizeDBOpenHelper.addUpdateDate(arrayOf(
                                "quiz_id" to quizId.toString(),
                                "question_main" to questionMain,
                                "question_1" to qArray[0],
                                "question_2" to qArray[1],
                                "question_3" to qArray[2],
                                "question_4" to qArray[3],
                                "question_5" to qArray[4])
                        )
                )
            }
        }
        Log.e("QuestionHandAction.save", "end save")
    }

    override fun load(context: Context, dbName: String) {
        val helper = MemorizeDBOpenHelper.getInstance(context, dbName)
        helper.use {
            try {
                val resultQstText: QuestionText = select(MemorizeDBOpenHelper.TABLE_NAME_QST_TEXT,
                        "question_main",
                           "question_1",  "question_2",  "question_3",  "question_4",  "question_5")
                        .whereArgs("quiz_id = {quizId}", "quizId" to quizId.toString())
                        .parseSingle(
                                rowParser { question_main: String,
                                            question_1: String, question_2: String, question_3: String,
                                            question_4: String, question_5: String ->
                                    val arr = stringArrayListWithoutBlankOf(question_1, question_2, question_3, question_4, question_5)
                                    QuestionText(quizId, question_main, arr)
                                }
                        )
                copyFrom(resultQstText)
            }
            // 該当レコードが無い場合
            catch (e : SQLiteException) {
                // 何もしない
            }
        }
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(quizId)
        dest?.writeString(questionMain)
        dest?.writeInt(questionList.size)
        dest?.writeStringArray(questionList.toTypedArray())
    }

    override fun describeContents(): Int {
        return 0
    }

    val title : String
        get() = questionMain

    fun copyFrom(src : QuestionText) {
        mQuestionMain = src.questionMain
        mQuestionList = src.questionList.toList()
    }
    /*

    override fun toString() : String
    {
        return String.format("quizId[%d], situation[%s], heroPos[%s], opponentPos[%s]", quizId, situation.toString(), heroPosition.toString(), opponentPosition.toString())
    }


*/
}
