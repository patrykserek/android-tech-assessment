package com.pelagohealth.codingchallenge.domain.dispatcher

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatcherModule {

    @Binds
    abstract fun bindDispatcherProvider(
        defaultDispatcherProvider: DefaultDispatcherProvider
    ): DispatcherProvider
}
