package com.ruma.routeksp.service.impl

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.ruma.routeksp.service.BaseHandler
import com.ruma.annotations.Route

@Route("test3/test3/test3/")
class SimpleRouteTest3 : BaseHandler {
    override fun onHandler(context: Context, needLogin: Boolean, bundle: Bundle) {
        Log.d("alex","333333")
    }
}