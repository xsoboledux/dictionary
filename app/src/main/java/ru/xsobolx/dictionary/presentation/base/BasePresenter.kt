package ru.xsobolx.dictionary.presentation.base

import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V : BaseMvpView> : MvpPresenter<V>() {
    protected val subscriptions = CompositeDisposable()

    override fun attachView(view: V) {
        super.attachView(view)
        onAttach(view)
    }

    override fun detachView(view: V) {
        super.detachView(view)
        subscriptions.dispose()
    }

    fun isSubscriptionsEmpty() = subscriptions.size() == 0

    abstract fun onAttach(view: V?)
}