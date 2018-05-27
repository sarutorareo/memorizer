package com.example.jirou.memorizer.models

import android.content.Context
import org.junit.Test

import org.junit.Assert.*
import com.example.jirou.memorizer.test_utils.TestHelper
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class TestTrainingManager {
    private var mContext: Context = TestHelper.getContext()

    @Test
    fun test_constructor() {
        val list = arrayListOf<Quiz>(QuizHandAction(0), QuizHandAction(1))
        val mng = TrainingManager(list)
        assertEquals(true,mng != null)
    }
}
