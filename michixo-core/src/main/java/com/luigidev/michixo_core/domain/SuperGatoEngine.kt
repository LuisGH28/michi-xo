package com.luigidev.michixo_core.domain

import com.luigidev.michixo.domain.Rules
import com.luigidev.michixo.model.Player
import com.luigidev.michixo_core.model.Difficulty

data class SuperGatoState(
    val cells: List<Player> = List(81) { Player.NONE },
    val boards: List<Player> = List(9) { Player.NONE },
    val fullBoards: List<Boolean> = List(9) { false },
    val activeBoard: Int? = null,
    val winner: Player? = null,
    val winLine: List<Int>? = null,
    val isDraw: Boolean = false
)

data class SuperMove(
    val boardIndex: Int,
    val cellIndex: Int
) {
    val flatIndex: Int = boardIndex * 9 + cellIndex
}

class SuperGatoEngine {

    fun newState(): SuperGatoState = SuperGatoState()

    fun makeMove(state: SuperGatoState, move: SuperMove, player: Player): SuperGatoState {
        if (state.winner != null || state.isDraw) return state
        if (move.boardIndex !in 0..8 || move.cellIndex !in 0..8) return state
        if (state.activeBoard != null && state.activeBoard != move.boardIndex) return state
        if (state.boards[move.boardIndex] != Player.NONE || state.fullBoards[move.boardIndex]) return state
        if (state.cells[move.flatIndex] != Player.NONE) return state

        val nextCells = state.cells.toMutableList()
        nextCells[move.flatIndex] = player

        val nextBoards = state.boards.toMutableList()
        val nextFullBoards = state.fullBoards.toMutableList()
        val smallBoard = boardAt(nextCells, move.boardIndex)
        val smallWinner = Rules.checkWinner(smallBoard)

        if (smallWinner != null) {
            nextBoards[move.boardIndex] = smallWinner.player
        }

        nextFullBoards[move.boardIndex] =
            smallWinner != null || smallBoard.none { it == Player.NONE }

        val macroWinner = Rules.checkWinner(nextBoards)
        val draw = macroWinner == null && nextFullBoards.all { it }
        val nextActiveBoard =
            if (nextBoards[move.cellIndex] == Player.NONE && !nextFullBoards[move.cellIndex]) {
                move.cellIndex
            } else {
                null
            }

        return SuperGatoState(
            cells = nextCells,
            boards = nextBoards,
            fullBoards = nextFullBoards,
            activeBoard = nextActiveBoard,
            winner = macroWinner?.player,
            winLine = macroWinner?.line,
            isDraw = draw
        )
    }

    fun legalMoves(state: SuperGatoState): List<SuperMove> {
        val boardsToPlay = state.activeBoard?.let { listOf(it) }
            ?: (0..8).filter { state.boards[it] == Player.NONE && !state.fullBoards[it] }

        return boardsToPlay.flatMap { boardIndex ->
            (0..8).mapNotNull { cellIndex ->
                val move = SuperMove(boardIndex, cellIndex)
                if (state.cells[move.flatIndex] == Player.NONE) move else null
            }
        }
    }

    fun chooseMove(
        state: SuperGatoState,
        ai: Player,
        difficulty: Difficulty
    ): SuperMove? {
        val moves = legalMoves(state)
        if (moves.isEmpty()) return null

        val human = opponentOf(ai)
        findMacroWinningMove(state, ai)?.let { return it }

        return when (difficulty) {
            Difficulty.EASY -> {
                findSmallWinningMove(state, ai)
                    ?: moves.random()
            }

            Difficulty.MEDIUM -> {
                findMacroWinningMove(state, human)
                    ?: findSmallWinningMove(state, ai)
                    ?: findSmallWinningMove(state, human)
                    ?: preferredMove(moves)
            }

            Difficulty.HARD -> {
                findMacroWinningMove(state, human)
                    ?: findSmallWinningMove(state, ai)
                    ?: findSmallWinningMove(state, human)
                    ?: moveThatSendsToClosedBoard(state, moves)
                    ?: preferredMove(moves)
            }
        }
    }

    private fun findMacroWinningMove(state: SuperGatoState, player: Player): SuperMove? {
        return legalMoves(state).firstOrNull { move ->
            val next = makeMove(state, move, player)
            next.winner == player
        }
    }

    private fun findSmallWinningMove(state: SuperGatoState, player: Player): SuperMove? {
        return legalMoves(state).firstOrNull { move ->
            val board = boardAt(state.cells, move.boardIndex).toMutableList()
            board[move.cellIndex] = player
            Rules.checkWinner(board)?.player == player
        }
    }

    private fun preferredMove(moves: List<SuperMove>): SuperMove {
        val preferredCells = listOf(4, 0, 2, 6, 8, 1, 3, 5, 7)
        for (cell in preferredCells) {
            moves.filter { it.cellIndex == cell }.randomOrNull()?.let { return it }
        }
        return moves.random()
    }

    private fun moveThatSendsToClosedBoard(
        state: SuperGatoState,
        moves: List<SuperMove>
    ): SuperMove? {
        return moves.filter { move ->
            state.boards[move.cellIndex] != Player.NONE || state.fullBoards[move.cellIndex]
        }.randomOrNull()
    }

    private fun boardAt(cells: List<Player>, boardIndex: Int): List<Player> {
        val start = boardIndex * 9
        return cells.subList(start, start + 9)
    }

    private fun opponentOf(player: Player): Player {
        return if (player == Player.X) Player.O else Player.X
    }
}
