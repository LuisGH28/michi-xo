package com.luigidev.michixo.mobile.network

import com.luigidev.michixo.model.Player
import org.json.JSONArray
import org.json.JSONObject

object RemoteAiMapper {

    fun buildMoveRequest(
        board: List<Player>,
        aiSymbol: String,
        humanSymbol: String
    ): String {
        val boardArray = JSONArray(
            board.map {
                when (it) {
                    Player.X -> "X"
                    Player.O -> "O"
                    Player.NONE -> " "
                }
            }
        )

        return JSONObject()
            .put("board", boardArray)
            .put("ai_symbol", aiSymbol)
            .put("human_symbol", humanSymbol)
            .toString()
    }

    fun parseMoveResponse(message: String): Int? {
        val json = JSONObject(message)

        if (json.optString("type") != "move") return null

        val index = json.optInt("index", -1)
        return index.takeIf { it in 0..8 }
    }
}