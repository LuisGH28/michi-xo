package com.luigidev.michixo.domain

import com.luigidev.michixo.model.Player
import com.luigidev.michixo_core.model.Difficulty

class AiPlayer(
    private val difficulty: Difficulty = Difficulty.EASY
) {

    fun chooseMove(board: List<Player>, ai: Player): Int? {
        return when (difficulty) {
            Difficulty.EASY -> chooseEasyMove(board, ai)
            Difficulty.MEDIUM -> chooseMediumMove(board, ai)
            Difficulty.HARD -> chooseHardMove(board, ai)
        }
    }

    private fun chooseEasyMove(board: List<Player>, ai: Player): Int? {
        val opponent = opponentOf(ai)

        findWinningMove(board, ai)?.let { return it }
        findWinningMove(board, opponent)?.let { return it }

        return emptyCells(board).randomOrNull()
    }

    private fun chooseMediumMove(board: List<Player>, ai: Player): Int? {
        val opponent = opponentOf(ai)

        findWinningMove(board, ai)?.let { return it }
        findWinningMove(board, opponent)?.let { return it }

        if (board[4] == Player.NONE) return 4

        val corners = listOf(0, 2, 6, 8).filter { board[it] == Player.NONE }
        if (corners.isNotEmpty()) return corners.random()

        return emptyCells(board).randomOrNull()
    }

    private fun chooseHardMove(board: List<Player>, ai: Player): Int? {
        val human = opponentOf(ai)
        val empty = emptyCells(board)

        var bestScore = Int.MIN_VALUE
        var bestMove: Int? = null

        for (move in empty) {
            val newBoard = board.toMutableList()
            newBoard[move] = ai

            val score = minimax(
                board = newBoard,
                depth = 0,
                isMaximizing = false,
                ai = ai,
                human = human,
                alpha = Int.MIN_VALUE,
                beta = Int.MAX_VALUE
            )

            if (score > bestScore) {
                bestScore = score
                bestMove = move
            }
        }

        return bestMove
    }

    private fun minimax(
        board: MutableList<Player>,
        depth: Int,
        isMaximizing: Boolean,
        ai: Player,
        human: Player,
        alpha: Int,
        beta: Int
    ): Int {
        val winner = Rules.checkWinner(board)

        if (winner?.player == ai) return 10 - depth
        if (winner?.player == human) return depth - 10
        if (board.none { it == Player.NONE }) return 0

        var localAlpha = alpha
        var localBeta = beta

        if (isMaximizing) {
            var bestScore = Int.MIN_VALUE

            for (i in board.indices) {
                if (board[i] == Player.NONE) {
                    board[i] = ai
                    val score = minimax(board, depth + 1, false, ai, human, localAlpha, localBeta)
                    board[i] = Player.NONE

                    bestScore = maxOf(bestScore, score)
                    localAlpha = maxOf(localAlpha, bestScore)

                    if (localBeta <= localAlpha) break
                }
            }
            return bestScore
        } else {
            var bestScore = Int.MAX_VALUE

            for (i in board.indices) {
                if (board[i] == Player.NONE) {
                    board[i] = human
                    val score = minimax(board, depth + 1, true, ai, human, localAlpha, localBeta)
                    board[i] = Player.NONE

                    bestScore = minOf(bestScore, score)
                    localBeta = minOf(localBeta, bestScore)

                    if (localBeta <= localAlpha) break
                }
            }
            return bestScore
        }
    }

    private fun findWinningMove(board: List<Player>, player: Player): Int? {
        for (i in board.indices) {
            if (board[i] == Player.NONE) {
                val copy = board.toMutableList()
                copy[i] = player
                if (Rules.checkWinner(copy)?.player == player) {
                    return i
                }
            }
        }
        return null
    }

    private fun emptyCells(board: List<Player>): List<Int> {
        return board.withIndex()
            .filter { it.value == Player.NONE }
            .map { it.index }
    }

    private fun opponentOf(player: Player): Player {
        return if (player == Player.X) Player.O else Player.X
    }
}
