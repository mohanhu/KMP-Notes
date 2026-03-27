package org.codeloop.notes.features.notes.di

import org.codeloop.notes.core.database.CommonDataBaseFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platFormModule: Module
    get() = module {
        single { CommonDataBaseFactory() }
    }