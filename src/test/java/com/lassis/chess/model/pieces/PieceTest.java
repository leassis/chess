package com.lassis.chess.model.pieces;

import com.lassis.chess.game.ChessGame;
import com.lassis.chess.model.Board;
import com.lassis.chess.model.Piece;
import com.lassis.chess.model.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ArgumentsSources;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PieceTest {

    public static final int MAX = ChessGame.SIZE_8 - 1;

    @DisplayName("Parameterized Test")
    @ParameterizedTest(name = "{3}")
    @ArgumentsSources({
            @ArgumentsSource(KingArgumentProvider.class),
            @ArgumentsSource(BishopArgumentProvider.class),
            @ArgumentsSource(QueenArgumentProvider.class),
            @ArgumentsSource(RookArgumentProvider.class),
            @ArgumentsSource(KnightArgumentProvider.class)
    })
    void genericTest(Piece piece, Point point, Set<Point> expected, String description) {
        Board board = new Board(ChessGame.SIZE_8, ChessGame.SIZE_8);
        board.place(piece, point);

        Set<Point> points = piece.allowedMoves(board, point);
        assertThat(points).containsExactlyInAnyOrderElementsOf(expected);
    }

}
