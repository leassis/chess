package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.Color;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Set;
import java.util.stream.Stream;

import static com.lassis.sensysgatso.chess.game.ChessGame.SIZE_8;
import static com.lassis.sensysgatso.chess.game.ChessGameTest.at;
import static com.lassis.sensysgatso.chess.model.pieces.PieceTest.MAX;

public class PawnArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of(pawn(Color.BLACK, 4, 4),
                        Set.of(at(5, 4), at(6, 4)),
                        "black pawn in the middle"
                ),
                Arguments.of(pawn(Color.WHITE, 4, 4),
                        Set.of(at(3, 4), at(2, 4)),
                        "white pawn in the middle"
                ),
                Arguments.of(pawn(Color.BLACK, 1, 0),
                        Set.of(at(2, 0), at(3, 0)),
                        "black pawn in top left corner"
                ),
                Arguments.of(pawn(Color.BLACK, 1, MAX),
                        Set.of(at(2, MAX), at(3, MAX)),
                        "black pawn in top right corner"
                ),
                Arguments.of(pawn(Color.WHITE, MAX - 1, 0),
                        Set.of(at(MAX - 2, 0), at(MAX - 3, 0)),
                        "while pawn in bottom left corner"
                ),
                Arguments.of(pawn(Color.WHITE, MAX - 1, MAX),
                        Set.of(at(MAX - 2, MAX), at(MAX - 3, MAX)),
                        "black pawn in bottom right corner"
                )
        );
    }

    private Pawn pawn(Color color, int row, int column) {
        Board board = new Board(SIZE_8, SIZE_8);

        return new Pawn(color, board, at(row, column));
    }
}
