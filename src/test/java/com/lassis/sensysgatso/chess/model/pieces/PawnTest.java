package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.Color;
import com.lassis.sensysgatso.chess.model.Piece;
import com.lassis.sensysgatso.chess.model.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Set;

import static com.lassis.sensysgatso.chess.ChessGame.SIZE;
import static com.lassis.sensysgatso.chess.ChessGameTest.at;
import static org.assertj.core.api.Assertions.assertThat;


class PawnTest {

    @DisplayName("Parameterized Test")
    @ParameterizedTest(name = "{2}")
    @ArgumentsSource(PawnArgumentProvider.class)
    void genericTest(Piece piece, Set<Position> expected, String description) {
        Set<Position> positions = piece.allowedMoves();
        assertThat(positions).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void allow_diagonal_move_when_find_a_opponent() {
        Board board = new Board(SIZE, SIZE);

        Pawn blackPawn = pawn(board, Color.BLACK, 3, 4);
        Pawn whitePawn = pawn(board, Color.WHITE, 4, 5);

        Set<Position> blackMoves = blackPawn.allowedMoves();
        assertThat(blackMoves).containsExactlyInAnyOrderElementsOf(Set.of(at(4, 4), at(5, 4), at(4, 5)));

        Set<Position> whiteMoves = whitePawn.allowedMoves();
        assertThat(whiteMoves).containsExactlyInAnyOrderElementsOf(Set.of(at(3, 5), at(2, 5), at(3, 4)));
    }

    @Test
    void do_not_allow_diagonal_move_when_is_not_an_opponent() {
        Board board = new Board(SIZE, SIZE);

        Pawn blackPawn1 = pawn(board, Color.BLACK, 3, 4);
        Pawn blackPawn2 = pawn(board, Color.BLACK, 4, 5);

        Set<Position> blackMoves = blackPawn1.allowedMoves();
        assertThat(blackMoves).containsExactlyInAnyOrderElementsOf(Set.of(at(4, 4), at(5, 4)));

        Set<Position> whiteMoves = blackPawn2.allowedMoves();
        assertThat(whiteMoves).containsExactlyInAnyOrderElementsOf(Set.of(at(5, 5), at(6, 5)));
    }

    private Pawn pawn(Board board, Color color, int row, int column) {
        Pawn pawn = new Pawn(color, board);
        board.place(pawn, at(row, column));
        return pawn;
    }
}