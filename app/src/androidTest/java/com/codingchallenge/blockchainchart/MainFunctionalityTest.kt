package com.codingchallenge.blockchainchart

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import com.codingchallenge.blockchainchart.di.NetworkModule
import com.codingchallenge.blockchainchart.presentation.MainActivity
import com.codingchallenge.blockchainchart.utils.getStringFrom
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@UninstallModules(NetworkModule::class)
@HiltAndroidTest
class MainFunctionalityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    //    @get:Rule
//    val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)
    @get:Rule
    val activityScenarioTestRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var mockServer: MockWebServer

    @Before
    fun setUp() {
        hiltRule.inject()
        mockServer = MockWebServer()
        mockServer.start(8080)
    }

    @Test
    fun checkIfProgressBarAppearsWhenLoadingStarts() {
        setDispatcher("valid_course.json", 200)
        waitUntilProgressBarAppears(R.id.progress_pb, 1000)
        waitUntilProgressBarDisappears(R.id.progress_pb, 1000)
    }

    private fun setDispatcher(fileName: String, responseCode: Int) {
        mockServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(responseCode)
                    .setBody(getStringFrom(fileName))
            }
        }
    }

    fun waitUntilProgressBarAppears(
        viewId: Int,
        timeoutInMs: Long = 3
    ) {
        getUiObject(viewId).waitForExists(timeoutInMs)
    }

    fun waitUntilProgressBarDisappears(
        viewId: Int,
        timeoutInMs: Long = 3
    ) {
        getUiObject(viewId).waitUntilGone(timeoutInMs)
    }

    private fun getUiObject(viewId: Int): UiObject {
        val resourceId =
            ApplicationProvider.getApplicationContext<Context>().resources.getResourceName(viewId)
        val selector = UiSelector().resourceId(resourceId)
        return UiDevice.getInstance(androidx.test.platform.app.InstrumentationRegistry.getInstrumentation())
            .findObject(selector)
    }

}