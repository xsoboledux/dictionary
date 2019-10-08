package ru.xsobolx.dictionary.domain.base

import io.reactivex.Single

interface UseCase<P, R> {

    fun execute(parameter: P) : Single<R>
}