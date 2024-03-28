package com.ruma.routeksp.service.impl

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.ruma.routeksp.service.BaseHandler
import com.ruma.annotations.Route

@Route(path = "test1/test1/test1/", needLogin = true)
class SimpleRouteTest1 : BaseHandler {
    override fun onHandler(context: Context, needLogin: Boolean, bundle: Bundle) {
        Log.d("alex", "111111111111")
    }
}