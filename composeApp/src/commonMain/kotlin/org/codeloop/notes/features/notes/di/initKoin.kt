package org.codeloop.notes.features.notes.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    appConfig : KoinAppDeclaration?= null
) {

    startKoin {

        appConfig?.invoke(this)

        modules(
            platFormModule,
            sharedModule,
        )
    }

}