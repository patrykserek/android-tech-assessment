package com.pelagohealth.codingchallenge.data

import com.pelagohealth.codingchallenge.data.datasource.rest.FactsRestApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Hilt module that provides dependencies for the data layer.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private const val BASE_URL = "https://uselessfacts.jsph.pl/api/v2"

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideRestApi(retrofit: Retrofit): FactsRestApi =
        retrofit.create(FactsRestApi::class.java)
}
