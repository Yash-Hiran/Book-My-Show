package com.demo

import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import javax.inject.Inject
import javax.sql.DataSource

abstract class IntegrationSpec : StringSpec() {
    @Inject
    protected open lateinit var dataSource: DataSource

    override fun afterEach(testCase: TestCase, result: TestResult) {
        super.afterEach(testCase, result)
        clearData()
    }

    abstract fun clearData(): Int
}
