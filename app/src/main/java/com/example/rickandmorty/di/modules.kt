package com.example.rickandmorty.di

import androidx.room.Room
import com.example.rickandmorty.data.database.RickAndMortyDataBase
import com.example.rickandmorty.data.network.RickAndMortyApi
import com.example.rickandmorty.data.repository.CharacterRepository
import com.example.rickandmorty.ui.MainViewModel
import com.example.rickandmorty.ui.detail.DetailActivityViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val apiModule = module {
    single<RickAndMortyApi> { RickAndMortyApi.instance }
}

val repositoryModule = module {
    single { CharacterRepository(get(), get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { DetailActivityViewModel(get()) }
}

val roomModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            RickAndMortyDataBase::class.java,
            "rick_and_morty_database"
        ).build()
    }

    single { get<RickAndMortyDataBase>().getOompaLoompaDao() }
}


