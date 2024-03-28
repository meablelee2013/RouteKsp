package com.ruma.routeksp.service

import android.content.Context
import android.os.Bundle

interface BaseHandler {
    fun onHandler(context: Context,needLogin:Boolean,bundle: Bundle)
}