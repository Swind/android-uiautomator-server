/*
 * The MIT License (MIT)
 * Copyright (c) 2015 xiaocong@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.uiautomator.stub

//import android.support.test.filters.FlakyTest;
import android.net.LocalServerSocket
import android.support.test.InstrumentationRegistry
import android.support.test.filters.LargeTest
import android.support.test.filters.SdkSuppress
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.UiDevice
import android.test.FlakyTest
import com.fasterxml.jackson.databind.ObjectMapper
import com.googlecode.jsonrpc4j.JsonRpcServer
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Use JUnit test to start the uiautomator jsonrpc server.
 * @author xiaocong@gmail.com
 */
@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class Stub {
    @Test
    @LargeTest
    @FlakyTest(tolerance = 3)
    @Throws(InterruptedException::class)
    fun testUIAutomatorLocalStreamStub() {
        val jsonRpcServer = JsonRpcServer(ObjectMapper(), AutomatorServiceImpl(), AutomatorService::class.java)
        val localStreamServer = LocalStreamServer(jsonRpcServer, "myuiautomator", 5)
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).wakeUp()
        localStreamServer.start()
    }

    @Test
    @LargeTest
    @FlakyTest(tolerance = 3)
    @Throws(InterruptedException::class)
    fun testUIAutomatorHttpStub() {
        val PORT = 9008
        val server = AutomatorHttpServer(PORT)

        try{
            server.route("/jsonrpc/0", JsonRpcServer(ObjectMapper(), AutomatorServiceImpl(), AutomatorService::class.java))
            server.start()
            UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).wakeUp()

            while(server.isAlive())
                Thread.sleep(100)

        }finally{
            server.stop()
        }
    }
}