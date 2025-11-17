package com.kriscg.belek.data.local

import android.content.Context

object LocalDatabase {

    lateinit var db: AppDatabase
        private set

    fun initialize(context: Context) {
        db = AppDatabase.getDatabase(context)
    }
}
