package com.anwesh.uiprojects.linkedtriwedgeview

/**
 * Created by anweshmishra on 22/06/18.
 */
import android.view.View
import android.view.MotionEvent
import android.graphics.*
import android.content.Context

class LinkedTriWedgeView (ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                
            }
        }
        return true
    }
}