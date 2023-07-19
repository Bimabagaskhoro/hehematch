package com.hehematch.android.hehematchandrodapp.core.di

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.hehematch.android.hehematchandrodapp.ui.finder.core.repository.ResultsRepository
import com.hehematch.android.hehematchandrodapp.ui.finder.core.repository.ResultsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Singleton
    @Provides
    fun provideDatabaseUserArea(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("userarea")
    }


    @Singleton
    @Provides
    fun provideUserRepository(resultsRepository: ResultsRepositoryImpl): ResultsRepository =
        resultsRepository
}