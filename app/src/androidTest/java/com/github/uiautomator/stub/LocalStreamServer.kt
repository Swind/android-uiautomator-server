package com.github.uiautomator.stub

import android.net.LocalServerSocket
import com.googlecode.jsonrpc4j.JsonRpcServer
import java.io.BufferedInputStream
import java.io.IOException
import java.io.OutputStream

class LocalStreamServer(val jsonRpcServer: JsonRpcServer, val name: String, maxThreadNum: Int) {
    val localServerSocket = LocalServerSocket(name)

    init {
        jsonRpcServer.setRethrowExceptions(false)
    }

    fun start() {
        Log.d("StreamServer starting $name")
        while (true) {
            Log.i("Server is starting, waiting client connection...")
            val clientSocket = localServerSocket.accept()
            Log.i("Client connected: ${clientSocket.localSocketAddress}")

            var input: BufferedInputStream? = null
            var output: OutputStream? = null

            try {
                input = BufferedInputStream(clientSocket.inputStream)
                output = clientSocket.outputStream
                while(true){
                    jsonRpcServer.handleRequest(input, output)
                }
            } catch (e: IOException) {
                Log.e("Client socket disconnected or failed $e")
            }
            finally {
                if (input != null && output != null) {
                    input.close()
                    output.close()
                }
            }
        }
    }
}


