package com.meloda.lineqrreader.util

import android.util.DisplayMetrics
import com.meloda.lineqrreader.common.AppGlobal


object AndroidUtils {

    fun px(dp: Float): Float {
        return dp * (AppGlobal.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun px(dp: Int) = px(dp.toFloat())

    fun dp(px: Float): Float {
        return px / (AppGlobal.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun dp(px: Int) = dp(px.toFloat())

}