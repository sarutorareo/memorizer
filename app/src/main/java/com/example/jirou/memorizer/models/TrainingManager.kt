package com.example.jirou.memorizer.models


class TrainingManager(private val mQuizList : List<Quiz>) {
    private var index = 0

    init {
    }

    fun start() : Quiz {
        index = 0
        return mGetQuiz()!!
    }

    fun next() : Quiz? {
        index++
        return mGetQuiz()
    }

    fun retry() : Quiz {
        return mGetQuiz()!!
    }

    private fun mGetQuiz(): Quiz? {
        return if ((index < 0) || (index >= mQuizList.size)) {
                null
            }
            else {
                mQuizList[index]
            }
    }
}
