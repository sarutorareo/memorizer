package com.example.jirou.memorizer.test_utils

import android.content.Context
import org.robolectric.RuntimeEnvironment

const val TEST_DB_NAME = "testdb.db"

class TestHelper {
    companion object {
        private var mContext : Context? = null

        fun getContext(): Context {
            /*
            instance = instance ?: MemorizeDBOpenHelper(context.applicationContext)
            return instance!!
            */
            mContext =
                    if (mContext == null) {
                        RuntimeEnvironment.application
                    } else {
                        mContext
                    }
            return mContext!!
        }
    }
}