package com.meloda.lineqrreader.activity.ui.presenter

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.meloda.lineqrreader.activity.ui.repository.AuthRepository
import com.meloda.lineqrreader.activity.ui.view.AuthView
import com.meloda.mvp.MvpPresenter
import kotlinx.coroutines.coroutineScope
import java.util.*
import kotlin.concurrent.schedule

class AuthPresenter(
    viewState: AuthView
) : MvpPresenter<Any, AuthRepository, AuthView>(
    viewState,
    AuthRepository::class.java.name
) {

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

    override fun onCreate(context: Context, bundle: Bundle?) {
        super.onCreate(context, bundle)

        init()
    }

    private fun init() {

    }

    suspend fun sendPhoneNumber(number: String) = coroutineScope {
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