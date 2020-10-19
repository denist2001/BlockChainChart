package com.codingchallenge.blockchainchart.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector

fun waitUntilViewAppears(
    viewId: Int,
    timeoutInMs: Long = 3
) {
    getUiObject(viewId).waitForExists(timeoutInMs)
}

fun waitUntilViewDisappears(
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