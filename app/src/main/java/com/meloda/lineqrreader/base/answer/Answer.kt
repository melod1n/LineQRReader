package com.meloda.lineqrreader.base.answer

data class Answer(
    var status: Status,
    var message: String? = ""
) {
    companion object {
        val SUCCESS get() = Answer(Status.SUCCESS)
        val LOADING get() = Answer(Status.LOADING)
        val FAIL get() = Answer(Status.FAIL)
    }
}