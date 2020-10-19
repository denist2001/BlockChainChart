package com.codingchallenge.blockchainchart

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.codingchallenge.blockchainchart.di.NetworkModule
import com.codingchallenge.blockchainchart.utils.waitUntilViewAppears
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@UninstallModules(NetworkModule::class)
@HiltAndroidTest
class ErrorFunctionalityTest : BaseTest() {

    @Test
    fun checkIfNetworkAppearsWhenLoadingStarts() {
        setDispatcher("valid_course.json", 404)
        waitUntilViewAppears(R.id.error_tv, 500)
        onView(withText("Network error")).check(matches(isDisplayed()))
    }

    @Test
    fun checkIfDataIsInvalid_shouldShowError() {
        setDispatcher("invalid_course.json", 200)
        waitUntilViewAppears(R.id.error_tv, 500)
        onView(withText("Parsing error")).check(matches(isDisplayed()))
    }

}