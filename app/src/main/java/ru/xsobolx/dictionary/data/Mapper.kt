package ru.xsobolx.dictionary.data

interface Mapper<T, R> {
    fun map(value: T): R
}