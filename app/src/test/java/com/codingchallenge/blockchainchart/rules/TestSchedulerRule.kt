package com.codingchallenge.blockchainchart.rules

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class TestSchedulerRule : TestRule {
    private val _testScheduler = TestScheduler()

    fun getTestScheduler(): TestScheduler {
        return _testScheduler;
    }

    override fun apply(base: Statement, d: Description): Statement {
        return object: Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setIoSchedulerHandler {
                    scheduler -> _testScheduler }
                RxJavaPlugins.setComputationSchedulerHandler {
                    scheduler -> _testScheduler }
                RxJavaPlugins.setNewThreadSchedulerHandler {
                    scheduler -> _testScheduler }
                RxAndroidPlugins.setMainThreadSchedulerHandler {
                    scheduler -> Schedulers.trampoline() }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}