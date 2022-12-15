package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.Color;
import com.lassis.sensysgatso.chess.model.Piece;
import com.lassis.sensysgatso.chess.model.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Set;

import static com.lassis.sensysgatso.chess.game.ChessGame.SIZE_8;
import static com.lassis.sensysgatso.chess.game.ChessGameTest.at;
import static org.assertj.core.api.Assertions.assertThat;


class PawnTest {

    @DisplayName("Parameterized Test")
    @ParameterizedTest(name = "{2}")
    @ArgumentsSource(PawnArgumentProvider.class)
    void genericTest(Piece piece, Set<Point> expected, String description) {
        Set<Point> points = piece.allowedMoves();
        assertThat(points).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void allow_diagonal_move_when_find_a_opponent() {
        Board board = new Board(SIZE_8, SIZE_8);

        Pawn blackPawn = pawn(board, Color.BLACK, 3, 4);
        Pawn whitePawn = pawn(board, Color.WHITE, 4, 5);

        Set<Point> blackMoves = blackPawn.allowedMoves();
        assertThat(blackMoves).containsExactlyInAnyOrder(at(4, 4), at(5, 4), at(4, 5));

        Set<Point> whiteMoves = whitePawn.allowedMoves();
        assertThat(whiteMoves).containsExactlyInAnyOrder(at(3, 5), at(2, 5), at(3, 4));
    }

    @Test
    void do_not_allow_diagonal_move_when_is_not_an_opponent() {
        Board board = new Board(SIZE_8, SIZE_8);

        Pawn blackPawn1 = pawn(board, Color.BLACK, 3, 4);
        Pawn blackPawn2 = pawn(board, Color.BLACK, 4, 5);

        Set<Point> moves1 = blackPawn1.allowedMoves();
        assertThat(moves1).containsExactlyInAnyOrder(at(4, 4), at(5, 4));

        Set<Point> moves2 = blackPawn2.allowedMoves();
        assertThat(moves2).containsExactlyInAnyOrder(at(5, 5), at(6, 5));
    }

    @Test
    void do_not_allow_move_when_there_opponent_ahead() {
        Board board = new Board(SIZE_8, SIZE_8);

        Pawn pawn1 = pawn(board, Color.BLACK, 3, 4);
        Pawn pawn2 = pawn(board, Color.WHITE, 4, 4);

        Set<Point> blackMoves = pawn1.allowedMoves();
        assertThat(blackMoves).isEmpty();

        Set<Point> whiteMoves = pawn2.allowedMoves();
        assertThat(whiteMoves).isEmpty();
    }

    @Test
    void do_not_allow_move_when_same_ahead() {
        Board board = new Board(SIZE_8, SIZE_8);

        Pawn pawn1 = pawn(board, Color.BLACK, 3, 4);
        Pawn pawn2 = pawn(board, Color.BLACK, 4, 4);

        Set<Point> moves1 = pawn1.allowedMoves();
        assertThat(moves1).isEmpty();

        Set<Point> moves2 = pawn2.allowedMoves();
        assertThat(moves2).containsExactlyInAnyOrder(at(5, 4), at(6, 4));
    }

    @Test
    void allow_only_diagonal_move() {
        Board board = new Board(SIZE_8, SIZE_8);

        Pawn pawn1 = pawn(board, Color.BLACK, 3, 4);
        Pawn pawn2 = pawn(board, Color.BLACK, 4, 4);
        Pawn pawn3 = pawn(board, Color.WHITE, 4, 5);

        Set<Point> moves1 = pawn1.allowedMoves();
        assertThat(moves1).containsExactlyInAnyOrder(at(4, 5)); // diagonal

        Set<Point> moves2 = pawn2.allowedMoves();
        assertThat(moves2).containsExactlyInAnyOrder(at(5, 4), at(6, 4));

        Set<Point> moves3 = pawn3.allowedMoves();
        assertThat(moves3).containsExactlyInAnyOrder(at(3, 5), at(2, 5), at(3, 4));
    }

    private Pawn pawn(Board board, Color color, int row, int column) {
        return new Pawn(color, board, at(row, column));
    }
}