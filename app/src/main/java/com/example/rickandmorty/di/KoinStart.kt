package com.example.rickandmorty.di

import android.app.Application
import com.example.rickandmorty.di.apiModule
import com.example.rickandmorty.di.repositoryModule
import com.example.rickandmorty.di.roomModule
import com.example.rickandmorty.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class KoinApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KoinApp)
            modules(
                apiModule,
                repositoryModule,
                viewModelModule,
                roomModule
            )
        }
    }
}