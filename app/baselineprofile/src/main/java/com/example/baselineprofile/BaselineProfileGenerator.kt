package com.example.baselineprofile

import android.provider.ContactsContract.Directory.PACKAGE_NAME
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test

/**
 * This test class generates a basic startup baseline profile for the target package.
 *
 * We recommend you start with this but add important user flows to the profile to improve their performance.
 * Refer to the [baseline profile documentation](https://d.android.com/topic/performance/baselineprofiles)
 * for more information.
 *
 * You can run the generator with the "Generate Baseline Profile" run configuration in Android Studio or
 * the equivalent `generateBaselineProfile` gradle task:
 * ```
 * ./gradlew :app:generateReleaseBaselineProfile
 * ```
 * The run configuration runs the Gradle task and applies filtering to run only the generators.
 *
 * Check [documentation](https://d.android.com/topic/performance/benchmarking/macrobenchmark-instrumentation-args)
 * for more information about available instrumentation arguments.
 *
 * After you run the generator, you can verify the improvements running the [StartupBenchmarks] benchmark.
 *
 * When using this class to generate a baseline profile, only API 33+ or rooted API 28+ are supported.
 *
 * The minimum required version of androidx.benchmark to generate a baseline profile is 1.2.0.
 **/
class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun appStartupAndUserJourneys() {
        baselineProfileRule.collect(packageName = PACKAGE_NAME) {
            startActivityAndWait()
            device.findObject(By.text("COMPOSE LAZYLIST")).clickAndWait(Until.newWindow(), 1_000)
            device.findObject(By.res("myLazyColumn")).also {
                it.fling(Direction.DOWN)
                it.fling(Direction.UP)
            }
            device.pressBack()
        }
        baselineProfileRule.collect(packageName = PACKAGE_NAME) {
            startActivityAndWait()
            device.findObject(By.text("COMPOSE LAZYLIST")).clickAndWait(Until.newWindow(), 1_000)
            device.findObject(By.res("myLazyRow")).also {
                it.fling(Direction.LEFT)
                it.fling(Direction.RIGHT)
            }
            device.pressBack()
        }
    }
}