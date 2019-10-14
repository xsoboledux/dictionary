package ru.xsobolx.dictionary.presentation.base

import com.arellomobile.mvp.MvpView

interface BaseMvpView : MvpView {

    fun showLoading()

    fun hideLoading()

    fun showError(message: String?)
}