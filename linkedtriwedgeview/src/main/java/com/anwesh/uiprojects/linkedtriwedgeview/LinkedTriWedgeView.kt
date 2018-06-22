package com.anwesh.uiprojects.linkedtriwedgeview

/**
 * Created by anweshmishra on 22/06/18.
 */
import android.view.View
import android.view.MotionEvent
import android.graphics.*
import android.content.Context

val TW_NODES : Int = 5

class LinkedTriWedgeView (ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(stopcb : (Float) -> Unit) {
            scale += 0.1f * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                stopcb(prevScale)
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class TWNode (var i : Int, val state : State = State()) {

        private var next : TWNode? = null

        private var prev : TWNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < TW_NODES - 1) {
                next = TWNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            val gap : Float = w / TW_NODES
            prev?.draw(canvas, paint)
            paint.color = Color.parseColor("#E1E1E1")
            canvas.save()
            canvas.translate((i - 1) * gap + gap * state.scale, h / 2)
            val path : Path = Path()
            path.moveTo(0f, 0f)
            path.lineTo(gap, 0f)
            path.lineTo(gap, -gap)
            path.lineTo(0f, 0f)
            canvas.drawPath(path, paint)
            canvas.restore()
        }

        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : TWNode {
            var curr : TWNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class LinkedTriWedge(var i : Int) {

        private var curr : TWNode = TWNode(0)

        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            curr.draw(canvas, paint)
        }

        fun update(stopcb : (Float) -> Unit) {
            curr.update {
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                stopcb(it)
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            curr.startUpdating(startcb)
        }
    }

    data class Renderer(var view : LinkedTriWedgeView) {

        private val animator : Animator = Animator(view)

        private val ltw : LinkedTriWedge = LinkedTriWedge(0)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            ltw.draw(canvas, paint)
            animator.animate {
                ltw.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            ltw.startUpdating {
                animator.start()
            }
        }
    }
}