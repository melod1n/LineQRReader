package com.meloda.lineqrreader.fragment.ui

import androidx.lifecycle.MutableLiveData
import com.meloda.lineqrreader.base.answer.Answer
import moxy.MvpPresenter

class InputCodePresenter : MvpPresenter<InputCodeView>() {

    val answer = MutableLiveData<Answer?>()

}