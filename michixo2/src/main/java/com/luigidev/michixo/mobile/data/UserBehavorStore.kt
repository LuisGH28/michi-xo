package com.luigidev.michixo.mobile.data

import android.content.Context
import com.luigidev.michixo.mobile.model.GameResult
import java.time.LocalDate

class UserBehaviorStore(context: Context) {

    private val prefs = context.getSharedPreferences("michixo_behavior", Context.MODE_PRIVATE)

    fun saveGamePlayed(result: GameResult) {
        val count = prefs.getInt("games_played", 0)

        prefs.edit()
            .putString("last_result", result.name)
            .putLong("last_played_day", LocalDate.now().toEpochDay())
            .putInt("games_played", count + 1)
            .apply()
    }

    fun getLastResult(): GameResult {
        return try {
            GameResult.valueOf(
                prefs.getString("last_result", GameResult.NONE.name)!!
            )
        } catch (e: Exception) {
            GameResult.NONE
        }
    }

    fun getDaysSinceLastPlay(): Long {
        val last = prefs.getLong("last_played_day", -1)
        if (last == -1L) return Long.MAX_VALUE
        return LocalDate.now().toEpochDay() - last
    }

    fun getGamesPlayed(): Int {
        return prefs.getInt("games_played", 0)
    }
}