package com.lassis.chess.model.pieces;

import com.lassis.chess.game.ChessGame;
import com.lassis.chess.game.ChessGameTest;
import com.lassis.chess.model.Board;
import com.lassis.chess.model.Color;
import com.lassis.chess.model.Piece;
import com.lassis.chess.model.Point;
import com.lassis.chess.model.Square;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class PawnTest {

    @DisplayName("Parameterized Test")
    @ParameterizedTest(name = "{3}")
    @ArgumentsSource(PawnArgumentProvider.class)
    void genericTest(Piece piece, Point point, Set<Point> expected, String description) {
        var board = new Board(ChessGame.SIZE_8, ChessGame.SIZE_8);
        board.place(piece, point);

        Set<Point> points = piece.allowedMoves(board, point);
        assertThat(points).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void allow_diagonal_move_when_find_a_opponent() {
        Board board = new Board(ChessGame.SIZE_8, ChessGame.SIZE_8);

        var blackPawn = pawn(board, Color.BLACK, 3, 4);
        var whitePawn = pawn(board, Color.WHITE, 4, 5);


        Set<Point> blackMoves = blackPawn.allowedMoves();
        assertThat(blackMoves).containsExactlyInAnyOrder(ChessGameTest.at(4, 4), ChessGameTest.at(5, 4), ChessGameTest.at(4, 5));

        Set<Point> whiteMoves = whitePawn.allowedMoves();
        assertThat(whiteMoves).containsExactlyInAnyOrder(ChessGameTest.at(3, 5), ChessGameTest.at(2, 5), ChessGameTest.at(3, 4));
    }

    @Test
    void do_not_allow_diagonal_move_when_is_not_an_opponent() {
        Board board = new Board(ChessGame.SIZE_8, ChessGame.SIZE_8);

        var blackPawn1 = pawn(board, Color.BLACK, 3, 4);
        var blackPawn2 = pawn(board, Color.BLACK, 4, 5);

        Set<Point> moves1 = blackPawn1.allowedMoves();
        assertThat(moves1).containsExactlyInAnyOrder(ChessGameTest.at(4, 4), ChessGameTest.at(5, 4));

        Set<Point> moves2 = blackPawn2.allowedMoves();
        assertThat(moves2).containsExactlyInAnyOrder(ChessGameTest.at(5, 5), ChessGameTest.at(6, 5));
    }

    @Test
    void do_not_allow_move_when_there_opponent_ahead() {
        Board board = new Board(ChessGame.SIZE_8, ChessGame.SIZE_8);

        var pawn1 = pawn(board, Color.BLACK, 3, 4);
        var pawn2 = pawn(board, Color.WHITE, 4, 4);

        Set<Point> blackMoves = pawn1.allowedMoves();
        assertThat(blackMoves).isEmpty();

        Set<Point> whiteMoves = pawn2.allowedMoves();
        assertThat(whiteMoves).isEmpty();
    }

    @Test
    void do_not_allow_move_when_same_ahead() {
        Board board = new Board(ChessGame.SIZE_8, ChessGame.SIZE_8);

        var pawn1 = pawn(board, Color.BLACK, 3, 4);
        var pawn2 = pawn(board, Color.BLACK, 4, 4);

        Set<Point> moves1 = pawn1.allowedMoves();
        assertThat(moves1).isEmpty();

        Set<Point> moves2 = pawn2.allowedMoves();
        assertThat(moves2).containsExactlyInAnyOrder(ChessGameTest.at(5, 4), ChessGameTest.at(6, 4));
    }

    @Test
    void allow_only_diagonal_move() {
        Board board = new Board(ChessGame.SIZE_8, ChessGame.SIZE_8);

        var pawn1 = pawn(board, Color.BLACK, 3, 4);
        var pawn2 = pawn(board, Color.BLACK, 4, 4);
        var pawn3 = pawn(board, Color.WHITE, 4, 5);

        Set<Point> moves1 = pawn1.allowedMoves();
        assertThat(moves1).containsExactlyInAnyOrder(ChessGameTest.at(4, 5)); // diagonal

        Set<Point> moves2 = pawn2.allowedMoves();
        assertThat(moves2).containsExactlyInAnyOrder(ChessGameTest.at(5, 4), ChessGameTest.at(6, 4));

        Set<Point> moves3 = pawn3.allowedMoves();
        assertThat(moves3).containsExactlyInAnyOrder(ChessGameTest.at(3, 5), ChessGameTest.at(2, 5), ChessGameTest.at(3, 4));
    }

    private Square pawn(Board board, Color color, int row, int column) {
        return board.place(new Pawn(color), ChessGameTest.at(row, column));
    }
}
