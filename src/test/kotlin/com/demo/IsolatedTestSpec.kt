package com.demo

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec

open class IsolatedTestSpec : StringSpec() {
    override fun isolationMode() = IsolationMode.InstancePerTest
}
