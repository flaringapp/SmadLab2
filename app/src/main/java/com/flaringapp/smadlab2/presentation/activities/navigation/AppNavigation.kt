package com.flaringapp.smadlab2.presentation.activities.navigation

interface AppNavigation {

    fun navigateTo(
        screen: Screen,
        data: Any? = null
    )

}