package com.meloda.lineqrreader.activity.ui

import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.meloda.lineqrreader.base.answer.Answer
import moxy.MvpPresenter

class AuthPresenter() : MvpPresenter<AuthView>() {

    val answer = MutableLiveData<Answer?>()
    val state = MutableLiveData<State?>()

    enum class State {
        INPUT_NUMBER, INPUT_CODE
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        showInputNumberScreen()
    }

    fun showInputNumberScreen() {
        state.value = State.INPUT_NUMBER
    }

    fun showInputCodeScreen() {
        if (Looper.myLooper() != Looper.getMainLooper())
            state.postValue(State.INPUT_CODE)
        else
            state.value = State.INPUT_CODE
    }

}