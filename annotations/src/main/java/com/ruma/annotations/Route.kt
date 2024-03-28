package com.ruma.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Route(val path: String = "", val needLogin: Boolean = false)
