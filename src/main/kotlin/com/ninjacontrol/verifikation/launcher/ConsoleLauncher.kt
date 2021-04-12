package com.ninjacontrol.verifikation.launcher

import com.ninjacontrol.verifikation.engine.VerifikationEngine
import org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage
import org.junit.platform.launcher.EngineFilter.includeEngines
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.launcher.core.LauncherFactory

class ConsoleLauncher {
    fun launch(packageName: String) {

        val request = LauncherDiscoveryRequestBuilder.request()
            .selectors(
                selectPackage(packageName)
            ).filters(includeEngines(VerifikationEngine.engineId)).build()
        val launcher = LauncherFactory.create()
        val testPlan = launcher.discover(request)
        // TODO: run!
    }
}

fun main() {
    val l = ConsoleLauncher()
    l.launch("com.ninjacontrol.verifikation")
}
