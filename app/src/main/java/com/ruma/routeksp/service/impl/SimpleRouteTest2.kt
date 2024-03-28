package com.ruma.routeksp.service.impl

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.ruma.routeksp.service.BaseHandler
import com.ruma.annotations.Route

@Route("test2/test2/test2/")
class SimpleRouteTest2 : BaseHandler {
    override fun onHandler(context: Context, needLogin: Boolean, bundle: Bundle) {
        val string = bundle.getString("alex")
        Log.d("alex","22222222222222  alex =$string")
    }
}