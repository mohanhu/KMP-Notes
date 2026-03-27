package org.codeloop.notes.features.notes.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.codeloop.notes.core.database.CommonDataBaseFactory
import org.codeloop.notes.features.notes.data.repository.NotesLocalRepositoryImpl
import org.codeloop.notes.features.notes.domain.repository.NotesLocalRepository
import org.codeloop.notes.features.notes.presentation.home.HomeViewModel
import org.codeloop.notes.features.notes.presentation.notes.NotesListViewModel
import org.codeloop.notes.features.notes.presentation.notes.NotesPreviewViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platFormModule : Module

val sharedModule = module {

//    single{ get<CommonDataBase>() }
    single{
        get<CommonDataBaseFactory>().create()
            .fallbackToDestructiveMigration(dropAllTables = true)
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    /**Home Start*/
    singleOf(::NotesLocalRepositoryImpl).bind<NotesLocalRepository>()
    viewModelOf(::HomeViewModel)
    /**Home End*/

    /**Notes Start*/
    viewModelOf(::NotesPreviewViewModel)
    viewModelOf(::NotesListViewModel)
    /**Notes End*/






}