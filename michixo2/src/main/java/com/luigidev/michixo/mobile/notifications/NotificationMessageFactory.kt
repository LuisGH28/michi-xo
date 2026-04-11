package com.luigidev.michixo.mobile.notifications

import android.content.Context
import com.luigidev.michixo.mobile.R
import com.luigidev.michixo.mobile.model.GameResult

object NotificationMessageFactory {

    fun createMessage(
        context: Context,
        daysSinceLastPlay: Long,
        lastResult: GameResult,
        gamesPlayedCount: Int
    ): Pair<String, String> {

        if (gamesPlayedCount == 0) {
            return context.getString(R.string.notification_first_time_title) to
                    context.getString(R.string.notification_first_time_body)
        }

        if (daysSinceLastPlay >= 3) {
            return context.getString(R.string.notification_miss_you_title) to
                    context.getString(R.string.notification_miss_you_body)
        }

        return when (lastResult) {
            GameResult.WIN ->
                context.getString(R.string.notification_win_title) to
                        context.getString(R.string.notification_win_body)

            GameResult.LOSE ->
                context.getString(R.string.notification_lose_title) to
                        context.getString(R.string.notification_lose_body)

            GameResult.DRAW ->
                context.getString(R.string.notification_draw_title) to
                        context.getString(R.string.notification_draw_body)

            GameResult.NONE ->
                context.getString(R.string.notification_title) to
                        context.getString(R.string.notification_body)
        }
    }
}