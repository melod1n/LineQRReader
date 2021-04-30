package com.meloda.lineqrreader.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.meloda.lineqrreader.R

class DividerItemDecoration : RecyclerView.ItemDecoration {

    companion object {
        private val ATTRS = intArrayOf(R.attr.dividerHorizontal)
    }

    private lateinit var divider: Drawable

    //default drawable
    constructor(context: Context) {
        val styledAttributes = context.obtainStyledAttributes(ATTRS)
        divider = styledAttributes.getDrawable(0) ?: return
        styledAttributes.recycle()
    }

    //custom drawable
    constructor(context: Context, resId: Int) {
        divider = ContextCompat.getDrawable(context, resId) ?: return
    }

    //set items' margins
//    override fun getItemOffsets(
//        outRect: Rect,
//        view: View,
//        parent: RecyclerView,
//        state: RecyclerView.State
//    ) {
//        super.getItemOffsets(outRect, view, parent, state)
//
////        if (parent.getChildAdapterPosition(view) == 0) return
//
//        outRect.right = AndroidUtils.px(16).roundToInt()
//        outRect.left = AndroidUtils.px(16).roundToInt()
//    }


    //set dividers' margins
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
//        val paddingInPx = AndroidUtils.px(16).roundToInt()

        val paddingInPx = 0

        val left = paddingInPx
        val right = parent.width - paddingInPx
        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin

            val bottom = top + divider.intrinsicHeight

            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }


}