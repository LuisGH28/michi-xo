package com.luigidev.michixo.mobile.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class RemoteAiClient(
    private val url: String,
    private val onConnected: (() -> Unit)? = null,
    private val onMessageReceived: (String) -> Unit,
    private val onError: ((Throwable) -> Unit)? = null
) : WebSocketListener() {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    fun connect() {
        val request = Request.Builder()
            .url(url)
            .build()

        webSocket = client.newWebSocket(request, this)
    }

    fun send(message: String) {
        webSocket?.send(message)
    }

    fun disconnect() {
        webSocket?.close(1000, "Client closed connection")
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        onConnected?.invoke()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        onMessageReceived(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        onMessageReceived(bytes.utf8())
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        onError?.invoke(t)
    }
}