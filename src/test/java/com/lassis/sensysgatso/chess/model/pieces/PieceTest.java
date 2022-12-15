package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.ChessGame;
import com.lassis.sensysgatso.chess.model.Piece;
import com.lassis.sensysgatso.chess.model.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ArgumentsSources;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PieceTest {

    public static final int MAX = ChessGame.SIZE - 1;

    @DisplayName("Parameterized Test")
    @ParameterizedTest(name = "{2}")
    @ArgumentsSources({
            @ArgumentsSource(KingsArgumentProvider.class),
            @ArgumentsSource(BishopArgumentProvider.class),
            @ArgumentsSource(QueenArgumentProvider.class),
            @ArgumentsSource(RookArgumentProvider.class),
            @ArgumentsSource(KnightArgumentProvider.class)
    })
    void genericTest(Piece piece, Set<Position> expected, String description) {
        Set<Position> positions = piece.allowedMoves();
        assertThat(positions).containsExactlyInAnyOrderElementsOf(expected);
    }

}