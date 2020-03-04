package com.flaringapp.smadlab2.presentation.mvp

interface IBaseDialog: IBaseView {
    val dialogTag: String?

    fun close()
}