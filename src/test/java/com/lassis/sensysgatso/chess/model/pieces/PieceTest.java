package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.Piece;
import com.lassis.sensysgatso.chess.model.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ArgumentsSources;

import java.util.Set;

import static com.lassis.sensysgatso.chess.game.ChessGame.SIZE_8;
import static org.assertj.core.api.Assertions.assertThat;

class PieceTest {

    public static final int MAX = SIZE_8 - 1;

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
        Board board = new Board(SIZE_8, SIZE_8);
        board.place(piece, point);

        Set<Point> points = piece.allowedMoves(board, point);
        assertThat(points).containsExactlyInAnyOrderElementsOf(expected);
    }

}
