package com.example.shopping.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.shopping.data.TodoAppDatabase
import com.example.shopping.data.TodoDAO
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideTodoDao(appDatabase: TodoAppDatabase): TodoDAO {
        return appDatabase.todoDao()
    }

    @Provides
    @Singleton
    fun provideTodoAppDatabase(
        @ApplicationContext appContext: Context): TodoAppDatabase {
        return TodoAppDatabase.getDatabase(appContext)
    }
}