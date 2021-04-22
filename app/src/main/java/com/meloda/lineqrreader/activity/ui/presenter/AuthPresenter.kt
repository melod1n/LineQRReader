package com.meloda.lineqrreader.activity.ui.presenter

import androidx.lifecycle.MutableLiveData
import com.meloda.lineqrreader.activity.ui.view.AuthView
import kotlinx.coroutines.coroutineScope
import moxy.MvpPresenter
import java.util.*
import kotlin.concurrent.schedule

class AuthPresenter() : MvpPresenter<AuthView>() {

    val authAnswer = MutableLiveData<AuthAnswer?>()

    data class AuthAnswer(var status: Status, var message: String? = "") {
        companion object {
            val SUCCESS get() = AuthAnswer(Status.SUCCESS)
            val FAIL get() = AuthAnswer(Status.FAIL)
            val LOADING get() = AuthAnswer(Status.LOADING)
        }

        enum class Status {
            SUCCESS, FAIL, LOADING
        }
    }

    suspend fun sendPhoneNumber() = coroutineScope {
        authAnswer.value = AuthAnswer.LOADING

        try {
            Timer().schedule(3000) {
                authAnswer.postValue(AuthAnswer.SUCCESS)
            }
        } catch (e: Exception) {
            e.printStackTrace()

            authAnswer.value = AuthAnswer.FAIL.also { it.message = e.message }
        }
    }
}