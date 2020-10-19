package com.codingchallenge.blockchainchart

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.codingchallenge.blockchainchart.di.NetworkModule
import com.codingchallenge.blockchainchart.utils.waitUntilViewAppears
import com.codingchallenge.blockchainchart.utils.waitUntilViewDisappears
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@UninstallModules(NetworkModule::class)
@HiltAndroidTest
class MainFunctionalityTest : BaseTest() {
    private lateinit var uiDevice: UiDevice

    @Test
    fun checkIfProgressBarAppearsWhenLoadingStarts() {
        setDispatcher("valid_course.json", 200)
        waitUntilViewAppears(R.id.progress_pb, 500)
        waitUntilViewDisappears(R.id.progress_pb, 500)
    }

    @Test
    fun checkIfDataIsValid_shouldShowGraphView() {
        setDispatcher("valid_course.json", 200)
        waitUntilViewAppears(R.id.line_chart_view, 500)
    }

    @Test
    fun checkIfDeviceRotates_shouldSendOnlyOneNetworkRequest() {
        setDispatcher("valid_course.json", 200)
        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        uiDevice.unfreezeRotation()
        uiDevice.setOrientationNatural()
        waitUntilViewAppears(R.id.line_chart_view, 500)
        onView(withId(R.id.line_chart_view)).check(matches(isDisplayed()))
        uiDevice.setOrientationLeft()
        activityScenarioTestRule.scenario.recreate()
        waitUntilViewAppears(R.id.line_chart_view, 500)
        onView(withId(R.id.line_chart_view)).check(matches(isDisplayed()))
        uiDevice.setOrientationNatural()
        activityScenarioTestRule.scenario.recreate()
        assertEquals(1, mockServer.requestCount)
    }

}