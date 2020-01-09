package com.techcent.momobankandroid

import android.app.Application
import android.content.Context


/**
 * Inherits from the Android app Application()
 * It can be used to manage some of the lifecycle of the entire application
 * outside of the scope of the activities and fragments
 */
class MoMoApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: MoMoApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

}