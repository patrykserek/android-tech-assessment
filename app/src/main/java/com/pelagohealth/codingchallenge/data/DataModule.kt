package com.pelagohealth.codingchallenge.data

import com.pelagohealth.codingchallenge.data.datasource.rest.FactsRestApi
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Hilt module that provides dependencies for the data layer.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private const val BASE_URL = "https://uselessfacts.jsph.pl/api/v2/"

    @Provides
    @Singleton
    fun provideJsonAdapterFactory(): JsonAdapter.Factory = KotlinJsonAdapterFactory()

    @Provides
    @Singleton
    fun provideMoshi(adapter: JsonAdapter.Factory): Moshi =
        Moshi.Builder()
            .add(adapter)
            .build()

    @Provides
    @Singleton
    fun provideConverterFactory(moshi: Moshi): Converter.Factory =
        MoshiConverterFactory.create(moshi)

    @Provides
    @Singleton
    fun provideRetrofit(converter: Converter.Factory): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(converter)
            .build()

    @Provides
    @Singleton
    fun provideRestApi(retrofit: Retrofit): FactsRestApi =
        retrofit.create(FactsRestApi::class.java)
}
