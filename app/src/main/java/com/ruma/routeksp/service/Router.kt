package cn.jailedbird.arouter.ksp.service

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import com.ruma.annotations.RouteInfo
import com.ruma.routeksp.service.BaseHandler
import java.io.Serializable
import java.lang.ref.WeakReference


object Router {
    private var bundle: Bundle? = null
    private var appContext: WeakReference<Context>? = null
    private val map: HashMap<String, RouteInfo> = HashMap()

    fun with(context: Context): Router {
        appContext = WeakReference(context.applicationContext)
        return this
    }

    fun withBundle(bundle: Bundle?): Router {
        this.bundle = bundle
        return this
    }

    fun init() {
        try {
            // 1. 获取对象的类
            val autoInjectClass = Class.forName("com.ruma.RouterMap")

            // 2. 获取方法的引用
            val initMethod = autoInjectClass.getDeclaredMethod("initRoute")
            val getRouterMapMethod = autoInjectClass.getDeclaredMethod("getRouterMap")

            // 创建 AutoInject 实例
            val autoInjectInstance = autoInjectClass.getDeclaredField("INSTANCE").get(null)

            // 3. 调用方法
            initMethod.invoke(autoInjectInstance)
            val routerMap = getRouterMapMethod.invoke(autoInjectInstance) as HashMap<String, RouteInfo>
            routerMap?.let {
                map.putAll(it)
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    fun handler(path: String) {

        if (appContext == null || appContext?.get() == null) {
            throw IllegalStateException("Router is not initialized. Call Router.with(context) before using it.")
        }

        if (map.isEmpty()) {
            init()
        }

        val routeInfo = map[path]
        routeInfo?.let {
            val className = routeInfo.clazz
            val needLogin = routeInfo.needLogin
            val context = appContext?.get()
            // 这里执行相应的操作，例如实例化类并调用其方法
            try {
                val clazz = Class.forName(className)
                val instance = clazz.newInstance() as BaseHandler
                instance.onHandler(context!!, needLogin ?: false, bundle ?: Bundle())
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        } ?: run {
            // 处理未找到路由信息的情况
            println("Route not found for path: $path")
        }
    }

    fun obtain(vararg args: Any): Bundle? {
        val b = Bundle()
        val n = args?.size ?: 0
        var i = 0
        while (i < n) {
            val key = args[i].toString()
            val `val` = args[i + 1]
            if (`val` is CharSequence) {
                b.putString(key, `val`.toString())
            } else if (`val` is Boolean) {
                b.putBoolean(key, `val`)
            } else if (`val` is Int) {
                b.putInt(key, `val`)
            } else if (`val` is Parcelable) {
                b.putParcelable(key, `val`)
            } else if (`val` is Serializable) {
                b.putSerializable(key, `val`)
            }
            i += 2
        }
        return b
    }
}

