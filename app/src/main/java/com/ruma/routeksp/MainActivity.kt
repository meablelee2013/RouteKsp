package com.ruma.routeksp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cn.jailedbird.arouter.ksp.service.Router

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun test(view: View) {
        Router.with(this@MainActivity).withBundle(Router.obtain("alex","haha")).handler("test2/test2/test2/")
    }
}