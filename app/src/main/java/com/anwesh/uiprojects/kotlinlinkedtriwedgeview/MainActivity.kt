package com.anwesh.uiprojects.kotlinlinkedtriwedgeview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.linkedtriwedgeview.LinkedTriWedgeView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LinkedTriWedgeView.create(this)
    }
}
