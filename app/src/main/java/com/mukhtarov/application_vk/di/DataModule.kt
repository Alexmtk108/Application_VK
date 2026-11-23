package com.mukhtarov.application_vk.di

import com.mukhtarov.application_vk.data.repository.AppRepositoryImpl
import com.mukhtarov.application_vk.domain.repository.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Модуль живет на уровне всего приложения
abstract class DataModule {

    @Binds
    @Singleton // Создаем репозиторий в единственном экземпляре (Singleton)
    abstract fun bindAppRepository(
        appRepositoryImpl: AppRepositoryImpl
    ): AppRepository
}