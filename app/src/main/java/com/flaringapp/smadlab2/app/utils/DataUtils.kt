package com.flaringapp.app.utils

fun<T> Array<T>.swap(from: Int, to: Int) {
    val temp = this[from]
    this[from] = this[to]
    this[to] = temp
}