package com.gedanken.catstomers

import android.os.Build
import junit.framework.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Config.OLDEST_SDK])
    class MainActivityUnitTest {
    @Test
    @Throws(Exception::class)
    fun activityShouldNotBeNull() {
        val activity: MainActivity =
            Robolectric.buildActivity(MainActivity::class.java).create().resume().get()
        assertNotNull(activity)
    }
}



