package ru.xsobolx.dictionary.presentation.base

import android.text.Editable
import android.text.TextWatcher
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class RxTextWatcher : TextWatcher {

    companion object {
        const val DEFAULT_DEBOUNCE_IN_MILLS = 500L
    }

    private val subjectTextWatcher = PublishSubject.create<String>()

    override fun afterTextChanged(s: Editable?) {
        subjectTextWatcher.onNext(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    fun observeTextChanges(): Observable<String> {
        return subjectTextWatcher.debounce(DEFAULT_DEBOUNCE_IN_MILLS, TimeUnit.MILLISECONDS)
    }
}