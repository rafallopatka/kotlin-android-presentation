package com.example.rlopatka.kotlinsamples

import android.app.Activity
import android.app.Application
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.androidActivityScope
import com.github.salomonbrys.kodein.android.androidModule
import com.github.salomonbrys.kodein.android.appKodein

/**
 * Created by rlopatka on 29.10.2017.
 */

interface FooPort
class FooAdapter: FooPort

interface BarPort
class BarAdapter(foo: FooPort): BarPort

interface NavigationService
class ActivityNavigationService(activity: Activity): NavigationService

val kodeinModule = Kodein.Module {
    import(androidModule)

    bind<FooPort>() with provider { FooAdapter() }
    bind<BarPort>() with singleton { BarAdapter(instance()) }

    bind<NavigationService>() with autoScopedSingleton(androidActivityScope, { ActivityNavigationService(it) })
}

class SampleApplication() : Application(), KodeinAware {
    override val kodein by Kodein.lazy {
        import(kodeinModule)
    }

    override fun onCreate() {
        registerActivityLifecycleCallbacks(androidActivityScope.lifecycleManager)
        super.onCreate()
    }
}

class InjectedActivity : Activity(), LazyKodeinAware {
    override val kodein = LazyKodein(appKodein)

    val inflater: BarPort by instance()
}