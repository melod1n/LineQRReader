package com.meloda.lineqrreader.fragment.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.activity.AuthActivity
import com.meloda.lineqrreader.base.answer.Answer
import com.meloda.lineqrreader.util.Utils
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import moxy.presenterScope
import java.util.*
import kotlin.concurrent.schedule

class InputNumberPresenter(private var context: Fragment) : MvpPresenter<InputNumberView>() {

    val answer = MutableLiveData<Answer?>()


    fun sendPhoneNumber(number: String) = presenterScope.launch {
        if (!validateNumber(number)) {
            answer.value = Answer.FAIL.also {
                it.message = context.getString(
                    if (number == "+7") R.string.error_enter_phone_number
                    else R.string.error_invalid_phone_number
                )
            }
            return@launch
        }

        viewState.hideKeyboard()
        answer.value = Answer.LOADING

        try {
            Timer().schedule(3000) {
                answer.postValue(Answer.SUCCESS)

                Timer().schedule(250) {
                    (context.requireActivity() as AuthActivity).presenter.showInputCodeScreen()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()

            answer.value = Answer.FAIL.also { it.message = e.message }
        }
    }

    private fun validateNumber(string: String): Boolean {
        return string.matches(Regex(Utils.PHONE_NUMBER_PATTERN))

    }

}