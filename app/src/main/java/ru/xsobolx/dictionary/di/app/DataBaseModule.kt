package ru.xsobolx.dictionary.di.app

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.xsobolx.dictionary.data.db.base.AppDatabase
import ru.xsobolx.dictionary.data.db.base.DATABASE_NAME

@Module
class DataBaseModule {

    @Provides
    fun provideDataBase(@AppContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .build()
    }
}