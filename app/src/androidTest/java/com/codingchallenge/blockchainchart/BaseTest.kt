package com.codingchallenge.blockchainchart

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.codingchallenge.blockchainchart.presentation.MainActivity
import com.codingchallenge.blockchainchart.utils.getStringFrom
import dagger.hilt.android.testing.HiltAndroidRule
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule

open class BaseTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityScenarioTestRule = ActivityScenarioRule(MainActivity::class.java)

    lateinit var mockServer: MockWebServer

    @Before
    fun setUp() {
        hiltRule.inject()
        mockServer = MockWebServer()
        mockServer.start(8080)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    fun setDispatcher(fileName: String, responseCode: Int) {
        mockServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(responseCode)
                    .setBody(getStringFrom(fileName))
            }
        }
    }
}