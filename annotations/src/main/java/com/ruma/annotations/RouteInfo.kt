package com.ruma.annotations

import java.io.Serializable


data class RouteInfo(val clazz: String, val needLogin: Boolean? = false): Serializable