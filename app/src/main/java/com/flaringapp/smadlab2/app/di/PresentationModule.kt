package com.flaringapp.smadlab2.app.di

import com.flaringapp.smadlab2.presentation.activities.MainContract
import com.flaringapp.smadlab2.presentation.activities.impl.MainActivity
import com.flaringapp.smadlab2.presentation.activities.impl.MainPresenter
import com.flaringapp.smadlab2.presentation.dialogs.input.InputContract
import com.flaringapp.smadlab2.presentation.dialogs.input.impl.InputPresenter
import com.flaringapp.smadlab2.presentation.fragments.home.HomeContract
import com.flaringapp.smadlab2.presentation.fragments.home.impl.HomeFragment
import com.flaringapp.smadlab2.presentation.fragments.home.impl.HomePresenter
import org.koin.core.qualifier.named
import org.koin.dsl.module

val presentationModule = module {

    //Activities

    scope(named<MainActivity>()) {
        scoped { MainPresenter() as MainContract.PresenterContract }
    }

    //Fragments

    scope(named<HomeFragment>()) {
        scoped { HomePresenter(get()) as HomeContract.PresenterContract }
    }

    //Dialogs

    factory { InputPresenter() as InputContract.PresenterContract }
}